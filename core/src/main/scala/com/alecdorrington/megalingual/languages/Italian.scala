package com.alecdorrington.megalingual
package languages

import com.alecdorrington.megalingual.Direction.LeftToRight

object Italian extends Language.Root("it", LeftToRight):

  override lazy val flag: Flag = Flag.bands(
    vertical = true,
    "#009246",
    "#ffffff",
    "#ce2b37",
  )

  override def plurals: Plural.Rule = Plural.oneAndMillions

  override def names: PartialFunction[Language, String] =
    case Arabic               => "arabo"
    case Bengali              => "bengalese"
    case Bulgarian            => "bulgaro"
    case Catalan              => "catalano"
    case Chinese              => "cinese"
    case SimplifiedChinese    => "cinese semplificato"
    case TraditionalChinese   => "cinese tradizionale"
    case TaiwaneseChinese     => "cinese tradizionale (Taiwan)"
    case Cantonese            => "cantonese"
    case Croatian             => "croato"
    case Czech                => "ceco"
    case Danish               => "danese"
    case Dutch                => "olandese"
    case English              => "inglese"
    case BritishEnglish       => "inglese britannico"
    case AmericanEnglish      => "inglese americano"
    case AustralianEnglish    => "inglese australiano"
    case Estonian             => "estone"
    case Filipino             => "filippino"
    case Finnish              => "finlandese"
    case French               => "francese"
    case German               => "tedesco"
    case GermanGerman         => "tedesco (Germania)"
    case GermanOfSwitzerland  => "tedesco (Svizzera)"
    case SwissStandardGerman  => "tedesco standard svizzero"
    case SwissGerman          => "tedesco svizzero"
    case Greek                => "greco"
    case Hebrew               => "ebraico"
    case Hindi                => "hindi"
    case Hungarian            => "ungherese"
    case Indonesian           => "indonesiano"
    case Italian              => "italiano"
    case Japanese             => "giapponese"
    case Korean               => "coreano"
    case Latvian              => "lettone"
    case Lithuanian           => "lituano"
    case Malay                => "malese"
    case Norwegian            => "norvegese"
    case Persian              => "persiano"
    case Polish               => "polacco"
    case Portuguese           => "portoghese"
    case BrazilianPortuguese  => "portoghese brasiliano"
    case EuropeanPortuguese   => "portoghese europeo"
    case Romanian             => "rumeno"
    case Russian              => "russo"
    case Serbian              => "serbo"
    case Slovak               => "slovacco"
    case Slovenian            => "sloveno"
    case Spanish              => "spagnolo"
    case EuropeanSpanish      => "spagnolo europeo"
    case LatinAmericanSpanish => "spagnolo latinoamericano"
    case Swahili              => "swahili"
    case Swedish              => "svedese"
    case Tamil                => "tamil"
    case Thai                 => "thailandese"
    case Turkish              => "turco"
    case Ukrainian            => "ucraino"
    case Urdu                 => "urdu"
    case Vietnamese           => "vietnamita"
