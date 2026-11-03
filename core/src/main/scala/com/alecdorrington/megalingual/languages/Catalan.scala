package com.alecdorrington.megalingual
package languages

import com.alecdorrington.megalingual.Direction.LeftToRight

object Catalan extends Language.Root("ca", LeftToRight):

  override lazy val flag: Flag = Flag.bands(
    vertical = false,
    "#fcdd09",
    "#da121a",
    "#fcdd09",
    "#da121a",
    "#fcdd09",
    "#da121a",
    "#fcdd09",
    "#da121a",
    "#fcdd09",
  )

  override def plurals: Plural.Rule = Plural.oneAndMillions

  override def names: PartialFunction[Language, String] =
    case Arabic               => "àrab"
    case Bengali              => "bengalí"
    case Bulgarian            => "búlgar"
    case Catalan              => "català"
    case Chinese              => "xinès"
    case SimplifiedChinese    => "xinès simplificat"
    case TraditionalChinese   => "xinès tradicional"
    case TaiwaneseChinese     => "xinès tradicional (Taiwan)"
    case Cantonese            => "cantonès"
    case Croatian             => "croat"
    case Czech                => "txec"
    case Danish               => "danès"
    case Dutch                => "neerlandès"
    case English              => "anglès"
    case BritishEnglish       => "anglès britànic"
    case AmericanEnglish      => "anglès americà"
    case AustralianEnglish    => "anglès australià"
    case Estonian             => "estonià"
    case Filipino             => "filipí"
    case Finnish              => "finès"
    case French               => "francès"
    case German               => "alemany"
    case GermanGerman         => "alemany (Alemanya)"
    case GermanOfSwitzerland  => "alemany (Suïssa)"
    case SwissStandardGerman  => "alemany estàndard suís"
    case SwissGerman          => "alemany suís"
    case Greek                => "grec"
    case Hebrew               => "hebreu"
    case Hindi                => "hindi"
    case Hungarian            => "hongarès"
    case Indonesian           => "indonesi"
    case Italian              => "italià"
    case Japanese             => "japonès"
    case Korean               => "coreà"
    case Latvian              => "letó"
    case Lithuanian           => "lituà"
    case Malay                => "malai"
    case Norwegian            => "noruec"
    case Persian              => "persa"
    case Polish               => "polonès"
    case Portuguese           => "portuguès"
    case BrazilianPortuguese  => "portuguès del Brasil"
    case EuropeanPortuguese   => "portuguès de Portugal"
    case Romanian             => "romanès"
    case Russian              => "rus"
    case Serbian              => "serbi"
    case Slovak               => "eslovac"
    case Slovenian            => "eslovè"
    case Spanish              => "espanyol"
    case EuropeanSpanish      => "espanyol europeu"
    case LatinAmericanSpanish => "espanyol hispanoamericà"
    case Swahili              => "suahili"
    case Swedish              => "suec"
    case Tamil                => "tàmil"
    case Thai                 => "tai"
    case Turkish              => "turc"
    case Ukrainian            => "ucraïnès"
    case Urdu                 => "urdú"
    case Vietnamese           => "vietnamita"
