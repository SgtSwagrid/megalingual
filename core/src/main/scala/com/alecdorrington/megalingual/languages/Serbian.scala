package com.alecdorrington.megalingual
package languages

import com.alecdorrington.megalingual.Direction.LeftToRight

/** Serbian in Cyrillic, its official script. */
object Serbian extends Language.Root("sr", LeftToRight):

  override lazy val flag: Flag = Flag.drawn(
    """<rect width="60" height="40" fill="#ffffff"/>""" +
      """<rect width="60" height="13.33" fill="#c6363c"/>""" +
      """<rect y="13.33" width="60" height="13.33" fill="#0c4076"/>""" +
      """<path d="M16,11 H28 V21 Q28,28 22,30 Q16,28 16,21 Z" """ +
      """fill="#c6363c" stroke="#ffffff" stroke-width="1"/>""" +
      """<path d="M22,13 V28 M17.5,19 H26.5" stroke="#ffffff" """ +
      """stroke-width="2"/>""",
  )

  override def plurals: Plural.Rule = Plural.southSlavic

  override def names: PartialFunction[Language, String] =
    case Arabic               => "арапски"
    case Bengali              => "бенгалски"
    case Bulgarian            => "бугарски"
    case Catalan              => "каталонски"
    case Chinese              => "кинески"
    case SimplifiedChinese    => "поједностављени кинески"
    case TraditionalChinese   => "традиционални кинески"
    case TaiwaneseChinese     => "традиционални кинески (Тајван)"
    case Cantonese            => "кантонски"
    case Croatian             => "хрватски"
    case Czech                => "чешки"
    case Danish               => "дански"
    case Dutch                => "холандски"
    case English              => "енглески"
    case BritishEnglish       => "британски енглески"
    case AmericanEnglish      => "амерички енглески"
    case AustralianEnglish    => "аустралијски енглески"
    case Estonian             => "естонски"
    case Filipino             => "филипински"
    case Finnish              => "фински"
    case French               => "француски"
    case German               => "немачки"
    case GermanGerman         => "немачки (Немачка)"
    case GermanOfSwitzerland  => "немачки (Швајцарска)"
    case SwissStandardGerman  => "швајцарски стандардни немачки"
    case SwissGerman          => "швајцарски немачки"
    case Greek                => "грчки"
    case Hebrew               => "хебрејски"
    case Hindi                => "хинди"
    case Hungarian            => "мађарски"
    case Indonesian           => "индонежански"
    case Italian              => "италијански"
    case Japanese             => "јапански"
    case Korean               => "корејски"
    case Latvian              => "летонски"
    case Lithuanian           => "литвански"
    case Malay                => "малајски"
    case Norwegian            => "норвешки"
    case Persian              => "персијски"
    case Polish               => "пољски"
    case Portuguese           => "португалски"
    case BrazilianPortuguese  => "бразилски португалски"
    case EuropeanPortuguese   => "европски португалски"
    case Romanian             => "румунски"
    case Russian              => "руски"
    case Serbian              => "српски"
    case Slovak               => "словачки"
    case Slovenian            => "словеначки"
    case Spanish              => "шпански"
    case EuropeanSpanish      => "европски шпански"
    case LatinAmericanSpanish => "латиноамерички шпански"
    case Swahili              => "свахили"
    case Swedish              => "шведски"
    case Tamil                => "тамилски"
    case Thai                 => "тајски"
    case Turkish              => "турски"
    case Ukrainian            => "украјински"
    case Urdu                 => "урду"
    case Vietnamese           => "вијетнамски"
