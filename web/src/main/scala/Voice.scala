package com.alecdorrington.megalingual
package web

import io.circe.Codec

/**
  * A voice of the browser's, in which it says text aloud (see [[Speech]]).
  *
  * @param id
  *   What tells it apart from the browser's other voices.
  *
  * @param name
  *   Its name, as the browser shows it.
  *
  * @param tag
  *   The BCP 47 tag of the language it speaks, as the browser gives it.
  *
  * @param local
  *   Whether it speaks on the device itself, rather than through an online
  *   service of the browser's maker, which is sent what it says.
  */
final case class Voice
  (
    id: String,
    name: String,
    tag: String,
    local: Boolean,
  )
  derives Codec:

  /**
    * The language it speaks, among those given, if its tag names one.
    *
    * @param languages
    *   The languages known.
    */
  def language(using languages: Languages): Option[Language] = languages
    .fromTag(tag)

object Voice:

  /**
    * The voice that best says words of a language: of its tree of languages,
    * the nearest to the variety the language is spoken in
    * ([[Language.spoken]]), as German is in German German and Chinese in
    * Simplified Chinese; then one on the device before one online; then the
    * first of the given languages, as Taiwanese Mandarin comes before Cantonese
    * for Simplified Chinese; then the first voice. An app may choose among
    * fewer, as one that keeps Swiss German apart from German would choose none
    * of German (Switzerland) for it, though it is nearest in the tree.
    *
    * @param language
    *   The language.
    *
    * @param voices
    *   The voices to choose among.
    *
    * @param languages
    *   The languages known.
    *
    * @return
    *   The voice, if any is of the language's tree, as a voice of another
    *   language would read its words as its own.
    */
  def best
    (language: Language, voices: List[Voice])
    (using languages: Languages)
    : Option[Voice] =
    val spoken = language.spoken
    voices
      .flatMap(voice =>
        voice
          .language
          .flatMap(own =>
            own
              .steps(spoken)
              .map(steps => (steps, !voice.local, languages.all.indexOf(own))),
          )
          .map(_ -> voice),
      )
      .minByOption(_._1)
      .map(_._2)
