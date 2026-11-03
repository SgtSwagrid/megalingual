package com.alecdorrington.megalingual

/**
  * A flag as SVG markup, drawn in a 60 by 40 view box. Each language draws its
  * own ([[Language.flag]]) with the shapes here.
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

  private val opening =
    """<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 60 40">"""

  /**
    * Makes a flag of shapes drawn back to front in a 60 by 40 view box.
    *
    * @param shapes
    *   The SVG elements, such as `<rect>`s, `<path>`s and [[star]]s.
    *
    * @return
    *   A flag of the shapes.
    */
  def drawn(shapes: String): Flag = Flag(s"$opening$shapes</svg>")

  /**
    * Makes a flag of equal bands.
    *
    * @param vertical
    *   Whether the bands run up and down, left to right, rather than across,
    *   top to bottom.
    *
    * @param colours
    *   The bands' colours, in order.
    *
    * @return
    *   A flag of the bands.
    */
  def bands(vertical: Boolean, colours: String*): Flag =
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

  /**
    * Makes a Nordic cross on a field.
    *
    * @param field
    *   The field's colour.
    *
    * @param cross
    *   The cross's colour.
    *
    * @param inner
    *   The colour of a narrower cross within it, if any.
    *
    * @return
    *   A flag of the cross.
    */
  def nordicCross
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

  /**
    * Draws a star, one point up.
    *
    * @param x
    *   Its centre's distance from the left.
    *
    * @param y
    *   Its centre's distance from the top.
    *
    * @param radius
    *   The distance from its centre to its points.
    *
    * @param fill
    *   Its colour.
    *
    * @param points
    *   The number of its points.
    *
    * @param inner
    *   The distance from its centre to its inner corners, as a share of its
    *   radius.
    *
    * @return
    *   The star's SVG `<polygon>`.
    */
  def star
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
    *
    * @param flag
    *   The flag in the canton.
    *
    * @return
    *   The canton's SVG `<svg>`.
    */
  def canton(flag: Flag): String =
    s"""<svg width="30" height="20" viewBox="0 0 60 40">${ flag
        .shapes }</svg>"""

  /**
    * Makes a tile of a short text in capitals, such as a language's code, in
    * the device's own typeface: two capitals fit at full size, and more are
    * shrunk to fit. A language with varieties takes a tile of its code, telling
    * it apart from them.
    *
    * @param text
    *   The text.
    *
    * @return
    *   A flag of the tile.
    */
  def tile(text: String): Flag =
    val capitals = text.map(_.toUpper)
    val size     = rounded(math.min(22, 54 / (0.66 * capitals.length)))
    drawn(
      """<rect width="60" height="40" fill="#e4e7ec"/>""" +
        s"""<text x="30" y="${ rounded(20 + 0.36 * size) }" """ +
        """text-anchor="middle" font-family="system-ui,sans-serif" """ +
        s"""font-size="$size" font-weight="700" fill="#344054">$capitals</text>""",
    )

  private def encoded(markup: String): String = markup
    .replace('"', '\'')
    .replace("\n", " ")
    .replace("#", "%23")
    .replace("<", "%3C")
    .replace(">", "%3E")
