package com.alecdorrington.megalingual
package languages

import com.alecdorrington.megalingual.Direction.LeftToRight
import com.alecdorrington.megalingual.Plural.{One, Other, Zero}

object Latvian extends Language.Root("lv", LeftToRight):

  override lazy val flag: Flag = Flag.drawn(
    """<rect width="60" height="40" fill="#9e3039"/>""" +
      """<rect y="16" width="60" height="8" fill="#ffffff"/>""",
  )

  override def plurals: Plural.Rule = n =>
    if n % 10 == 0 || Plural.teen(n, 11, 19) then Zero
    else if n % 10 == 1 then One
    else Other

  override def names: PartialFunction[Language, String] =
    case Arabic               => "arābu"
    case Bengali              => "bengāļu"
    case Bulgarian            => "bulgāru"
    case Catalan              => "katalāņu"
    case Chinese              => "ķīniešu"
    case SimplifiedChinese    => "ķīniešu vienkāršotā"
    case TraditionalChinese   => "ķīniešu tradicionālā"
    case TaiwaneseChinese     => "ķīniešu tradicionālā (Taivāna)"
    case Cantonese            => "kantoniešu"
    case Croatian             => "horvātu"
    case Czech                => "čehu"
    case Danish               => "dāņu"
    case Dutch                => "holandiešu"
    case English              => "angļu"
    case BritishEnglish       => "britu angļu"
    case AmericanEnglish      => "amerikāņu angļu"
    case AustralianEnglish    => "austrāliešu angļu"
    case Estonian             => "igauņu"
    case Filipino             => "filipīniešu"
    case Finnish              => "somu"
    case French               => "franču"
    case German               => "vācu"
    case GermanGerman         => "vācu (Vācija)"
    case GermanOfSwitzerland  => "vācu (Šveice)"
    case SwissStandardGerman  => "augšvācu (Šveice)"
    case SwissGerman          => "Šveices vācu"
    case Greek                => "grieķu"
    case Hebrew               => "ivrits"
    case Hindi                => "hindi"
    case Hungarian            => "ungāru"
    case Indonesian           => "indonēziešu"
    case Italian              => "itāļu"
    case Japanese             => "japāņu"
    case Korean               => "korejiešu"
    case Latvian              => "latviešu"
    case Lithuanian           => "lietuviešu"
    case Malay                => "malajiešu"
    case Norwegian            => "norvēģu"
    case Persian              => "persiešu"
    case Polish               => "poļu"
    case Portuguese           => "portugāļu"
    case BrazilianPortuguese  => "Brazīlijas portugāļu"
    case EuropeanPortuguese   => "Eiropas portugāļu"
    case Romanian             => "rumāņu"
    case Russian              => "krievu"
    case Serbian              => "serbu"
    case Slovak               => "slovāku"
    case Slovenian            => "slovēņu"
    case Spanish              => "spāņu"
    case EuropeanSpanish      => "Eiropas spāņu"
    case LatinAmericanSpanish => "Latīņamerikas spāņu"
    case Swahili              => "svahili"
    case Swedish              => "zviedru"
    case Tamil                => "tamilu"
    case Thai                 => "taju"
    case Turkish              => "turku"
    case Ukrainian            => "ukraiņu"
    case Urdu                 => "urdu"
    case Vietnamese           => "vjetnamiešu"
