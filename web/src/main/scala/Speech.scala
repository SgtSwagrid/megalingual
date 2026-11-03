package com.alecdorrington.megalingual
package web

import cats.effect.Async
import cats.effect.syntax.all.*
import cats.syntax.all.*
import org.scalajs.dom.{Event, EventTarget}
import scala.annotation.unused
import scala.concurrent.duration.{DurationInt, FiniteDuration}
import scala.scalajs.js
import scala.scalajs.js.annotation.JSGlobal

/**
  * What the browser can say aloud, in its own voices, through the Web Speech
  * API, which needs no permission. Its voices read text as it is spelt: none
  * reads IPA, nor the SSML that would carry it. A page, an extension's pages
  * and its content scripts have them, but not a worker, as an extension's
  * service worker is. It reads the global `speechSynthesis` as it is asked.
  *
  * @tparam F
  *   The effect in which it speaks.
  */
final class Speech[F[_] : Async] private[web] (
  synthesis: => js.UndefOr[SpeechSynthesisApi],
):

  /**
    * The browser's voices, once it has listed them: at once if it has, or else
    * once it does, as Chromium lists none until first asked, waiting at most
    * [[Speech.patience]].
    *
    * @return
    *   The voices, in the browser's order, none where it has no speech.
    */
  def voices: F[List[Voice]] =
    Async[F].defer(synthesis.fold(List.empty[Voice].pure[F])(listing))

  /** The voices that `synthesis` lists, waiting for it to list them. */
  private def listing(synthesis: SpeechSynthesisApi): F[List[Voice]] = Async[F]
    .delay(Speech.listed(synthesis))
    .flatMap(listed =>
      if listed.nonEmpty then listed.pure[F]
      else
        Speech
          .relisted[F](synthesis)
          .timeoutTo(Speech.patience, Async[F].unit) *>
          Async[F].delay(Speech.listed(synthesis)),
    )

  /**
    * The browser's voice that best says words of a language (see
    * [[Voice.best]]).
    *
    * @param language
    *   The language.
    *
    * @param languages
    *   The languages known.
    *
    * @return
    *   The voice, if the browser has one of the language's tree.
    */
  def voice(language: Language)(using languages: Languages): F[Option[Voice]] =
    voices.map(Voice.best(language, _))

  /**
    * Says text aloud in a voice, cutting short whatever the browser was saying,
    * even what a page itself was, as it says one thing at a time. It begins in
    * its first step, so that it still has the user's activation, which a
    * browser may need to speak, when run at once from an event's handler (as
    * `IO`'s `syncStep` runs it). Cancelled, it stops saying it.
    *
    * @param text
    *   The text, read as it is spelt.
    *
    * @param voice
    *   One of the browser's [[voices]].
    *
    * @return
    *   Nothing, once the text has been said, or cut short by something else
    *   said. It fails with a [[SpeechError]] where the text cannot be said: the
    *   browser has no speech, no longer has the voice, or the voice fails.
    */
  def say(text: String, voice: Voice): F[Unit] = Async[F]
    .delay(begin(text, voice))
    .flatMap(_.liftTo[F])
    .flatMap(saying =>
      Async[F].async[Unit](done =>
        Async[F].delay:
          saying.whenDone(failure =>
            done(failure.map(SpeechError(_)).toLeft(())),
          )
          Some(Async[F].delay(saying.stop())),
      ),
    )

  /** Begins saying `text` in `voice`, if the browser can. */
  private def begin(text: String, voice: Voice): Either[SpeechError, Saying] =
    for
      synthesis <- synthesis.toOption.toRight(SpeechError("no speech here"))
      found     <- synthesis
        .getVoices()
        .find(_.voiceURI == voice.id)
        .toRight(SpeechError(s"no voice ${ voice.name }"))
    yield
      val utterance = SpeechSynthesisUtterance(text)
      utterance.voice = found
      utterance.lang = found.lang
      Saying.begin(synthesis, utterance)

object Speech:

  /**
    * The browser's speech, in the effect `F`.
    *
    * @tparam F
    *   The effect in which it speaks.
    *
    * @return
    *   The speech, reading the global `speechSynthesis` as it is asked.
    */
  def apply[F[_] : Async]: Speech[F] = new Speech[F](synthesis)

  /**
    * How long to wait for the browser to list its voices, where it has listed
    * none when first asked.
    */
  val patience: FiniteDuration = 1.second

  /** The browser's speech, where there is any. */
  private def synthesis: js.UndefOr[SpeechSynthesisApi] = js
    .Dynamic
    .global
    .globalThis
    .speechSynthesis
    .asInstanceOf[js.UndefOr[SpeechSynthesisApi]]

  /** The voices that `synthesis` lists now. */
  private def listed(synthesis: SpeechSynthesisApi): List[Voice] = synthesis
    .getVoices()
    .toList
    .map(voice =>
      Voice(
        voice.voiceURI,
        voice.name,
        voice.lang,
        voice.localService,
      ),
    )

  /** Nothing, once `synthesis` lists its voices again. */
  private def relisted[F[_] : Async](synthesis: SpeechSynthesisApi): F[Unit] =
    Async[F].async[Unit](done =>
      Async[F].delay:
        lazy val listener: js.Function1[Event, Unit] = _ =>
          synthesis.removeEventListener("voiceschanged", listener)
          done(Right(()))
        synthesis.addEventListener("voiceschanged", listener)
        Some(Async[F].delay(
          synthesis.removeEventListener("voiceschanged", listener),
        )),
    )

/**
  * What the browser is saying, an `utterance` of `synthesis`, until it has been
  * said, or cut short, or has failed. It holds the utterance while what says it
  * waits for it, as Chromium may otherwise collect it and never tell it that it
  * ended.
  */
private[web] final class Saying private (
  synthesis: SpeechSynthesisApi,
  val utterance: SpeechSynthesisUtterance,
):

  /**
    * How it ended, once it has: with why it failed, if it did, or with none if
    * it was said or only cut short.
    */
  private var ended = Option.empty[Option[String]]

  /** What waits for it to end, each given why it failed, if it did. */
  private var waiting = List.empty[Option[String] => Unit]

  utterance.addEventListener("end", (_: Event) => end(None))

  utterance.addEventListener(
    "error",
    (event: Event) => end(Saying.failure(event)),
  )

  private def end(failure: Option[String]): Unit = if ended.isEmpty then
    ended = Some(failure)
    waiting.foreach(_(failure))
    waiting = Nil

  /**
    * Gives `then` why it failed, if it did, once it has ended, or at once if it
    * has.
    */
  def whenDone(`then`: Option[String] => Unit): Unit = ended match
    case Some(failure) => `then`(failure)
    case None          => waiting = waiting :+ `then`

  /**
    * Stops saying it, if it is still being said and nothing was begun after it:
    * cancelling cuts short whatever the browser is saying, and one cut short
    * still counts as being said until the browser tells it so, a moment after
    * what cut it short began.
    */
  def stop(): Unit =
    if ended.isEmpty && Saying.latest.exists(_ eq this) then synthesis.cancel()

private[web] object Saying:

  /** What was begun last, which alone may cut the browser's speech short. */
  private var latest = Option.empty[Saying]

  /**
    * Begins saying `utterance`, cutting short whatever `synthesis` was saying,
    * all at once, so that it still has the user's activation.
    */
  def begin
    (
      synthesis: SpeechSynthesisApi,
      utterance: SpeechSynthesisUtterance,
    )
    : Saying =
    val saying = new Saying(synthesis, utterance)
    latest = Some(saying)
    synthesis.cancel()
    synthesis.speak(utterance)
    saying

  /**
    * Why an utterance failed, as its `error` event says, if it did: not if it
    * was only cut short, as by another.
    */
  private def failure(event: Event): Option[String] =
    val error = event.asInstanceOf[SpeechSynthesisErrorEventApi].error
    Option.unless(Set("interrupted", "canceled")(error))(error)

/** Why the browser could not say something, its `reason`. */
final case class SpeechError(reason: String)
  extends Exception(s"The browser could not say it: $reason.")

/** The browser's speech, as the Web Speech API gives it. */
@js.native
private[web] trait SpeechSynthesisApi extends EventTarget:

  def getVoices(): js.Array[SpeechSynthesisVoiceApi] = js.native

  def speak(utterance: SpeechSynthesisUtterance): Unit = js.native

  def cancel(): Unit = js.native

@js.native
private[web] trait SpeechSynthesisVoiceApi extends js.Object:

  val voiceURI: String      = js.native
  val name: String          = js.native
  val lang: String          = js.native
  val localService: Boolean = js.native

@js.native
private[web] trait SpeechSynthesisErrorEventApi extends Event:

  val error: String = js.native

/** Text to be said aloud, in a `voice` and `lang`. */
@js.native
@JSGlobal
private[web] class SpeechSynthesisUtterance
  (
    @unused
    text: String,
  )
  extends EventTarget:

  var voice: SpeechSynthesisVoiceApi = js.native

  var lang: String = js.native
