package com.alecdorrington.megalingual
package languages

import com.alecdorrington.megalingual.Direction.LeftToRight

object Hungarian extends Language.Root("hu", LeftToRight):

  override lazy val flag: Flag = Flag.bands(
    vertical = false,
    "#ce2939",
    "#ffffff",
    "#477050",
  )

  override def plurals: Plural.Rule = Plural.oneOrOther

  override def names: PartialFunction[Language, String] =
    case Arabic               => "arab"
    case Bengali              => "bengáli"
    case Bulgarian            => "bolgár"
    case Catalan              => "katalán"
    case Chinese              => "kínai"
    case SimplifiedChinese    => "egyszerűsített kínai"
    case TraditionalChinese   => "hagyományos kínai"
    case TaiwaneseChinese     => "hagyományos kínai (Tajvan)"
    case Cantonese            => "kantoni"
    case Croatian             => "horvát"
    case Czech                => "cseh"
    case Danish               => "dán"
    case Dutch                => "holland"
    case English              => "angol"
    case BritishEnglish       => "brit angol"
    case AmericanEnglish      => "amerikai angol"
    case AustralianEnglish    => "ausztrál angol"
    case Estonian             => "észt"
    case Filipino             => "filippínó"
    case Finnish              => "finn"
    case French               => "francia"
    case German               => "német"
    case GermanGerman         => "német (Németország)"
    case GermanOfSwitzerland  => "német (Svájc)"
    case SwissStandardGerman  => "svájci felnémet"
    case SwissGerman          => "svájci német"
    case Greek                => "görög"
    case Hebrew               => "héber"
    case Hindi                => "hindi"
    case Hungarian            => "magyar"
    case Indonesian           => "indonéz"
    case Italian              => "olasz"
    case Japanese             => "japán"
    case Korean               => "koreai"
    case Latvian              => "lett"
    case Lithuanian           => "litván"
    case Malay                => "maláj"
    case Norwegian            => "norvég"
    case Persian              => "perzsa"
    case Polish               => "lengyel"
    case Portuguese           => "portugál"
    case BrazilianPortuguese  => "brazíliai portugál"
    case EuropeanPortuguese   => "európai portugál"
    case Romanian             => "román"
    case Russian              => "orosz"
    case Serbian              => "szerb"
    case Slovak               => "szlovák"
    case Slovenian            => "szlovén"
    case Spanish              => "spanyol"
    case EuropeanSpanish      => "európai spanyol"
    case LatinAmericanSpanish => "latin-amerikai spanyol"
    case Swahili              => "szuahéli"
    case Swedish              => "svéd"
    case Tamil                => "tamil"
    case Thai                 => "thai"
    case Turkish              => "török"
    case Ukrainian            => "ukrán"
    case Urdu                 => "urdu"
    case Vietnamese           => "vietnámi"
