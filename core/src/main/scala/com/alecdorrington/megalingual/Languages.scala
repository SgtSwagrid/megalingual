package com.alecdorrington.megalingual

import com.alecdorrington.megalingual.languages.*

/**
  * The languages an app knows, as a tree: Megalingual's own
  * ([[Languages.builtIn]]) and any another library adds. Whatever finds a
  * language by its code or a tag, or lists languages, looks among the given
  * ones ([[Language.fromTag]], [[Language.children]], the codecs, [[Localised]]
  * and the picker), by default Megalingual's own. A library adding languages
  * gives them where they are looked for, as in
  * {{{
  * given Languages = Languages.builtIn ++ List(Klingon)
  * }}}
  *
  * @param all
  *   The languages, in the order given, each with a code of its own and its
  *   parent among them.
  */
final class Languages private (val all: List[Language]):

  /** The languages that are no variety of another, in their order. */
  def roots: List[Language] = all.filter(_.parent.isEmpty)

  /**
    * Finds the language with exactly the given code.
    *
    * @param code
    *   The language's code, as [[Language.code]] gives it.
    *
    * @return
    *   A language, if one has the code.
    */
  def fromCode(code: String): Option[Language] = all.find(_.code == code)

  /**
    * Finds the language a BCP 47 tag names, as a browser or a `lang` attribute
    * gives it, ignoring case: the language with the whole tag as its code, else
    * the first claiming it, else the language with the longest prefix of it
    * (`de-AT` finds German, `es-MX` Latin American Spanish).
    *
    * @param tag
    *   The tag, its parts joined by `-` or `_`.
    *
    * @return
    *   A language, if the tag names one.
    */
  def fromTag(tag: String): Option[Language] =
    val parts = tag.trim.split("[-_]").toList.filter(_.nonEmpty)
    (parts.size to 1 by -1)
      .iterator
      .map(parts.take(_).mkString("-"))
      .flatMap(tagged)
      .nextOption()

  /** The language with a tag as its code, else the first claiming it. */
  private def tagged(tag: String): Option[Language] = all
    .find(_.code.equalsIgnoreCase(tag))
    .orElse(all.find(_.claims.exists(_.equalsIgnoreCase(tag))))

  /**
    * Finds the languages that BCP 47 tags name, such as a browser's
    * preferences.
    *
    * @param tags
    *   The tags, most preferred first.
    *
    * @return
    *   The languages named, in the tags' order, each once.
    */
  def fromTags(tags: Seq[String]): List[Language] = tags
    .flatMap(fromTag)
    .distinct
    .toList

  /**
    * Adds languages after these, such as another library's.
    *
    * @param others
    *   The languages added, each with a code of its own and its parent among
    *   these or them. Any of these again is left where it is.
    *
    * @return
    *   These languages and the others.
    */
  def ++ (others: IterableOnce[Language]): Languages =
    Languages((all ++ others)*)

object Languages:

  /**
    * Knows the given languages, in their order.
    *
    * @param languages
    *   The languages, any given twice taken once, no two others sharing a code
    *   (ignoring case), and each one's parent among them.
    *
    * @return
    *   The languages known.
    */
  def apply(languages: Language*): Languages =
    val distinct = languages.distinct.toList
    val shared   = sharing(distinct)
    require(
      shared.isEmpty,
      s"No two languages share a code, but some share ${ shared.mkString(
          ", ",
        ) }.",
    )
    val orphans = distinct.filterNot(_.parent.forall(distinct.contains))
    require(
      orphans.isEmpty,
      s"Every parent is known, but not ${ orphans.mkString(", ") }'s.",
    )
    new Languages(distinct)

  /** The codes that more than one language has, ignoring case. */
  private def sharing(languages: List[Language]): List[String] = languages
    .groupBy(_.code.toLowerCase)
    .collect { case (code, alike) if alike.size > 1 => code }
    .toList

  /**
    * Megalingual's own languages, in alphabetical order of their English names,
    * each variety after its parent. They are given wherever no others are.
    */
  given builtIn: Languages = Languages(
    Arabic,
    Bengali,
    Bulgarian,
    Catalan,
    Chinese,
    SimplifiedChinese,
    TraditionalChinese,
    TaiwaneseChinese,
    Cantonese,
    Croatian,
    Czech,
    Danish,
    Dutch,
    English,
    BritishEnglish,
    AmericanEnglish,
    AustralianEnglish,
    Estonian,
    Filipino,
    Finnish,
    French,
    German,
    GermanGerman,
    GermanOfSwitzerland,
    SwissStandardGerman,
    SwissGerman,
    Greek,
    Hebrew,
    Hindi,
    Hungarian,
    Indonesian,
    Italian,
    Japanese,
    Korean,
    Latvian,
    Lithuanian,
    Malay,
    Norwegian,
    Persian,
    Polish,
    Portuguese,
    BrazilianPortuguese,
    EuropeanPortuguese,
    Romanian,
    Russian,
    Serbian,
    Slovak,
    Slovenian,
    Spanish,
    EuropeanSpanish,
    LatinAmericanSpanish,
    Swahili,
    Swedish,
    Tamil,
    Thai,
    Turkish,
    Ukrainian,
    Urdu,
    Vietnamese,
  )
