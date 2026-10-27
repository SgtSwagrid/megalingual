package com.alecdorrington.megalingual

/**
  * A flag as SVG markup, drawn in a 60 by 40 view box.
  *
  * @param markup
  *   The SVG document, from `<svg` to `</svg>`.
  */
final case class Flag(markup: String):

  /** The flag as a `data:` URI, for an image's `src`. */
  def dataUri: String = "data:image/svg+xml;utf8," + Flag.encoded(markup)

  private def shapes: String = markup
    .stripPrefix(Flag.opening)
    .stripSuffix("</svg>")

object Flag:

  private val opening =
    """<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 60 40">"""

  /**
    * A stand-in for the flag of a language whose varieties are spoken in
    * different countries, as no one country's flag stands for it.
    */
  private[megalingual] case object Varieties

  /** Makes a flag of shapes drawn back to front in a 60 by 40 view box. */
  private[megalingual] def of(shapes: String): Flag =
    Flag(s"$opening$shapes</svg>")

  /** Makes a flag of equal bands, top to bottom, or across if `vertical`. */
  private[megalingual] def bands(vertical: Boolean, colours: String*): Flag =
    val count = colours.size.max(1)
    of(colours.zipWithIndex.map(band(vertical, count)).mkString)

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
  private[megalingual] def nordicCross
    (
      field: String,
      cross: String,
      inner: Option[String] = None,
    )
    : Flag =
    val crosses = inner match
      case Some(colour) => arms(cross, 10) + arms(colour, 5)
      case None         => arms(cross, 8)
    of(s"""<rect width="60" height="40" fill="$field"/>$crosses""")

  private def arms(colour: String, width: Int): String =
    s"""<path d="M22,0 V40 M0,20 H60" stroke="$colour" """ +
      s"""stroke-width="$width"/>"""

  /** Draws a star, one point up, its inner corners at `inner` of its radius. */
  private[megalingual] def star
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
  private[megalingual] def canton(flag: Flag): String =
    s"""<svg width="30" height="20" viewBox="0 0 60 40">${ flag
        .shapes }</svg>"""

  /**
    * Makes a tile of a short text, such as a language's code, in the device's
    * own typeface: two capitals fit.
    */
  private[megalingual] def tile(text: String): Flag = of(
    """<rect width="60" height="40" fill="#e4e7ec"/>""" +
      """<text x="30" y="28" text-anchor="middle" """ +
      """font-family="system-ui,sans-serif" font-size="22" """ +
      s"""font-weight="700" fill="#344054">$text</text>""",
  )

  private[megalingual] val india: Flag = of(
    """<rect width="60" height="40" fill="#ffffff"/>""" +
      """<rect width="60" height="13.33" fill="#ff9933"/>""" +
      """<rect y="26.67" width="60" height="13.33" fill="#138808"/>""" +
      """<circle cx="30" cy="20" r="5.5" fill="none" stroke="#000080" """ +
      """stroke-width="1"/>""" +
      """<path d="M24.5,20 H35.5 M30,14.5 V25.5 M26.1,16.1 L33.9,23.9 """ +
      """M26.1,23.9 L33.9,16.1" stroke="#000080" stroke-width="0.6"/>""",
  )

  private[megalingual] val unionFlag: Flag = of(
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
