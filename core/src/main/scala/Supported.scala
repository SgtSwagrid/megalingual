package com.alecdorrington.megalingual

/**
  * The languages an app supports, each with what the app has in it, such as its
  * interface's text. A language outside them is read in the nearest one: its
  * first supported variety, else its parent's nearest, else the default.
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
final class Supported[+A] private (
  val default: Language,
  values: PartialFunction[Language, A],
):

  /** The supported languages, in declared order. */
  val languages: List[Language] = Language.values.toList.filter(supports)

  /**
    * The supported languages and every language one is a variety of, in
    * declared order, as a picker offers them: a broader language is read in its
    * [[nearest]] supported one.
    */
  def listed: List[Language] = Language
    .values
    .toList
    .filter(language => languages.exists(_.within(language)))

  /**
    * Whether the app supports the language itself.
    *
    * @param language
    *   The language.
    */
  def supports(language: Language): Boolean = values.isDefinedAt(language)

  /**
    * Finds the supported language a language is read in: itself, else its first
    * supported variety, else its parent's nearest.
    *
    * @param language
    *   The language.
    *
    * @return
    *   A supported language, if the language's tree has one.
    */
  def nearest(language: Language): Option[Language] =
    (language :: language.descendants)
      .find(supports)
      .orElse(language.parent.flatMap(nearest))

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
    * Chooses the language a reader reads in: the first of their choice and then
    * their preferences with a [[nearest]] supported language, preferring a
    * variety they also prefer (English is American English to a reader
    * preferring `en-US`), else the default.
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
    : Language = (chosen.toList ++ preferences)
    .iterator
    .flatMap(language => preferred(language, preferences))
    .nextOption()
    .getOrElse(default)

  /**
    * The first preference within a language that is supported, else its
    * nearest.
    */
  private def preferred
    (
      language: Language,
      preferences: Seq[Language],
    )
    : Option[Language] = preferences
    .find(preference => preference.within(language) && supports(preference))
    .orElse(nearest(language))

object Supported:

  /**
    * Supports the languages a partial function is defined for, each with what
    * it gives, as in
    * {{{
    * Supported(BritishEnglish):
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
    *   A set of supported languages.
    */
  def apply[A]
    (default: Language)
    (values: PartialFunction[Language, A])
    : Supported[A] =
    require(
      values.isDefinedAt(default),
      s"The default, $default, is supported.",
    )
    new Supported(default, values)

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
    *   A set of supported languages, each giving itself.
    */
  def only(default: Language, others: Language*): Supported[Language] =
    val supported = others.toSet + default
    apply(default) { case language if supported(language) => language }

  /**
    * Supports every language, with nothing in them but themselves.
    *
    * @param default
    *   The language read in when nothing chooses another.
    *
    * @return
    *   A set of every language, each giving itself.
    */
  def all(default: Language): Supported[Language] =
    only(default, Language.values*)
