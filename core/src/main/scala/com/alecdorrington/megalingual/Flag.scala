package com.alecdorrington.megalingual

import com.alecdorrington.megalingual.Language.*

/**
  * A flag as SVG markup, drawn in a 60 by 40 view box.
  *
  * @param markup
  *   The SVG document, from
  *   `<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 60 40">` to
  *   `</svg>`.
  */
final case class Flag(markup: String):

  /** The flag as a `data:` URI, for an image's `src`. */
  def dataUri: String = "data:image/svg+xml;utf8," + Flag.encoded(markup)

  private def shapes: String = markup
    .stripPrefix(Flag.opening)
    .stripSuffix("</svg>")

object Flag:

  /**
    * The flag of a language, drawn by hand. A language spoken in one country
    * takes its flag; one whose varieties are spoken in different countries,
    * which no one country's flag stands for, a tile of its code. A flag is
    * drawn only when it is first shown, so code that shows none links none.
    */
  private[megalingual] def of(language: Language): Flag = language match
    case Chinese | English | German | Portuguese | Spanish =>
      tile(language.code.map(_.toUpper))
    case Arabic => drawn(
        """<rect width="60" height="40" fill="#006c35"/>""" +
          """<path d="M16,17 Q20,12 24,16 T32,16 T40,15 T44,14" fill="none" """ +
          """stroke="#ffffff" stroke-width="2"/>""" +
          """<path d="M16,27 H44 M40,25 V29" stroke="#ffffff" """ +
          """stroke-width="1.6"/>""",
      )
    case Bengali => drawn(
        """<rect width="60" height="40" fill="#006a4e"/>""" +
          """<circle cx="27" cy="20" r="12" fill="#f42a41"/>""",
      )
    case Bulgarian => bands(
        vertical = false,
        "#ffffff",
        "#00966e",
        "#d62612",
      )
    case Catalan => bands(
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
      )
    case SimplifiedChinese => drawn(
        """<rect width="60" height="40" fill="#ee1c25"/>""" +
          star(10, 10, 6, "#ffff00") + star(20, 4, 2, "#ffff00") +
          star(24, 8, 2, "#ffff00") + star(24, 14, 2, "#ffff00") +
          star(20, 18, 2, "#ffff00"),
      )
    case TraditionalChinese => drawn(
        """<rect width="60" height="40" fill="#fe0000"/>""" +
          """<rect width="30" height="20" fill="#000095"/>""" + star(
            15,
            10,
            7.5,
            "#ffffff",
            points = 12,
            inner = 0.6,
          ) + """<circle cx="15" cy="10" r="4.2" fill="#000095"/>""" +
          """<circle cx="15" cy="10" r="3.6" fill="#ffffff"/>""",
      )
    case Cantonese => drawn(
        """<rect width="60" height="40" fill="#de2910"/>""" + (0 until 5)
          .map(petal =>
            """<ellipse cx="30" cy="13.5" rx="3.5" ry="6.5" fill="#ffffff" """ +
              s"""transform="rotate(${ petal * 72 } 30 20)"/>""",
          )
          .mkString,
      )
    case Croatian => drawn(
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
      )
    case Czech => drawn(
        """<rect width="60" height="20" fill="#ffffff"/>""" +
          """<rect y="20" width="60" height="20" fill="#d7141a"/>""" +
          """<polygon points="0,0 30,20 0,40" fill="#11457e"/>""",
      )
    case Danish => nordicCross("#c8102e", "#ffffff")
    case Dutch  => bands(
        vertical = false,
        "#ae1c28",
        "#ffffff",
        "#21468b",
      )
    case BritishEnglish  => unionFlag
    case AmericanEnglish =>
      drawn(
        """<rect width="60" height="40" fill="#b31942"/>""" +
          """<path d="M0,4.62 H60 M0,10.77 H60 M0,16.92 H60 M0,23.08 H60 """ +
          """M0,29.23 H60 M0,35.38 H60" stroke="#ffffff" stroke-width="3.08"/>""" +
          """<rect width="24" height="21.54" fill="#0a3161"/>""",
      )
      // Focused on the Commonwealth Star, which no other flag has.
    case AustralianEnglish => drawn(
        """<rect width="60" height="40" fill="#012169"/>""" +
          canton(unionFlag) + List(
            (15.0, 30.0, 6.0, 7),   // the Commonwealth Star
            (45.0, 33.33, 3.0, 7),  // Alpha Crucis
            (37.5, 17.5, 3.0, 7),   // Beta Crucis
            (45.0, 6.67, 3.0, 7),   // Gamma Crucis
            (51.67, 14.83, 3.0, 7), // Delta Crucis
            (48.0, 21.67, 1.8, 5),  // Epsilon Crucis
          ).map((x, y, radius, points) =>
              star(
                x,
                y,
                radius,
                "#ffffff",
                points,
                inner = 4.0 / 9,
              ),
            )
            .mkString,
      )
    case Estonian => bands(
        vertical = false,
        "#0072ce",
        "#000000",
        "#ffffff",
      )
    case Filipino => drawn(
        """<rect width="60" height="20" fill="#0038a8"/>""" +
          """<rect y="20" width="60" height="20" fill="#ce1126"/>""" +
          """<polygon points="0,0 34.64,20 0,40" fill="#ffffff"/>""" +
          """<circle cx="11.5" cy="20" r="4" fill="#fcd116"/>""" +
          star(4, 5, 2, "#fcd116") + star(4, 35, 2, "#fcd116") +
          star(28, 20, 2, "#fcd116"),
      )
    case Finnish => nordicCross("#ffffff", "#002f6c")
    case French  => bands(
        vertical = true,
        "#0055a4",
        "#ffffff",
        "#ef4135",
      )
    case GermanGerman => bands(
        vertical = false,
        "#000000",
        "#dd0000",
        "#ffce00",
      )
    case SwissStandardGerman => switzerland
    case SwissGerman         => switzerland
    case Greek               => drawn(
        """<rect width="60" height="40" fill="#0d5eaf"/>""" +
          """<path d="M0,6.67 H60 M0,15.56 H60 M0,24.44 H60 M0,33.33 H60" """ +
          """stroke="#ffffff" stroke-width="4.44"/>""" +
          """<rect width="22.22" height="22.22" fill="#0d5eaf"/>""" +
          """<path d="M11.11,0 V22.22 M0,11.11 H22.22" stroke="#ffffff" """ +
          """stroke-width="4.44"/>""",
      )
    case Hebrew => drawn(
        """<rect width="60" height="40" fill="#ffffff"/>""" +
          """<path d="M0,6.5 H60 M0,33.5 H60" stroke="#0038b8" """ +
          """stroke-width="5"/>""" +
          """<path d="M30,12 L36.93,24 H23.07 Z M30,28 L36.93,16 H23.07 Z" """ +
          """fill="none" stroke="#0038b8" stroke-width="1.5"/>""",
      )
    case Hindi     => india
    case Hungarian => bands(
        vertical = false,
        "#ce2939",
        "#ffffff",
        "#477050",
      )
    case Indonesian => bands(vertical = false, "#ce1126", "#ffffff")
    case Italian    => bands(
        vertical = true,
        "#009246",
        "#ffffff",
        "#ce2b37",
      )
    case Japanese => drawn(
        """<rect width="60" height="40" fill="#ffffff"/>""" +
          """<circle cx="30" cy="20" r="12" fill="#bc002d"/>""",
      )
    case Korean => drawn(
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
      )
    case Latvian => drawn(
        """<rect width="60" height="40" fill="#9e3039"/>""" +
          """<rect y="16" width="60" height="8" fill="#ffffff"/>""",
      )
    case Lithuanian => bands(
        vertical = false,
        "#fdb913",
        "#006a44",
        "#c1272d",
      )
    case Malay => drawn(
        """<rect width="60" height="40" fill="#ffffff"/>""" + (0 until 14 by 2)
          .map(stripe =>
            s"""<rect y="${ stripe * 40.0 / 14 }" width="60" """ +
              s"""height="${ 40.0 / 14 }" fill="#cc0001"/>""",
          )
          .mkString + """<rect width="30" height="22.86" fill="#010066"/>""" +
          """<circle cx="11" cy="11.43" r="7" fill="#ffcc00"/>""" +
          """<circle cx="13" cy="11.43" r="5.8" fill="#010066"/>""" + star(
            21.5,
            11.43,
            5,
            "#ffcc00",
            points = 14,
            inner = 0.45,
          ),
      )
    case Norwegian => nordicCross("#ba0c2f", "#ffffff", Some("#00205b"))
    case Persian   => drawn(
        """<rect width="60" height="40" fill="#ffffff"/>""" +
          """<rect width="60" height="13.33" fill="#239f40"/>""" +
          """<rect y="26.67" width="60" height="13.33" fill="#da0000"/>""" +
          """<circle cx="30" cy="20" r="4" fill="none" stroke="#da0000" """ +
          """stroke-width="1.5"/>""",
      )
    case Polish              => bands(vertical = false, "#ffffff", "#dc143c")
    case BrazilianPortuguese => drawn(
        """<rect width="60" height="40" fill="#009c3b"/>""" +
          """<polygon points="6,20 30,4 54,20 30,36" fill="#ffdf00"/>""" +
          """<circle cx="30" cy="20" r="8.5" fill="#002776"/>""",
      )
    case EuropeanPortuguese => drawn(
        """<rect width="60" height="40" fill="#da291c"/>""" +
          """<rect width="24" height="40" fill="#046a38"/>""" +
          """<circle cx="24" cy="20" r="8" fill="none" stroke="#ffe900" """ +
          """stroke-width="2"/>""" +
          """<path d="M20,15 H28 V21 Q28,25 24,26 Q20,25 20,21 Z" """ +
          """fill="#ffffff" stroke="#da291c" stroke-width="1.5"/>""",
      )
    case Romanian => bands(
        vertical = true,
        "#002b7f",
        "#fcd116",
        "#ce1126",
      )
    case Russian => bands(
        vertical = false,
        "#ffffff",
        "#0039a6",
        "#d52b1e",
      )
    case Serbian => drawn(
        """<rect width="60" height="40" fill="#ffffff"/>""" +
          """<rect width="60" height="13.33" fill="#c6363c"/>""" +
          """<rect y="13.33" width="60" height="13.33" fill="#0c4076"/>""" +
          """<path d="M16,11 H28 V21 Q28,28 22,30 Q16,28 16,21 Z" """ +
          """fill="#c6363c" stroke="#ffffff" stroke-width="1"/>""" +
          """<path d="M22,13 V28 M17.5,19 H26.5" stroke="#ffffff" """ +
          """stroke-width="2"/>""",
      )
    case Slovak => drawn(
        """<rect width="60" height="40" fill="#ee1c25"/>""" +
          """<rect width="60" height="26.67" fill="#0b4ea2"/>""" +
          """<rect width="60" height="13.33" fill="#ffffff"/>""" +
          """<path d="M13,8 H31 V22 Q31,31 22,34 Q13,31 13,22 Z" """ +
          """fill="#ee1c25" stroke="#ffffff" stroke-width="1.2"/>""" +
          """<path d="M14.5,29 Q22,24 29.5,29 Q26,32.5 22,33.5 Q18,32.5 14.5,29 Z" """ +
          """fill="#0b4ea2"/>""" +
          """<path d="M22,11 V27 M18,15 H26 M17,19.5 H27" stroke="#ffffff" """ +
          """stroke-width="1.8"/>""",
      )
    case Slovenian =>
      drawn(
        """<rect width="60" height="40" fill="#ed1c24"/>""" +
          """<rect width="60" height="26.67" fill="#005da4"/>""" +
          """<rect width="60" height="13.33" fill="#ffffff"/>""" +
          """<path d="M10,6 H24 V16 Q24,23 17,25 Q10,23 10,16 Z" """ +
          """fill="#005da4" stroke="#ed1c24" stroke-width="1.2"/>""" +
          """<polygon points="11,20 14.5,14 17,17.5 19.5,14 23,20" fill="#ffffff"/>""",
      )
      // Four equal bands, so that the yellow is twice as tall.
    case EuropeanSpanish => bands(
        vertical = false,
        "#aa151b",
        "#f1bf00",
        "#f1bf00",
        "#aa151b",
      )
    case LatinAmericanSpanish => drawn(
        """<rect width="60" height="40" fill="#ffffff"/>""" +
          """<rect width="20" height="40" fill="#006847"/>""" +
          """<rect x="40" width="20" height="40" fill="#ce1126"/>""" +
          """<circle cx="30" cy="20" r="5" fill="#8c5a2b"/>""",
      )
    case Swahili => drawn(
        """<rect width="60" height="40" fill="#00a3dd"/>""" +
          """<polygon points="0,0 60,0 0,40" fill="#1eb53a"/>""" +
          """<path d="M0,40 L60,0" stroke="#fcd116" stroke-width="14"/>""" +
          """<path d="M0,40 L60,0" stroke="#000000" stroke-width="9"/>""",
      )
    case Swedish => nordicCross("#006aa7", "#fecc02")
    case Tamil   =>
      india
      // Six equal bands, so that the blue is twice as tall.
    case Thai => bands(
        vertical = false,
        "#a51931",
        "#f4f5f8",
        "#2d2a4a",
        "#2d2a4a",
        "#f4f5f8",
        "#a51931",
      )
    case Turkish => drawn(
        """<rect width="60" height="40" fill="#e30a17"/>""" +
          """<circle cx="22" cy="20" r="10" fill="#ffffff"/>""" +
          """<circle cx="24.5" cy="20" r="8" fill="#e30a17"/>""" +
          star(35, 20, 4.5, "#ffffff"),
      )
    case Ukrainian => bands(vertical = false, "#0057b7", "#ffd700")
    case Urdu      => drawn(
        """<rect width="60" height="40" fill="#01411c"/>""" +
          """<rect width="15" height="40" fill="#ffffff"/>""" +
          """<circle cx="37.5" cy="21" r="10" fill="#ffffff"/>""" +
          """<circle cx="40.5" cy="18.5" r="8.5" fill="#01411c"/>""" +
          star(44, 15, 3.5, "#ffffff"),
      )
    case Vietnamese => drawn(
        """<rect width="60" height="40" fill="#da251d"/>""" +
          star(30, 20, 12, "#ffff00"),
      )

  private val opening =
    """<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 60 40">"""

  /** Makes a flag of shapes drawn back to front in a 60 by 40 view box. */
  private def drawn(shapes: String): Flag = Flag(s"$opening$shapes</svg>")

  /** Makes a flag of equal bands, top to bottom, or across if `vertical`. */
  private def bands(vertical: Boolean, colours: String*): Flag =
    val count = colours.size.max(1)
    drawn(colours.zipWithIndex.map(band(vertical, count)).mkString)

  private def band(vertical: Boolean, count: Int)(colour: String, index: Int) =
    if vertical then
      val width = 60.0 / count
      s"""<rect x="${ index * width }" y="0" width="$width" """ +
        s"""height="40" fill="$colour"/>"""
    else
      val height = 40.0 / count
      s"""<rect x="0" y="${ index * height }" width="60" """ +
        s"""height="$height" fill="$colour"/>"""

  /** Makes a Nordic cross on a field, with an optional narrower inner cross. */
  private def nordicCross
    (
      field: String,
      cross: String,
      inner: Option[String] = None,
    )
    : Flag =
    val crosses = inner match
      case Some(colour) => arms(cross, 10) + arms(colour, 5)
      case None         => arms(cross, 8)
    drawn(s"""<rect width="60" height="40" fill="$field"/>$crosses""")

  private def arms(colour: String, width: Int): String =
    s"""<path d="M22,0 V40 M0,20 H60" stroke="$colour" """ +
      s"""stroke-width="$width"/>"""

  /** Draws a star, one point up, its inner corners at `inner` of its radius. */
  private def star
    (
      x: Double,
      y: Double,
      radius: Double,
      fill: String,
      points: Int = 5,
      inner: Double = 0.382,
    )
    : String =
    val corners = (0 until points * 2).map: index =>
      val reach = if index % 2 == 0 then radius else radius * inner
      val angle = math.Pi * (index * 180.0 / points - 90) / 180
      s"${ rounded(x + reach * math.cos(angle)) }," +
        s"${ rounded(y + reach * math.sin(angle)) }"
    s"""<polygon points="${ corners.mkString(" ") }" fill="$fill"/>"""

  private def rounded(value: Double): Double = math.round(value * 100) / 100.0

  /**
    * Draws a flag shrunk into the canton, inside an `<svg>` of its own so that
    * nothing drawn to its edge, such as a saltire's ends, spills over.
    */
  private def canton(flag: Flag): String =
    s"""<svg width="30" height="20" viewBox="0 0 60 40">${ flag
        .shapes }</svg>"""

  /**
    * Makes a tile of a short text, such as a language's code, in the device's
    * own typeface: two capitals fit.
    */
  private def tile(text: String): Flag = drawn(
    """<rect width="60" height="40" fill="#e4e7ec"/>""" +
      """<text x="30" y="28" text-anchor="middle" """ +
      """font-family="system-ui,sans-serif" font-size="22" """ +
      s"""font-weight="700" fill="#344054">$text</text>""",
  )

  private lazy val india: Flag = drawn(
    """<rect width="60" height="40" fill="#ffffff"/>""" +
      """<rect width="60" height="13.33" fill="#ff9933"/>""" +
      """<rect y="26.67" width="60" height="13.33" fill="#138808"/>""" +
      """<circle cx="30" cy="20" r="5.5" fill="none" stroke="#000080" """ +
      """stroke-width="1"/>""" +
      """<path d="M24.5,20 H35.5 M30,14.5 V25.5 M26.1,16.1 L33.9,23.9 """ +
      """M26.1,23.9 L33.9,16.1" stroke="#000080" stroke-width="0.6"/>""",
  )

  private lazy val switzerland: Flag = drawn(
    """<rect width="60" height="40" fill="#da291c"/>""" +
      """<path d="M30,8 V32 M18,20 H42" stroke="#ffffff" stroke-width="8"/>""",
  )

  private lazy val unionFlag: Flag = drawn(
    """<rect width="60" height="40" fill="#012169"/>""" +
      """<path d="M0,0 L60,40 M60,0 L0,40" stroke="#ffffff" stroke-width="8"/>""" +
      """<path d="M0,0 L60,40 M60,0 L0,40" stroke="#c8102e" stroke-width="3"/>""" +
      """<path d="M30,0 V40 M0,20 H60" stroke="#ffffff" stroke-width="12"/>""" +
      """<path d="M30,0 V40 M0,20 H60" stroke="#c8102e" stroke-width="7"/>""",
  )

  private def encoded(markup: String): String = markup
    .replace('"', '\'')
    .replace("\n", " ")
    .replace("#", "%23")
    .replace("<", "%3C")
    .replace(">", "%3E")
