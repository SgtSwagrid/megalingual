package com.alecdorrington.megalingual
package languages

/**
  * Mandarin in traditional characters, as in Taiwan. As traditional Chinese's
  * first variety, it is what tags naming no other region find where a variety
  * is needed.
  */
object TaiwaneseChinese
  extends Language.Variety("zh-TW", of = TraditionalChinese):

  override def claims: List[String] = List("zh-Hant-TW")

  override lazy val flag: Flag = Flag.drawn(
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
  )
