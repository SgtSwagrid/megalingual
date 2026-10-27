package com.alecdorrington.megalingual

import com.alecdorrington.megalingual.Direction.{LeftToRight, RightToLeft}
import com.alecdorrington.megalingual.names.Names
import io.circe.{Codec, Decoder, Encoder}

/**
  * A language, perhaps a variety of another.
  *
  * Languages form a tree of any depth: a language's `parent` is the broader
  * language it is a variety of (British English of English), and nothing is
  * read from a code's shape. A broader language is read in its first variety
  * where a more specific one is needed.
  *
  * An app supporting only some languages says so with a [[Supported]], and
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
  * @param drawn
  *   The language's flag, or `Flag.Varieties` for a language whose varieties
  *   are spoken in different countries, which shows its code instead.
  *
  * @param parent
  *   The language this is a variety of, if any, declared before it.
  */
enum Language
  (
    val code: String,
    val direction: Direction,
    drawn: Flag | Flag.Varieties.type,
    val parent: Option[Language] = None,
  ):

  /** The flag: its own, or a tile of its code. */
  lazy val flag: Flag = drawn match
    case flag: Flag     => flag
    case Flag.Varieties => Flag.tile(code.takeWhile(_ != '-').map(_.toUpper))

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
  def plural(count: Int): Plural = Plural.of(this, count)

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
    case SwissGerman => List("de-LI")
    case Cantonese   => List(
        "zh-MO",
        "zh-Hant-HK",
        "zh-Hant-MO",
        "yue",
      )
    case TraditionalChinese => List("zh-Hant", "zh-Hant-TW")
    case Norwegian          => List("no")
    case _                  => Nil

  /** Modern Standard Arabic. */
  case Arabic
    extends Language(
      "ar",
      RightToLeft,
      Flag.of(
        """<rect width="60" height="40" fill="#006c35"/>""" +
          """<path d="M16,17 Q20,12 24,16 T32,16 T40,15 T44,14" fill="none" """ +
          """stroke="#ffffff" stroke-width="2"/>""" +
          """<path d="M16,27 H44 M40,25 V29" stroke="#ffffff" """ +
          """stroke-width="1.6"/>""",
      ),
    )

  case Bengali
    extends Language(
      "bn",
      LeftToRight,
      Flag.of(
        """<rect width="60" height="40" fill="#006a4e"/>""" +
          """<circle cx="27" cy="20" r="12" fill="#f42a41"/>""",
      ),
    )

  case Bulgarian
    extends Language(
      "bg",
      LeftToRight,
      Flag.bands(
        vertical = false,
        "#ffffff",
        "#00966e",
        "#d62612",
      ),
    )

  case Catalan
    extends Language(
      "ca",
      LeftToRight,
      Flag.bands(
        vertical = false,
        "#fcdd09",
        "#da121a",
        "#fcdd09",
        "#da121a",
        "#fcdd09",
        "#da121a",
        "#fcdd09",
        "#da121a",
        "#fcdd09",
      ),
    )

  case Chinese extends Language("zh", LeftToRight, Flag.Varieties)

  /**
    * Mandarin in simplified characters. As Chinese's first variety, it is what
    * tags such as `zh` and `zh-SG` find where a variety is needed.
    */
  case SimplifiedChinese
    extends Language(
      "zh-CN",
      LeftToRight,
      Flag.of(
        """<rect width="60" height="40" fill="#ee1c25"/>""" +
          Flag.star(10, 10, 6, "#ffff00") + Flag.star(20, 4, 2, "#ffff00") +
          Flag.star(24, 8, 2, "#ffff00") + Flag.star(24, 14, 2, "#ffff00") +
          Flag.star(20, 18, 2, "#ffff00"),
      ),
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
      Flag.of(
        """<rect width="60" height="40" fill="#fe0000"/>""" +
          """<rect width="30" height="20" fill="#000095"/>""" + Flag.star(
            15,
            10,
            7.5,
            "#ffffff",
            points = 12,
            inner = 0.6,
          ) + """<circle cx="15" cy="10" r="4.2" fill="#000095"/>""" +
          """<circle cx="15" cy="10" r="3.6" fill="#ffffff"/>""",
      ),
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
      Flag.of(
        """<rect width="60" height="40" fill="#de2910"/>""" + (0 until 5)
          .map(petal =>
            """<ellipse cx="30" cy="13.5" rx="3.5" ry="6.5" fill="#ffffff" """ +
              s"""transform="rotate(${ petal * 72 } 30 20)"/>""",
          )
          .mkString,
      ),
      parent = Some(Chinese),
    )

  case Croatian
    extends Language(
      "hr",
      LeftToRight,
      Flag.of(
        """<rect width="60" height="40" fill="#ffffff"/>""" +
          """<rect width="60" height="13.33" fill="#ff0000"/>""" +
          """<rect y="26.67" width="60" height="13.33" fill="#171796"/>""" +
          """<rect x="24" y="11" width="12" height="15" fill="#ffffff" """ +
          """stroke="#ff0000" stroke-width="0.8"/>""" +
          (for
            row    <- 0 until 5
            column <- 0 until 4 if (row + column) % 2 == 0
          yield s"""<rect x="${ 24 + column * 3 }" y="${ 11 + row * 3 }" """ +
            """width="3" height="3" fill="#ff0000"/>""").mkString,
      ),
    )

  case Czech
    extends Language(
      "cs",
      LeftToRight,
      Flag.of(
        """<rect width="60" height="20" fill="#ffffff"/>""" +
          """<rect y="20" width="60" height="20" fill="#d7141a"/>""" +
          """<polygon points="0,0 30,20 0,40" fill="#11457e"/>""",
      ),
    )

  case Danish
    extends Language(
      "da",
      LeftToRight,
      Flag.nordicCross("#c8102e", "#ffffff"),
    )

  case Dutch
    extends Language(
      "nl",
      LeftToRight,
      Flag.bands(
        vertical = false,
        "#ae1c28",
        "#ffffff",
        "#21468b",
      ),
    )

  case English extends Language("en", LeftToRight, Flag.Varieties)

  case BritishEnglish
    extends Language(
      "en-GB",
      LeftToRight,
      Flag.unionFlag,
      parent = Some(English),
    )

  case AmericanEnglish
    extends Language(
      "en-US",
      LeftToRight,
      Flag.of(
        """<rect width="60" height="40" fill="#b31942"/>""" +
          """<path d="M0,4.62 H60 M0,10.77 H60 M0,16.92 H60 M0,23.08 H60 """ +
          """M0,29.23 H60 M0,35.38 H60" stroke="#ffffff" stroke-width="3.08"/>""" +
          """<rect width="24" height="21.54" fill="#0a3161"/>""",
      ),
      parent = Some(English),
    )

  /** Focused on the Commonwealth Star, which no other flag has. */
  case AustralianEnglish
    extends Language(
      "en-AU",
      LeftToRight,
      Flag.of(
        """<rect width="60" height="40" fill="#012169"/>""" +
          Flag.canton(Flag.unionFlag) + List(
            (15.0, 30.0, 6.0, 7),   // the Commonwealth Star
            (45.0, 33.33, 3.0, 7),  // Alpha Crucis
            (37.5, 17.5, 3.0, 7),   // Beta Crucis
            (45.0, 6.67, 3.0, 7),   // Gamma Crucis
            (51.67, 14.83, 3.0, 7), // Delta Crucis
            (48.0, 21.67, 1.8, 5),  // Epsilon Crucis
          ).map((x, y, radius, points) =>
              Flag.star(
                x,
                y,
                radius,
                "#ffffff",
                points,
                inner = 4.0 / 9,
              ),
            )
            .mkString,
      ),
      parent = Some(English),
    )

  case Estonian
    extends Language(
      "et",
      LeftToRight,
      Flag.bands(
        vertical = false,
        "#0072ce",
        "#000000",
        "#ffffff",
      ),
    )

  /** Under `fil`, the tag browsers send for it, as `tl` names Tagalog. */
  case Filipino
    extends Language(
      "fil",
      LeftToRight,
      Flag.of(
        """<rect width="60" height="20" fill="#0038a8"/>""" +
          """<rect y="20" width="60" height="20" fill="#ce1126"/>""" +
          """<polygon points="0,0 34.64,20 0,40" fill="#ffffff"/>""" +
          """<circle cx="11.5" cy="20" r="4" fill="#fcd116"/>""" +
          Flag.star(4, 5, 2, "#fcd116") + Flag.star(4, 35, 2, "#fcd116") +
          Flag.star(28, 20, 2, "#fcd116"),
      ),
    )

  case Finnish
    extends Language(
      "fi",
      LeftToRight,
      Flag.nordicCross("#ffffff", "#002f6c"),
    )

  case French
    extends Language(
      "fr",
      LeftToRight,
      Flag.bands(
        vertical = true,
        "#0055a4",
        "#ffffff",
        "#ef4135",
      ),
    )

  case German extends Language("de", LeftToRight, Flag.Varieties)

  case GermanGerman
    extends Language(
      "de-DE",
      LeftToRight,
      Flag.bands(
        vertical = false,
        "#000000",
        "#dd0000",
        "#ffce00",
      ),
      parent = Some(German),
    )

  /** Swiss Standard German, written without `ß`. */
  case SwissGerman
    extends Language(
      "de-CH",
      LeftToRight,
      Flag.of(
        """<rect width="60" height="40" fill="#da291c"/>""" +
          """<path d="M30,8 V32 M18,20 H42" stroke="#ffffff" stroke-width="8"/>""",
      ),
      parent = Some(German),
    )

  case Greek
    extends Language(
      "el",
      LeftToRight,
      Flag.of(
        """<rect width="60" height="40" fill="#0d5eaf"/>""" +
          """<path d="M0,6.67 H60 M0,15.56 H60 M0,24.44 H60 M0,33.33 H60" """ +
          """stroke="#ffffff" stroke-width="4.44"/>""" +
          """<rect width="22.22" height="22.22" fill="#0d5eaf"/>""" +
          """<path d="M11.11,0 V22.22 M0,11.11 H22.22" stroke="#ffffff" """ +
          """stroke-width="4.44"/>""",
      ),
    )

  case Hebrew
    extends Language(
      "he",
      RightToLeft,
      Flag.of(
        """<rect width="60" height="40" fill="#ffffff"/>""" +
          """<path d="M0,6.5 H60 M0,33.5 H60" stroke="#0038b8" """ +
          """stroke-width="5"/>""" +
          """<path d="M30,12 L36.93,24 H23.07 Z M30,28 L36.93,16 H23.07 Z" """ +
          """fill="none" stroke="#0038b8" stroke-width="1.5"/>""",
      ),
    )

  case Hindi extends Language("hi", LeftToRight, Flag.india)

  case Hungarian
    extends Language(
      "hu",
      LeftToRight,
      Flag.bands(
        vertical = false,
        "#ce2939",
        "#ffffff",
        "#477050",
      ),
    )

  case Indonesian
    extends Language(
      "id",
      LeftToRight,
      Flag.bands(vertical = false, "#ce1126", "#ffffff"),
    )

  case Italian
    extends Language(
      "it",
      LeftToRight,
      Flag.bands(
        vertical = true,
        "#009246",
        "#ffffff",
        "#ce2b37",
      ),
    )

  case Japanese
    extends Language(
      "ja",
      LeftToRight,
      Flag.of(
        """<rect width="60" height="40" fill="#ffffff"/>""" +
          """<circle cx="30" cy="20" r="12" fill="#bc002d"/>""",
      ),
    )

  case Korean
    extends Language(
      "ko",
      LeftToRight,
      Flag.of(
        """<rect width="60" height="40" fill="#ffffff"/>""" +
          """<circle cx="30" cy="20" r="10" fill="#0047a0"/>""" +
          """<path d="M20,20 A10,10 0 0 1 40,20 A5,5 0 0 1 30,20 """ +
          """A5,5 0 0 0 20,20 Z" fill="#cd2e3a"/>""" + List(
            (15.9, 10.6, -56.3),
            (44.1, 29.4, -56.3),
            (44.1, 10.6, 56.3),
            (15.9, 29.4, 56.3),
          ).map((x, y, angle) =>
              """<rect x="-4" y="-2.5" width="8" height="5" fill="#000000" """ +
                s"""transform="translate($x,$y) rotate($angle)"/>""",
            )
            .mkString,
      ),
    )

  case Latvian
    extends Language(
      "lv",
      LeftToRight,
      Flag.of(
        """<rect width="60" height="40" fill="#9e3039"/>""" +
          """<rect y="16" width="60" height="8" fill="#ffffff"/>""",
      ),
    )

  case Lithuanian
    extends Language(
      "lt",
      LeftToRight,
      Flag.bands(
        vertical = false,
        "#fdb913",
        "#006a44",
        "#c1272d",
      ),
    )

  case Malay
    extends Language(
      "ms",
      LeftToRight,
      Flag.of(
        """<rect width="60" height="40" fill="#ffffff"/>""" + (0 until 14 by 2)
          .map(stripe =>
            s"""<rect y="${ stripe * 40.0 / 14 }" width="60" """ +
              s"""height="${ 40.0 / 14 }" fill="#cc0001"/>""",
          )
          .mkString + """<rect width="30" height="22.86" fill="#010066"/>""" +
          """<circle cx="11" cy="11.43" r="7" fill="#ffcc00"/>""" +
          """<circle cx="13" cy="11.43" r="5.8" fill="#010066"/>""" + Flag.star(
            21.5,
            11.43,
            5,
            "#ffcc00",
            points = 14,
            inner = 0.45,
          ),
      ),
    )

  /** Bokmål, under its own code, claiming Norwegian's `no`. */
  case Norwegian
    extends Language(
      "nb",
      LeftToRight,
      Flag.nordicCross("#ba0c2f", "#ffffff", Some("#00205b")),
    )

  case Persian
    extends Language(
      "fa",
      RightToLeft,
      Flag.of(
        """<rect width="60" height="40" fill="#ffffff"/>""" +
          """<rect width="60" height="13.33" fill="#239f40"/>""" +
          """<rect y="26.67" width="60" height="13.33" fill="#da0000"/>""" +
          """<circle cx="30" cy="20" r="4" fill="none" stroke="#da0000" """ +
          """stroke-width="1.5"/>""",
      ),
    )

  case Polish
    extends Language(
      "pl",
      LeftToRight,
      Flag.bands(vertical = false, "#ffffff", "#dc143c"),
    )

  case Portuguese extends Language("pt", LeftToRight, Flag.Varieties)

  case BrazilianPortuguese
    extends Language(
      "pt-BR",
      LeftToRight,
      Flag.of(
        """<rect width="60" height="40" fill="#009c3b"/>""" +
          """<polygon points="6,20 30,4 54,20 30,36" fill="#ffdf00"/>""" +
          """<circle cx="30" cy="20" r="8.5" fill="#002776"/>""",
      ),
      parent = Some(Portuguese),
    )

  case EuropeanPortuguese
    extends Language(
      "pt-PT",
      LeftToRight,
      Flag.of(
        """<rect width="60" height="40" fill="#da291c"/>""" +
          """<rect width="24" height="40" fill="#046a38"/>""" +
          """<circle cx="24" cy="20" r="8" fill="none" stroke="#ffe900" """ +
          """stroke-width="2"/>""" +
          """<path d="M20,15 H28 V21 Q28,25 24,26 Q20,25 20,21 Z" """ +
          """fill="#ffffff" stroke="#da291c" stroke-width="1.5"/>""",
      ),
      parent = Some(Portuguese),
    )

  case Romanian
    extends Language(
      "ro",
      LeftToRight,
      Flag.bands(
        vertical = true,
        "#002b7f",
        "#fcd116",
        "#ce1126",
      ),
    )

  case Russian
    extends Language(
      "ru",
      LeftToRight,
      Flag.bands(
        vertical = false,
        "#ffffff",
        "#0039a6",
        "#d52b1e",
      ),
    )

  /** Serbian in Cyrillic, its official script. */
  case Serbian
    extends Language(
      "sr",
      LeftToRight,
      Flag.of(
        """<rect width="60" height="40" fill="#ffffff"/>""" +
          """<rect width="60" height="13.33" fill="#c6363c"/>""" +
          """<rect y="13.33" width="60" height="13.33" fill="#0c4076"/>""" +
          """<path d="M16,11 H28 V21 Q28,28 22,30 Q16,28 16,21 Z" """ +
          """fill="#c6363c" stroke="#ffffff" stroke-width="1"/>""" +
          """<path d="M22,13 V28 M17.5,19 H26.5" stroke="#ffffff" """ +
          """stroke-width="2"/>""",
      ),
    )

  case Slovak
    extends Language(
      "sk",
      LeftToRight,
      Flag.of(
        """<rect width="60" height="40" fill="#ee1c25"/>""" +
          """<rect width="60" height="26.67" fill="#0b4ea2"/>""" +
          """<rect width="60" height="13.33" fill="#ffffff"/>""" +
          """<path d="M13,8 H31 V22 Q31,31 22,34 Q13,31 13,22 Z" """ +
          """fill="#ee1c25" stroke="#ffffff" stroke-width="1.2"/>""" +
          """<path d="M14.5,29 Q22,24 29.5,29 Q26,32.5 22,33.5 Q18,32.5 14.5,29 Z" """ +
          """fill="#0b4ea2"/>""" +
          """<path d="M22,11 V27 M18,15 H26 M17,19.5 H27" stroke="#ffffff" """ +
          """stroke-width="1.8"/>""",
      ),
    )

  case Slovenian
    extends Language(
      "sl",
      LeftToRight,
      Flag.of(
        """<rect width="60" height="40" fill="#ed1c24"/>""" +
          """<rect width="60" height="26.67" fill="#005da4"/>""" +
          """<rect width="60" height="13.33" fill="#ffffff"/>""" +
          """<path d="M10,6 H24 V16 Q24,23 17,25 Q10,23 10,16 Z" """ +
          """fill="#005da4" stroke="#ed1c24" stroke-width="1.2"/>""" +
          """<polygon points="11,20 14.5,14 17,17.5 19.5,14 23,20" fill="#ffffff"/>""",
      ),
    )

  case Spanish extends Language("es", LeftToRight, Flag.Varieties)

  /** Four equal bands, so that the yellow is twice as tall. */
  case EuropeanSpanish
    extends Language(
      "es-ES",
      LeftToRight,
      Flag.bands(
        vertical = false,
        "#aa151b",
        "#f1bf00",
        "#f1bf00",
        "#aa151b",
      ),
      parent = Some(Spanish),
    )

  case LatinAmericanSpanish
    extends Language(
      "es-419",
      LeftToRight,
      Flag.of(
        """<rect width="60" height="40" fill="#ffffff"/>""" +
          """<rect width="20" height="40" fill="#006847"/>""" +
          """<rect x="40" width="20" height="40" fill="#ce1126"/>""" +
          """<circle cx="30" cy="20" r="5" fill="#8c5a2b"/>""",
      ),
      parent = Some(Spanish),
    )

  case Swahili
    extends Language(
      "sw",
      LeftToRight,
      Flag.of(
        """<rect width="60" height="40" fill="#00a3dd"/>""" +
          """<polygon points="0,0 60,0 0,40" fill="#1eb53a"/>""" +
          """<path d="M0,40 L60,0" stroke="#fcd116" stroke-width="14"/>""" +
          """<path d="M0,40 L60,0" stroke="#000000" stroke-width="9"/>""",
      ),
    )

  case Swedish
    extends Language(
      "sv",
      LeftToRight,
      Flag.nordicCross("#006aa7", "#fecc02"),
    )

  case Tamil extends Language("ta", LeftToRight, Flag.india)

  /** Six equal bands, so that the blue is twice as tall. */
  case Thai
    extends Language(
      "th",
      LeftToRight,
      Flag.bands(
        vertical = false,
        "#a51931",
        "#f4f5f8",
        "#2d2a4a",
        "#2d2a4a",
        "#f4f5f8",
        "#a51931",
      ),
    )

  case Turkish
    extends Language(
      "tr",
      LeftToRight,
      Flag.of(
        """<rect width="60" height="40" fill="#e30a17"/>""" +
          """<circle cx="22" cy="20" r="10" fill="#ffffff"/>""" +
          """<circle cx="24.5" cy="20" r="8" fill="#e30a17"/>""" +
          Flag.star(35, 20, 4.5, "#ffffff"),
      ),
    )

  case Ukrainian
    extends Language(
      "uk",
      LeftToRight,
      Flag.bands(vertical = false, "#0057b7", "#ffd700"),
    )

  case Urdu
    extends Language(
      "ur",
      RightToLeft,
      Flag.of(
        """<rect width="60" height="40" fill="#01411c"/>""" +
          """<rect width="15" height="40" fill="#ffffff"/>""" +
          """<circle cx="37.5" cy="21" r="10" fill="#ffffff"/>""" +
          """<circle cx="40.5" cy="18.5" r="8.5" fill="#01411c"/>""" +
          Flag.star(44, 15, 3.5, "#ffffff"),
      ),
    )

  case Vietnamese
    extends Language(
      "vi",
      LeftToRight,
      Flag.of(
        """<rect width="60" height="40" fill="#da251d"/>""" +
          Flag.star(30, 20, 12, "#ffff00"),
      ),
    )

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
