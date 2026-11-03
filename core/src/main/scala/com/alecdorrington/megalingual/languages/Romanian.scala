package com.alecdorrington.megalingual
package languages

import com.alecdorrington.megalingual.Direction.LeftToRight
import com.alecdorrington.megalingual.Plural.{Few, One, Other}

object Romanian extends Language.Root("ro", LeftToRight):

  override lazy val flag: Flag = Flag.bands(
    vertical = true,
    "#002b7f",
    "#fcd116",
    "#ce1126",
  )

  override def plurals: Plural.Rule = n =>
    if n == 1 then One
    else if n == 0 || Plural.teen(n, 1, 19) then Few
    else Other

  override def names: PartialFunction[Language, String] =
    case Arabic               => "arabă"
    case Bengali              => "bengaleză"
    case Bulgarian            => "bulgară"
    case Catalan              => "catalană"
    case Chinese              => "chineză"
    case SimplifiedChinese    => "chineză simplificată"
    case TraditionalChinese   => "chineză tradițională"
    case TaiwaneseChinese     => "chineză tradițională (Taiwan)"
    case Cantonese            => "cantoneză"
    case Croatian             => "croată"
    case Czech                => "cehă"
    case Danish               => "daneză"
    case Dutch                => "neerlandeză"
    case English              => "engleză"
    case BritishEnglish       => "engleză britanică"
    case AmericanEnglish      => "engleză americană"
    case AustralianEnglish    => "engleză australiană"
    case Estonian             => "estonă"
    case Filipino             => "filipineză"
    case Finnish              => "finlandeză"
    case French               => "franceză"
    case German               => "germană"
    case GermanGerman         => "germană (Germania)"
    case GermanOfSwitzerland  => "germană (Elveția)"
    case SwissStandardGerman  => "germană standard elvețiană"
    case SwissGerman          => "germană elvețiană"
    case Greek                => "greacă"
    case Hebrew               => "ebraică"
    case Hindi                => "hindi"
    case Hungarian            => "maghiară"
    case Indonesian           => "indoneziană"
    case Italian              => "italiană"
    case Japanese             => "japoneză"
    case Korean               => "coreeană"
    case Latvian              => "letonă"
    case Lithuanian           => "lituaniană"
    case Malay                => "malaeză"
    case Norwegian            => "norvegiană"
    case Persian              => "persană"
    case Polish               => "poloneză"
    case Portuguese           => "portugheză"
    case BrazilianPortuguese  => "portugheză braziliană"
    case EuropeanPortuguese   => "portugheză europeană"
    case Romanian             => "română"
    case Russian              => "rusă"
    case Serbian              => "sârbă"
    case Slovak               => "slovacă"
    case Slovenian            => "slovenă"
    case Spanish              => "spaniolă"
    case EuropeanSpanish      => "spaniolă europeană"
    case LatinAmericanSpanish => "spaniolă latino-americană"
    case Swahili              => "swahili"
    case Swedish              => "suedeză"
    case Tamil                => "tamilă"
    case Thai                 => "thailandeză"
    case Turkish              => "turcă"
    case Ukrainian            => "ucraineană"
    case Urdu                 => "urdu"
    case Vietnamese           => "vietnameză"
