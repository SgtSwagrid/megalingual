package com.alecdorrington.megalingual
package web

import cats.effect.IO
import com.alecdorrington.megalingual.Language.*
import munit.CatsEffectSuite
import scala.collection.mutable
import scala.concurrent.duration.DurationInt
import scala.scalajs.js

/** Text to be said, as the stub speech is given it, which tells its ends. */
final class StubUtterance(val text: String) extends js.Object:

  var voice: js.Dynamic = null

  var lang: String = ""

  private val listeners = mutable.Map.empty[String, js.Function1[js.Any, Unit]]

  def addEventListener
    (
      kind: String,
      listener: js.Function1[js.Any, Unit],
    )
    : Unit = listeners(kind) = listener

  /** Tells its listener of `kind` that it happened, with `error`, if any. */
  def tell(kind: String, error: String = ""): Unit = listeners
    .get(kind)
    .foreach(_(js.Dynamic.literal(error = error)))

/**
  * The browser's speech, listing `voices` (each a name, which is also its ID, a
  * tag and whether it is local), or none until relisted if `late`, and keeping
  * what it is given to say.
  */
final class StubSpeech
  (
    voices: List[(String, String, Boolean)],
    late: Boolean,
  )
  extends js.Object:

  private var listed = !late

  private var listener = Option.empty[js.Function1[js.Any, Unit]]

  val said = mutable.ArrayBuffer.empty[StubUtterance]

  var cancels = 0

  def getVoices(): js.Array[js.Object] =
    if listed then
      js.Array(
        voices.map((name, tag, local) =>
          js.Dynamic
            .literal(
              voiceURI = name,
              name = name,
              lang = tag,
              localService = local,
            )
            .asInstanceOf[js.Object],
        )*,
      )
    else js.Array()

  def speak(utterance: StubUtterance): Unit = said += utterance

  def cancel(): Unit = cancels += 1

  def addEventListener
    (
      kind: String,
      listener: js.Function1[js.Any, Unit],
    )
    : Unit = if kind == "voiceschanged" then this.listener = Some(listener)

  def removeEventListener
    (
      kind: String,
      listener: js.Function1[js.Any, Unit],
    )
    : Unit =
    if kind == "voiceschanged" && this.listener.exists(_ eq listener) then
      this.listener = None

  /** Lists the voices at last, telling what listens. */
  def relist(): Unit =
    listed = true
    listener.foreach(_(js.Dynamic.literal()))

class SpeechSuite extends CatsEffectSuite:

  js.Dynamic.global.globalThis.SpeechSynthesisUtterance =
    js.constructorOf[StubUtterance]

  private val voices = List(
    ("Hedda", "de-DE", true),
    ("Google Deutsch", "de-DE", false),
    ("Zira", "en-US", true),
  )

  private val hedda = Voice("Hedda", "Hedda", "de-DE", true)

  /** The browser's speech over `stub`. */
  private def over(stub: StubSpeech): Speech[IO] =
    new Speech[IO](stub.asInstanceOf[SpeechSynthesisApi])

  /**
    * The ID of the voice, of those of `tags`, each on the device unless marked
    * `~` for online, that best says words of `language`, if any.
    */
  private def best(language: Language, tags: String*): Option[String] = Voice
    .best(
      language,
      tags
        .toList
        .map(tag =>
          Voice(
            tag,
            tag,
            tag.stripPrefix("~"),
            !tag.startsWith("~"),
          ),
        ),
    )
    .map(_.id)

  test(
    "A word is said in the voice of its own variety, or else the nearest in the tree, a broader language in its first variety.",
  ):
    assertEquals(
      best(
        German,
        "en-US",
        "de-CH",
        "de-AT",
        "de-DE",
      ),
      Some("de-DE"),
    )
    assertEquals(
      best(German, "en-US", "de-CH", "de-AT"),
      Some("de-AT"),
    )
    assertEquals(
      best(SwissStandardGerman, "de-DE", "de-CH"),
      Some("de-CH"),
    )
    assertEquals(
      best(English, "en-US", "en-AU", "en-GB"),
      Some("en-GB"),
    )
    assertEquals(
      best(
        SimplifiedChinese,
        "zh-HK",
        "zh-TW",
        "zh-CN",
      ),
      Some("zh-CN"),
    )
    assertEquals(
      best(SimplifiedChinese, "zh-HK", "zh-TW"),
      Some("zh-TW"),
    )
    assertEquals(
      best(SwissGerman, "de-DE", "de-CH"),
      Some("de-CH"),
    )
    assertEquals(
      best(Norwegian, "da-DK", "no-NO"),
      Some("no-NO"),
    )
    assertEquals(best(Spanish, "es_MX"), Some("es_MX"))

  test(
    "Of voices as near, one on the device is chosen before one online, else the first.",
  ):
    assertEquals(
      best(German, "~de-DE", "de-DE"),
      Some("de-DE"),
    )
    assertEquals(
      best(German, "~de-DE", "~de-de"),
      Some("~de-DE"),
    )
    assertEquals(
      best(German, "de-AT", "~de-DE"),
      Some("~de-DE"),
    )

  test(
    "No voice says a word of another tree of languages, nor of a tag that names no language.",
  ):
    assertEquals(best(French, "en-US", "de-DE"), None)
    assertEquals(best(German, "", "xx-YY"), None)
    assertEquals(best(German), None)

  test(
    "The browser's voices are listed as it lists them, at once, or once it lists them late, as Chromium does, and none where it has no speech.",
  ):
    val late = StubSpeech(voices, late = true)
    for
      soon    <- over(StubSpeech(voices, late = false)).voices
      waiting <- over(late).voices.start
      _       <- IO.sleep(10.millis) *> IO(late.relist())
      later   <- waiting.joinWithNever
      none    <- new Speech[IO](js.undefined).voices
      german  <- over(StubSpeech(voices, late = false)).voice(German)
    yield
      assertEquals(
        soon,
        List(
          hedda,
          Voice(
            "Google Deutsch",
            "Google Deutsch",
            "de-DE",
            false,
          ),
          Voice("Zira", "Zira", "en-US", true),
        ),
      )
      assertEquals(later, soon)
      assertEquals(none, Nil)
      assertEquals(german, Some(hedda))
      assertEquals(
        soon.map(_.language),
        List(
          Some(GermanGerman),
          Some(GermanGerman),
          Some(AmericanEnglish),
        ),
      )

  test(
    "Text is said at once in its voice, done once said or cut short, and fails if its voice fails.",
  ):
    val stub   = StubSpeech(voices, late = false)
    val speech = over(stub)
    for
      said <- speech.say("Hallo", hedda).start
      _    <- IO.sleep(10.millis) *> IO(stub.said(0).tell("end"))
      _    <- said.joinWithNever
      cut  <- speech.say("Eins", hedda).start
      _ <- IO.sleep(10.millis) *> IO(stub.said(1).tell("error", "interrupted"))
      _ <- cut.joinWithNever
      failing <- speech.say("Zwei", hedda).attempt.start
      _       <- IO.sleep(10.millis) *>
        IO(stub.said(2).tell("error", "synthesis-failed"))
      failed <- failing.joinWithNever
    yield
      assertEquals(
        stub.said.map(_.text).toList,
        List("Hallo", "Eins", "Zwei"),
      )
      assertEquals(stub.said(0).lang, "de-DE")
      assertEquals(
        stub.said(0).voice.name.asInstanceOf[String],
        "Hedda",
      )
      assertEquals(stub.cancels, 3)
      assertEquals(
        failed,
        Left(SpeechError("synthesis-failed")),
      )

  test(
    "Text cannot be said in a voice that the browser does not list, nor where it has no speech, as in a worker.",
  ):
    val stub = StubSpeech(voices, late = false)
    val gone = Voice("Gone", "Gone", "de-DE", true)
    for
      unlisted <- over(stub).say("Hallo", gone).attempt
      silent   <- new Speech[IO](js.undefined).say("Hallo", hedda).attempt
    yield
      assertEquals(
        unlisted,
        Left(SpeechError("no voice Gone")),
      )
      assertEquals(
        silent,
        Left(SpeechError("no speech here")),
      )
      assertEquals(stub.said.toList, Nil)

  test(
    "Cancelling what is being said cuts the browser's speech short only if nothing was begun after it, which the browser has not yet told it has cut it short.",
  ):
    val stub   = StubSpeech(voices, late = false)
    val speech = over(stub)
    for
      first  <- speech.say("Eins", hedda).start
      _      <- IO.sleep(10.millis)
      second <- speech.say("Zwei", hedda).start
      _      <- IO.sleep(10.millis)
      _      <- first.cancel
      kept   <- IO(stub.cancels)
      _      <- second.cancel
    yield
      assertEquals(kept, 2)
      assertEquals(stub.cancels, 3)
