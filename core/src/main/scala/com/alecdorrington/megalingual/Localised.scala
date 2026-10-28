package com.alecdorrington.megalingual

/**
  * What an app has in each language it supports, such as its interface's text.
  * A language outside them is read in the nearest one: its first supported
  * variety, else one in the same script, else its parent's nearest, else the
  * default.
  *
  * The languages are a partial function's domain, so that an app names only
  * those it supports, never every [[Language]], and what it has in one is made
  * only once that one is read.
  *
  * @param default
  *   The language read in when nothing chooses another.
  *
  * @param values
  *   What the app has in each language it supports.
  *
  * @tparam A
  *   What the app has in each language.
  */
final class Localised[+A] private (
  val default: Language,
  values: PartialFunction[Language, A],
):

  /** The supported languages, in declared order. */
  val languages: List[Language] = Language.values.toList.filter(supports)

  /**
    * The supported languages and every language two of them are varieties of,
    * in declared order, as a picker offers them: a broader language is read in
    * its [[nearest]] supported one. A broader language with one supported
    * variety would only offer it twice.
    */
  def listed: List[Language] = Language
    .values
    .toList
    .filter(language =>
      supports(language) || languages.count(_.within(language)) > 1,
    )

  /**
    * Whether the app supports the language itself.
    *
    * @param language
    *   The language.
    */
  def supports(language: Language): Boolean = values.isDefinedAt(language)

  /**
    * Finds the supported language a language is read in: itself, else its first
    * supported variety, else one in the same script (traditional Chinese for
    * Cantonese), else its parent's nearest.
    *
    * @param language
    *   The language.
    *
    * @return
    *   A supported language, if the language's tree has one.
    */
  def nearest(language: Language): Option[Language] = beneath(language)
    .orElse(language.akin.iterator.flatMap(beneath).nextOption())
    .orElse(language.parent.flatMap(nearest))

  /** Itself, else its first supported variety. */
  private def beneath(language: Language): Option[Language] =
    (language :: language.descendants).find(supports)

  /**
    * Finds the supported language a language is read in: its [[nearest]], else
    * the default.
    *
    * @param language
    *   The language.
    *
    * @return
    *   A supported language.
    */
  def resolve(language: Language): Language =
    nearest(language).getOrElse(default)

  /**
    * Finds what the app has in a language, as [[resolve]] reads it.
    *
    * @param language
    *   The language.
    *
    * @return
    *   What the app has in the language it is read in.
    */
  def apply(language: Language): A = values(resolve(language))

  /**
    * Chooses the language a reader reads in. Their choice comes first, read in
    * its [[nearest]] supported language. Then each of their preferences in
    * turn, if it or a variety of it is supported, before any is read in another
    * of the same script or its parent's, so that a language the reader names is
    * never passed over. A broader language is read in a variety the reader also
    * prefers (English is American English to a reader preferring `en-US`). Else
    * the default.
    *
    * @param chosen
    *   The language the reader chose, if any.
    *
    * @param preferences
    *   The reader's languages, most preferred first, such as a browser's
    *   ([[Language.fromTags]]).
    *
    * @return
    *   A supported language.
    */
  def negotiate
    (
      chosen: Option[Language],
      preferences: Seq[Language],
    )
    : Language = chosen
    .flatMap(language => close(language, preferences).orElse(nearest(language)))
    .orElse(preferences.iterator.flatMap(close(_, preferences)).nextOption())
    .orElse(preferences.iterator.flatMap(nearest).nextOption())
    .getOrElse(default)

  /**
    * The first preference within a language that is supported, else the
    * language itself or its first supported variety, never another.
    */
  private def close
    (
      language: Language,
      preferences: Seq[Language],
    )
    : Option[Language] = preferences
    .find(preference => preference.within(language) && supports(preference))
    .orElse(beneath(language))

object Localised:

  /**
    * Supports the languages a partial function is defined for, each with what
    * it gives, as in
    * {{{
    * Localised(BritishEnglish):
    *   case BritishEnglish => britishStrings
    *   case French         => frenchStrings
    * }}}
    *
    * @param default
    *   The language read in when nothing chooses another, which must be one of
    *   them.
    *
    * @param values
    *   What the app has in each language it supports.
    *
    * @tparam A
    *   What the app has in each language.
    *
    * @return
    *   What the app has in each language it supports.
    */
  def apply[A]
    (default: Language)
    (values: PartialFunction[Language, A])
    : Localised[A] =
    require(
      values.isDefinedAt(default),
      s"The default, $default, is supported.",
    )
    new Localised(default, values)

  /**
    * Supports the given languages, with nothing in them but themselves.
    *
    * @param default
    *   The language read in when nothing chooses another.
    *
    * @param others
    *   The other languages supported.
    *
    * @return
    *   The languages, each giving itself.
    */
  def only(default: Language, others: Language*): Localised[Language] =
    val supported = others.toSet + default
    apply(default) { case language if supported(language) => language }

  /**
    * Supports every language, with nothing in them but themselves.
    *
    * @param default
    *   The language read in when nothing chooses another.
    *
    * @return
    *   Every language, each giving itself.
    */
  def all(default: Language): Localised[Language] =
    only(default, Language.values*)
