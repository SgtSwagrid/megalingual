package com.alecdorrington.megalingual
package languages

import com.alecdorrington.megalingual.Direction.RightToLeft

object Persian extends Language.Root("fa", RightToLeft):

  override lazy val flag: Flag = Flag.drawn(
    """<rect width="60" height="40" fill="#ffffff"/>""" +
      """<rect width="60" height="13.33" fill="#239f40"/>""" +
      """<rect y="26.67" width="60" height="13.33" fill="#da0000"/>""" +
      """<circle cx="30" cy="20" r="4" fill="none" stroke="#da0000" """ +
      """stroke-width="1.5"/>""",
  )

  override def plurals: Plural.Rule = Plural.zeroOrOne

  override def names: PartialFunction[Language, String] =
    case Arabic               => "عربی"
    case Bengali              => "بنگالی"
    case Bulgarian            => "بلغاری"
    case Catalan              => "کاتالان"
    case Chinese              => "چینی"
    case SimplifiedChinese    => "چینی ساده‌شده"
    case TraditionalChinese   => "چینی سنتی"
    case TaiwaneseChinese     => "چینی سنتی (تایوان)"
    case Cantonese            => "کانتونی"
    case Croatian             => "کرواتی"
    case Czech                => "چکی"
    case Danish               => "دانمارکی"
    case Dutch                => "هلندی"
    case English              => "انگلیسی"
    case BritishEnglish       => "انگلیسی بریتانیا"
    case AmericanEnglish      => "انگلیسی آمریکا"
    case AustralianEnglish    => "انگلیسی استرالیا"
    case Estonian             => "استونیایی"
    case Filipino             => "فیلیپینی"
    case Finnish              => "فنلاندی"
    case French               => "فرانسوی"
    case German               => "آلمانی"
    case GermanGerman         => "آلمانی (آلمان)"
    case GermanOfSwitzerland  => "آلمانی (سوئیس)"
    case SwissStandardGerman  => "آلمانی معیار سوئیس"
    case SwissGerman          => "آلمانی سوئیسی"
    case Greek                => "یونانی"
    case Hebrew               => "عبری"
    case Hindi                => "هندی"
    case Hungarian            => "مجاری"
    case Indonesian           => "اندونزیایی"
    case Italian              => "ایتالیایی"
    case Japanese             => "ژاپنی"
    case Korean               => "کره‌ای"
    case Latvian              => "لتونیایی"
    case Lithuanian           => "لیتوانیایی"
    case Malay                => "مالایی"
    case Norwegian            => "نروژی"
    case Persian              => "فارسی"
    case Polish               => "لهستانی"
    case Portuguese           => "پرتغالی"
    case BrazilianPortuguese  => "پرتغالی برزیل"
    case EuropeanPortuguese   => "پرتغالی اروپا"
    case Romanian             => "رومانیایی"
    case Russian              => "روسی"
    case Serbian              => "صربی"
    case Slovak               => "اسلواکی"
    case Slovenian            => "اسلوونیایی"
    case Spanish              => "اسپانیایی"
    case EuropeanSpanish      => "اسپانیایی اروپا"
    case LatinAmericanSpanish => "اسپانیایی آمریکای لاتین"
    case Swahili              => "سواحیلی"
    case Swedish              => "سوئدی"
    case Tamil                => "تامیلی"
    case Thai                 => "تایلندی"
    case Turkish              => "ترکی استانبولی"
    case Ukrainian            => "اوکراینی"
    case Urdu                 => "اردو"
    case Vietnamese           => "ویتنامی"
