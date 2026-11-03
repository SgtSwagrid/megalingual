package com.alecdorrington.megalingual
package languages

import com.alecdorrington.megalingual.Direction.LeftToRight
import com.alecdorrington.megalingual.Plural.{One, Other}

/** Under `fil`, the tag browsers send for it, as `tl` names Tagalog. */
object Filipino extends Language.Root("fil", LeftToRight):

  override lazy val flag: Flag = Flag.drawn(
    """<rect width="60" height="20" fill="#0038a8"/>""" +
      """<rect y="20" width="60" height="20" fill="#ce1126"/>""" +
      """<polygon points="0,0 34.64,20 0,40" fill="#ffffff"/>""" +
      """<circle cx="11.5" cy="20" r="4" fill="#fcd116"/>""" +
      Flag.star(4, 5, 2, "#fcd116") + Flag.star(4, 35, 2, "#fcd116") +
      Flag.star(28, 20, 2, "#fcd116"),
  )

  override def plurals: Plural.Rule =
    n => if Set(4L, 6L, 9L)(n % 10) then Other else One

  override def names: PartialFunction[Language, String] =
    case Arabic               => "Arabe"
    case Bengali              => "Bengali"
    case Bulgarian            => "Bulgaro"
    case Catalan              => "Catalan"
    case Chinese              => "Tsino"
    case SimplifiedChinese    => "Pinasimpleng Tsino"
    case TraditionalChinese   => "Tradisyonal na Tsino"
    case TaiwaneseChinese     => "Tradisyonal na Tsino (Taiwan)"
    case Cantonese            => "Kantones"
    case Croatian             => "Kroato"
    case Czech                => "Tseko"
    case Danish               => "Danes"
    case Dutch                => "Olandes"
    case English              => "Ingles"
    case BritishEnglish       => "Ingles (Britanya)"
    case AmericanEnglish      => "Ingles (Amerika)"
    case AustralianEnglish    => "Ingles (Australya)"
    case Estonian             => "Estonyo"
    case Filipino             => "Filipino"
    case Finnish              => "Finlandes"
    case French               => "Pranses"
    case German               => "Aleman"
    case GermanGerman         => "Aleman (Alemanya)"
    case GermanOfSwitzerland  => "Aleman (Suwisa)"
    case SwissStandardGerman  => "Pamantayang Aleman (Suwisa)"
    case SwissGerman          => "Aleman ng Suwisa"
    case Greek                => "Griyego"
    case Hebrew               => "Hebreo"
    case Hindi                => "Hindi"
    case Hungarian            => "Unggaro"
    case Indonesian           => "Indones"
    case Italian              => "Italyano"
    case Japanese             => "Hapones"
    case Korean               => "Koreano"
    case Latvian              => "Latbiyano"
    case Lithuanian           => "Litwano"
    case Malay                => "Malayo"
    case Norwegian            => "Noruwego"
    case Persian              => "Persa"
    case Polish               => "Polako"
    case Portuguese           => "Portuges"
    case BrazilianPortuguese  => "Portuges (Brasil)"
    case EuropeanPortuguese   => "Portuges (Europa)"
    case Romanian             => "Rumano"
    case Russian              => "Ruso"
    case Serbian              => "Serbiyo"
    case Slovak               => "Eslobako"
    case Slovenian            => "Eslobeno"
    case Spanish              => "Espanyol"
    case EuropeanSpanish      => "Espanyol (Europa)"
    case LatinAmericanSpanish => "Espanyol (Latinong Amerika)"
    case Swahili              => "Swahili"
    case Swedish              => "Suweko"
    case Tamil                => "Tamil"
    case Thai                 => "Thai"
    case Turkish              => "Turko"
    case Ukrainian            => "Ukranyo"
    case Urdu                 => "Urdu"
    case Vietnamese           => "Biyetnames"
