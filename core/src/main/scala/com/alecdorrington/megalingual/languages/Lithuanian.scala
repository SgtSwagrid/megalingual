package com.alecdorrington.megalingual
package languages

import com.alecdorrington.megalingual.Direction.LeftToRight
import com.alecdorrington.megalingual.Plural.{Few, One, Other}

object Lithuanian extends Language.Root("lt", LeftToRight):

  override lazy val flag: Flag = Flag.bands(
    vertical = false,
    "#fdb913",
    "#006a44",
    "#c1272d",
  )

  override def plurals: Plural.Rule = n =>
    if Plural.teen(n, 11, 19) then Other
    else if n % 10 == 1 then One
    else if n % 10 >= 2 then Few
    else Other

  override def names: PartialFunction[Language, String] =
    case Arabic               => "arabų"
    case Bengali              => "bengalų"
    case Bulgarian            => "bulgarų"
    case Catalan              => "katalonų"
    case Chinese              => "kinų"
    case SimplifiedChinese    => "supaprastintoji kinų"
    case TraditionalChinese   => "tradicinė kinų"
    case TaiwaneseChinese     => "tradicinė kinų (Taivanas)"
    case Cantonese            => "kantoniečių"
    case Croatian             => "kroatų"
    case Czech                => "čekų"
    case Danish               => "danų"
    case Dutch                => "olandų"
    case English              => "anglų"
    case BritishEnglish       => "Didžiosios Britanijos anglų"
    case AmericanEnglish      => "Jungtinių Valstijų anglų"
    case AustralianEnglish    => "Australijos anglų"
    case Estonian             => "estų"
    case Filipino             => "filipiniečių"
    case Finnish              => "suomių"
    case French               => "prancūzų"
    case German               => "vokiečių"
    case GermanGerman         => "vokiečių (Vokietija)"
    case GermanOfSwitzerland  => "vokiečių (Šveicarija)"
    case SwissStandardGerman  => "Šveicarijos aukštutinė vokiečių"
    case SwissGerman          => "Šveicarijos vokiečių"
    case Greek                => "graikų"
    case Hebrew               => "hebrajų"
    case Hindi                => "hindi"
    case Hungarian            => "vengrų"
    case Indonesian           => "indoneziečių"
    case Italian              => "italų"
    case Japanese             => "japonų"
    case Korean               => "korėjiečių"
    case Latvian              => "latvių"
    case Lithuanian           => "lietuvių"
    case Malay                => "malajiečių"
    case Norwegian            => "norvegų"
    case Persian              => "persų"
    case Polish               => "lenkų"
    case Portuguese           => "portugalų"
    case BrazilianPortuguese  => "Brazilijos portugalų"
    case EuropeanPortuguese   => "Europos portugalų"
    case Romanian             => "rumunų"
    case Russian              => "rusų"
    case Serbian              => "serbų"
    case Slovak               => "slovakų"
    case Slovenian            => "slovėnų"
    case Spanish              => "ispanų"
    case EuropeanSpanish      => "Europos ispanų"
    case LatinAmericanSpanish => "Lotynų Amerikos ispanų"
    case Swahili              => "suahilių"
    case Swedish              => "švedų"
    case Tamil                => "tamilų"
    case Thai                 => "tajų"
    case Turkish              => "turkų"
    case Ukrainian            => "ukrainiečių"
    case Urdu                 => "urdų"
    case Vietnamese           => "vietnamiečių"
