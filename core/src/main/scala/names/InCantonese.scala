package com.alecdorrington.megalingual
package names

import com.alecdorrington.megalingual.Language.*

/**
  * Every language's name in traditional Chinese as written in Hong Kong, where
  * it differs from Taiwan's.
  */
private[megalingual] object InCantonese:

  def apply(language: Language): String = language match
    case Catalan   => "加泰隆尼亞文"
    case Croatian  => "克羅地亞文"
    case Italian   => "意大利文"
    case Slovenian => "斯洛文尼亞文"
    case Tamil     => "泰米爾文"
    case Urdu      => "烏爾都文"
    case _         => InTraditionalChinese(language)
