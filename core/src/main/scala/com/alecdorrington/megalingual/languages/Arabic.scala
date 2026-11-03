package com.alecdorrington.megalingual
package languages

import com.alecdorrington.megalingual.Direction.RightToLeft
import com.alecdorrington.megalingual.Plural.{Few, Many, One, Other, Two, Zero}

/** Modern Standard Arabic. */
object Arabic extends Language.Root("ar", RightToLeft):

  override lazy val flag: Flag = Flag.drawn(
    """<rect width="60" height="40" fill="#006c35"/>""" +
      """<path d="M16,17 Q20,12 24,16 T32,16 T40,15 T44,14" fill="none" """ +
      """stroke="#ffffff" stroke-width="2"/>""" +
      """<path d="M16,27 H44 M40,25 V29" stroke="#ffffff" """ +
      """stroke-width="1.6"/>""",
  )

  override def plurals: Plural.Rule = n =>
    if n == 0 then Zero
    else if n == 1 then One
    else if n == 2 then Two
    else if n % 100 >= 3 && n % 100 <= 10 then Few
    else if n % 100 >= 11 then Many
    else Other

  override def names: PartialFunction[Language, String] =
    case Arabic               => "العربية"
    case Bengali              => "البنغالية"
    case Bulgarian            => "البلغارية"
    case Catalan              => "الكتالانية"
    case Chinese              => "الصينية"
    case SimplifiedChinese    => "الصينية المبسطة"
    case TraditionalChinese   => "الصينية التقليدية"
    case TaiwaneseChinese     => "الصينية التقليدية (تايوان)"
    case Cantonese            => "الكانتونية"
    case Croatian             => "الكرواتية"
    case Czech                => "التشيكية"
    case Danish               => "الدنماركية"
    case Dutch                => "الهولندية"
    case English              => "الإنجليزية"
    case BritishEnglish       => "الإنجليزية البريطانية"
    case AmericanEnglish      => "الإنجليزية الأمريكية"
    case AustralianEnglish    => "الإنجليزية الأسترالية"
    case Estonian             => "الإستونية"
    case Filipino             => "الفلبينية"
    case Finnish              => "الفنلندية"
    case French               => "الفرنسية"
    case German               => "الألمانية"
    case GermanGerman         => "الألمانية (ألمانيا)"
    case GermanOfSwitzerland  => "الألمانية (سويسرا)"
    case SwissStandardGerman  => "الألمانية العليا السويسرية"
    case SwissGerman          => "الألمانية السويسرية"
    case Greek                => "اليونانية"
    case Hebrew               => "العبرية"
    case Hindi                => "الهندية"
    case Hungarian            => "الهنغارية"
    case Indonesian           => "الإندونيسية"
    case Italian              => "الإيطالية"
    case Japanese             => "اليابانية"
    case Korean               => "الكورية"
    case Latvian              => "اللاتفية"
    case Lithuanian           => "الليتوانية"
    case Malay                => "الماليزية"
    case Norwegian            => "النرويجية"
    case Persian              => "الفارسية"
    case Polish               => "البولندية"
    case Portuguese           => "البرتغالية"
    case BrazilianPortuguese  => "البرتغالية البرازيلية"
    case EuropeanPortuguese   => "البرتغالية الأوروبية"
    case Romanian             => "الرومانية"
    case Russian              => "الروسية"
    case Serbian              => "الصربية"
    case Slovak               => "السلوفاكية"
    case Slovenian            => "السلوفينية"
    case Spanish              => "الإسبانية"
    case EuropeanSpanish      => "الإسبانية الأوروبية"
    case LatinAmericanSpanish => "الإسبانية الأمريكية اللاتينية"
    case Swahili              => "السواحلية"
    case Swedish              => "السويدية"
    case Tamil                => "التاميلية"
    case Thai                 => "التايلاندية"
    case Turkish              => "التركية"
    case Ukrainian            => "الأوكرانية"
    case Urdu                 => "الأوردية"
    case Vietnamese           => "الفيتنامية"
