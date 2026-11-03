package com.alecdorrington.megalingual
package languages

import com.alecdorrington.megalingual.Direction.LeftToRight

object Dutch extends Language.Root("nl", LeftToRight):

  override lazy val flag: Flag = Flag.bands(
    vertical = false,
    "#ae1c28",
    "#ffffff",
    "#21468b",
  )

  override def plurals: Plural.Rule = Plural.oneOrOther

  override def names: PartialFunction[Language, String] =
    case Arabic               => "Arabisch"
    case Bengali              => "Bengaals"
    case Bulgarian            => "Bulgaars"
    case Catalan              => "Catalaans"
    case Chinese              => "Chinees"
    case SimplifiedChinese    => "vereenvoudigd Chinees"
    case TraditionalChinese   => "traditioneel Chinees"
    case TaiwaneseChinese     => "traditioneel Chinees (Taiwan)"
    case Cantonese            => "Kantonees"
    case Croatian             => "Kroatisch"
    case Czech                => "Tsjechisch"
    case Danish               => "Deens"
    case Dutch                => "Nederlands"
    case English              => "Engels"
    case BritishEnglish       => "Brits Engels"
    case AmericanEnglish      => "Amerikaans Engels"
    case AustralianEnglish    => "Australisch Engels"
    case Estonian             => "Estisch"
    case Filipino             => "Filipijns"
    case Finnish              => "Fins"
    case French               => "Frans"
    case German               => "Duits"
    case GermanGerman         => "Duits (Duitsland)"
    case GermanOfSwitzerland  => "Duits (Zwitserland)"
    case SwissStandardGerman  => "Zwitsers Hoogduits"
    case SwissGerman          => "Zwitserduits"
    case Greek                => "Grieks"
    case Hebrew               => "Hebreeuws"
    case Hindi                => "Hindi"
    case Hungarian            => "Hongaars"
    case Indonesian           => "Indonesisch"
    case Italian              => "Italiaans"
    case Japanese             => "Japans"
    case Korean               => "Koreaans"
    case Latvian              => "Lets"
    case Lithuanian           => "Litouws"
    case Malay                => "Maleis"
    case Norwegian            => "Noors"
    case Persian              => "Perzisch"
    case Polish               => "Pools"
    case Portuguese           => "Portugees"
    case BrazilianPortuguese  => "Braziliaans Portugees"
    case EuropeanPortuguese   => "Europees Portugees"
    case Romanian             => "Roemeens"
    case Russian              => "Russisch"
    case Serbian              => "Servisch"
    case Slovak               => "Slowaaks"
    case Slovenian            => "Sloveens"
    case Spanish              => "Spaans"
    case EuropeanSpanish      => "Europees Spaans"
    case LatinAmericanSpanish => "Latijns-Amerikaans Spaans"
    case Swahili              => "Swahili"
    case Swedish              => "Zweeds"
    case Tamil                => "Tamil"
    case Thai                 => "Thai"
    case Turkish              => "Turks"
    case Ukrainian            => "Oekraïens"
    case Urdu                 => "Urdu"
    case Vietnamese           => "Vietnamees"
