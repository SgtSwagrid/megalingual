package com.alecdorrington.megalingual
package languages

import com.alecdorrington.megalingual.Direction.RightToLeft

object Urdu extends Language.Root("ur", RightToLeft):

  override lazy val flag: Flag = Flag.drawn(
    """<rect width="60" height="40" fill="#01411c"/>""" +
      """<rect width="15" height="40" fill="#ffffff"/>""" +
      """<circle cx="37.5" cy="21" r="10" fill="#ffffff"/>""" +
      """<circle cx="40.5" cy="18.5" r="8.5" fill="#01411c"/>""" +
      Flag.star(44, 15, 3.5, "#ffffff"),
  )

  override def plurals: Plural.Rule = Plural.oneOrOther

  override def names: PartialFunction[Language, String] =
    case Arabic               => "عربی"
    case Bengali              => "بنگالی"
    case Bulgarian            => "بلغاری"
    case Catalan              => "کیٹالان"
    case Chinese              => "چینی"
    case SimplifiedChinese    => "چینی (آسان کردہ)"
    case TraditionalChinese   => "روایتی چینی"
    case TaiwaneseChinese     => "روایتی چینی (تائیوان)"
    case Cantonese            => "کینٹونیز"
    case Croatian             => "کروشیائی"
    case Czech                => "چیک"
    case Danish               => "ڈینش"
    case Dutch                => "ڈچ"
    case English              => "انگریزی"
    case BritishEnglish       => "برطانوی انگریزی"
    case AmericanEnglish      => "امریکی انگریزی"
    case AustralianEnglish    => "آسٹریلیائی انگریزی"
    case Estonian             => "اسٹونین"
    case Filipino             => "فلیپینو"
    case Finnish              => "فینیش"
    case French               => "فرانسیسی"
    case German               => "جرمن"
    case GermanGerman         => "جرمن (جرمنی)"
    case GermanOfSwitzerland  => "جرمن (سوئٹزر لینڈ)"
    case SwissStandardGerman  => "سوئس ہائی جرمن"
    case SwissGerman          => "سوئس جرمن"
    case Greek                => "یونانی"
    case Hebrew               => "عبرانی"
    case Hindi                => "ہندی"
    case Hungarian            => "ہنگیرین"
    case Indonesian           => "انڈونیشیائی"
    case Italian              => "اطالوی"
    case Japanese             => "جاپانی"
    case Korean               => "کوریائی"
    case Latvian              => "لیٹوین"
    case Lithuanian           => "لیتھوینین"
    case Malay                => "مالے"
    case Norwegian            => "نارویجین"
    case Persian              => "فارسی"
    case Polish               => "پولش"
    case Portuguese           => "پرتگالی"
    case BrazilianPortuguese  => "برازیلی پرتگالی"
    case EuropeanPortuguese   => "یورپی پرتگالی"
    case Romanian             => "رومینین"
    case Russian              => "روسی"
    case Serbian              => "سربین"
    case Slovak               => "سلوواک"
    case Slovenian            => "سلووینیائی"
    case Spanish              => "ہسپانوی"
    case EuropeanSpanish      => "یورپی ہسپانوی"
    case LatinAmericanSpanish => "لاطینی امریکی ہسپانوی"
    case Swahili              => "سواحلی"
    case Swedish              => "سویڈش"
    case Tamil                => "تمل"
    case Thai                 => "تھائی"
    case Turkish              => "ترکی"
    case Ukrainian            => "یوکرینیائی"
    case Urdu                 => "اردو"
    case Vietnamese           => "ویتنامی"
