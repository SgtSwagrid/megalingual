package com.alecdorrington.megalingual

import com.alecdorrington.megalingual.Direction.{LeftToRight, RightToLeft}
import com.alecdorrington.megalingual.names.Names
import io.circe.{Codec, Decoder, Encoder, KeyDecoder, KeyEncoder}

/**
  * A language, perhaps a variety of another.
  *
  * Languages form a tree of any depth: a language's `parent` is the broader
  * language it is a variety of (British English of English), and nothing is
  * read from a code's shape. A broader language is read in its first variety
  * where a more specific one is needed.
  *
  * An app supporting only some languages says so with a [[Localised]], and
  * never matches on every language itself, so that a language added here breaks
  * no app's build.
  *
  * @param code
  *   The language's BCP 47 tag, as specific as tells it from the others (`en`,
  *   `en-GB`, `es-419`). A variety's need not begin with its parent's.
  *
  * @param direction
  *   The direction the language is written in.
  *
  * @param parent
  *   The language this is a variety of, if any, declared before it.
  */
enum Language
  (
    val code: String,
    val direction: Direction,
    val parent: Option[Language] = None,
  ):

  /** The language's flag, drawn when first shown. */
  lazy val flag: Flag = Flag.of(this)

  /**
    * Names the language in another, as written mid-sentence there (`anglais` in
    * French).
    *
    * @param in
    *   The language to name it in.
    *
    * @return
    *   A name for the language.
    */
  def name(in: Language): String = Names(this, in)

  /**
    * Names the language in another, as it begins a menu's item or a heading
    * there (`Anglais` in French): [[name]] with its first letter in upper case.
    *
    * @param in
    *   The language to name it in.
    *
    * @return
    *   A name for the language.
    */
  def title(in: Language): String = name(in).capitalize

  /**
    * Finds the plural category of a whole number in the language, by Unicode's
    * CLDR rules (`22` is [[Plural.Few]] in Polish).
    *
    * @param count
    *   The number of things counted.
    *
    * @return
    *   A category, which the language words its own way.
    */
  def plural(count: Long): Plural = Plural.of(this, count)

  /** The languages this is a variety of, the nearest first. */
  def ancestors: List[Language] = parent.toList.flatMap(_.lineage)

  /** This language, then each language it is a variety of, nearest first. */
  def lineage: List[Language] = this :: ancestors

  /** The direct varieties, in declared order, the first standing in for it. */
  def children: List[Language] = Language
    .values
    .toList
    .filter(_.parent.contains(this))

  /** Every variety of this language, however deep, each before its own. */
  def descendants: List[Language] =
    children.flatMap(child => child :: child.descendants)

  /**
    * Whether this language is the given one, or a variety of it.
    *
    * @param language
    *   The broader language.
    */
  def within(language: Language): Boolean = lineage.contains(language)

  /**
    * The languages outside its tree that a reader of this one reads best after
    * it, before its parent's: those written in the same script, or by the same
    * readers.
    */
  private[megalingual] def akin: List[Language] = this match
    case Cantonese           => List(TraditionalChinese)
    case TraditionalChinese  => List(Cantonese)
    case SwissStandardGerman => List(SwissGerman)
    case SwissGerman         => List(SwissStandardGerman)
    case _                   => Nil

  /** Whether a BCP 47 tag is this language's code or one it claims. */
  private def tagged(tag: String): Boolean = (code :: claims).exists(
    _.equalsIgnoreCase(tag),
  )

  /**
    * The tags besides its code that choose this language where the tree would
    * choose another, or none: `es-MX` would find Spanish, and `no` nothing.
    */
  private def claims: List[String] = this match
    case LatinAmericanSpanish => List(
        "AR",
        "BO",
        "CL",
        "CO",
        "CR",
        "CU",
        "DO",
        "EC",
        "GT",
        "HN",
        "MX",
        "NI",
        "PA",
        "PE",
        "PR",
        "PY",
        "SV",
        "US",
        "UY",
        "VE",
      ).map("es-" + _)
    case EuropeanPortuguese => List("AO", "CV", "GW", "MO", "MZ", "ST", "TL")
        .map("pt-" + _)
    case SwissStandardGerman => List("de-LI")
    case Cantonese           => List(
        "zh-MO",
        "zh-Hant-HK",
        "zh-Hant-MO",
        "yue",
      )
    case TraditionalChinese => List("zh-Hant", "zh-Hant-TW")
    case Norwegian          => List("no")
    case _                  => Nil

  /** Modern Standard Arabic. */
  case Arabic extends Language("ar", RightToLeft)

  case Bengali extends Language("bn", LeftToRight)

  case Bulgarian extends Language("bg", LeftToRight)

  case Catalan extends Language("ca", LeftToRight)

  case Chinese extends Language("zh", LeftToRight)

  /**
    * Mandarin in simplified characters. As Chinese's first variety, it is what
    * tags such as `zh` and `zh-SG` find where a variety is needed.
    */
  case SimplifiedChinese
    extends Language(
      "zh-CN",
      LeftToRight,
      parent = Some(Chinese),
    )

  /**
    * Mandarin in traditional characters, as in Taiwan. It claims the
    * traditional script's tags naming no other region, which would otherwise
    * find simplified Chinese.
    */
  case TraditionalChinese
    extends Language(
      "zh-TW",
      LeftToRight,
      parent = Some(Chinese),
    )

  /**
    * Cantonese in traditional characters. Having no two-letter code, it is
    * `zh-HK`, the tag its readers' browsers send.
    */
  case Cantonese
    extends Language(
      "zh-HK",
      LeftToRight,
      parent = Some(Chinese),
    )

  case Croatian extends Language("hr", LeftToRight)

  case Czech extends Language("cs", LeftToRight)

  case Danish extends Language("da", LeftToRight)

  case Dutch extends Language("nl", LeftToRight)

  case English extends Language("en", LeftToRight)

  case BritishEnglish
    extends Language(
      "en-GB",
      LeftToRight,
      parent = Some(English),
    )

  case AmericanEnglish
    extends Language(
      "en-US",
      LeftToRight,
      parent = Some(English),
    )

  case AustralianEnglish
    extends Language(
      "en-AU",
      LeftToRight,
      parent = Some(English),
    )

  case Estonian extends Language("et", LeftToRight)

  /** Under `fil`, the tag browsers send for it, as `tl` names Tagalog. */
  case Filipino extends Language("fil", LeftToRight)

  case Finnish extends Language("fi", LeftToRight)

  case French extends Language("fr", LeftToRight)

  case German extends Language("de", LeftToRight)

  case GermanGerman
    extends Language(
      "de-DE",
      LeftToRight,
      parent = Some(German),
    )

  /** Swiss Standard German, written without `ß`. */
  case SwissStandardGerman
    extends Language(
      "de-CH",
      LeftToRight,
      parent = Some(German),
    )

  /**
    * The Alemannic dialects of Switzerland, under `gsw`, their own code:
    * `de-CH` is Swiss Standard German, which their speakers mostly write
    * instead.
    */
  case SwissGerman extends Language("gsw", LeftToRight, parent = Some(German))

  case Greek extends Language("el", LeftToRight)

  case Hebrew extends Language("he", RightToLeft)

  case Hindi extends Language("hi", LeftToRight)

  case Hungarian extends Language("hu", LeftToRight)

  case Indonesian extends Language("id", LeftToRight)

  case Italian extends Language("it", LeftToRight)

  case Japanese extends Language("ja", LeftToRight)

  case Korean extends Language("ko", LeftToRight)

  case Latvian extends Language("lv", LeftToRight)

  case Lithuanian extends Language("lt", LeftToRight)

  case Malay extends Language("ms", LeftToRight)

  /** Bokmål, under its own code, claiming Norwegian's `no`. */
  case Norwegian extends Language("nb", LeftToRight)

  case Persian extends Language("fa", RightToLeft)

  case Polish extends Language("pl", LeftToRight)

  case Portuguese extends Language("pt", LeftToRight)

  case BrazilianPortuguese
    extends Language(
      "pt-BR",
      LeftToRight,
      parent = Some(Portuguese),
    )

  case EuropeanPortuguese
    extends Language(
      "pt-PT",
      LeftToRight,
      parent = Some(Portuguese),
    )

  case Romanian extends Language("ro", LeftToRight)

  case Russian extends Language("ru", LeftToRight)

  /** Serbian in Cyrillic, its official script. */
  case Serbian extends Language("sr", LeftToRight)

  case Slovak extends Language("sk", LeftToRight)

  case Slovenian extends Language("sl", LeftToRight)

  case Spanish extends Language("es", LeftToRight)

  case EuropeanSpanish
    extends Language(
      "es-ES",
      LeftToRight,
      parent = Some(Spanish),
    )

  case LatinAmericanSpanish
    extends Language(
      "es-419",
      LeftToRight,
      parent = Some(Spanish),
    )

  case Swahili extends Language("sw", LeftToRight)

  case Swedish extends Language("sv", LeftToRight)

  case Tamil extends Language("ta", LeftToRight)

  case Thai extends Language("th", LeftToRight)

  case Turkish extends Language("tr", LeftToRight)

  case Ukrainian extends Language("uk", LeftToRight)

  case Urdu extends Language("ur", RightToLeft)

  case Vietnamese extends Language("vi", LeftToRight)

object Language:

  /** The languages that are no variety of another, in declared order. */
  def roots: List[Language] = values.toList.filter(_.parent.isEmpty)

  /**
    * Finds the language with exactly the given code.
    *
    * @param code
    *   The language's code, as [[Language.code]] gives it.
    *
    * @return
    *   A language, if one has the code.
    */
  def fromCode(code: String): Option[Language] = values.find(_.code == code)

  /**
    * Finds the language a BCP 47 tag names, as a browser or a `lang` attribute
    * gives it, ignoring case: the language with the whole tag as its code or
    * claiming it, else with the longest prefix of it (`de-AT` finds German,
    * `es-MX` Latin American Spanish).
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
      .flatMap(prefix => values.find(_.tagged(prefix)))
      .nextOption()

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

  /** Kept as its code, refusing any other. */
  given Codec[Language] = Codec.from(
    Decoder
      .decodeString
      .emap(code => fromCode(code).toRight(s"No language has the code $code.")),
    Encoder.encodeString.contramap(_.code),
  )

  /** A map's key kept as its code. */
  given KeyEncoder[Language] = KeyEncoder.encodeKeyString.contramap(_.code)

  /** A map's key read from its code, refusing any other. */
  given KeyDecoder[Language] = KeyDecoder.instance(fromCode)
