package com.alecdorrington.megalingual
package languages

/**
  * Cantonese in traditional characters. Having no two-letter code, it is
  * `zh-HK`, the tag its readers' browsers send.
  */
object Cantonese extends Language.Variety("zh-HK", of = TraditionalChinese):

  override def claims: List[String] = List(
    "zh-MO",
    "zh-Hant-HK",
    "zh-Hant-MO",
    "yue",
  )

  override lazy val flag: Flag = Flag.drawn(
    """<rect width="60" height="40" fill="#de2910"/>""" + (0 until 5)
      .map(petal =>
        """<ellipse cx="30" cy="13.5" rx="3.5" ry="6.5" fill="#ffffff" """ +
          s"""transform="rotate(${ petal * 72 } 30 20)"/>""",
      )
      .mkString,
  )

  /**
    * Every language's name in traditional Chinese as written in Hong Kong,
    * where it differs from Taiwan's.
    */
  override def names: PartialFunction[Language, String] =
    case Catalan   => "加泰隆尼亞文"
    case Croatian  => "克羅地亞文"
    case Italian   => "意大利文"
    case Slovenian => "斯洛文尼亞文"
    case Tamil     => "泰米爾文"
    case Urdu      => "烏爾都文"
