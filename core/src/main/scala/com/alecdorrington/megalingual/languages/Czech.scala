package com.alecdorrington.megalingual
package languages

import com.alecdorrington.megalingual.Direction.LeftToRight

object Czech extends Language.Root("cs", LeftToRight):

  override lazy val flag: Flag = Flag.drawn(
    """<rect width="60" height="20" fill="#ffffff"/>""" +
      """<rect y="20" width="60" height="20" fill="#d7141a"/>""" +
      """<polygon points="0,0 30,20 0,40" fill="#11457e"/>""",
  )

  override def plurals: Plural.Rule = Plural.westSlavic

  override def names: PartialFunction[Language, String] =
    case Arabic               => "arabština"
    case Bengali              => "bengálština"
    case Bulgarian            => "bulharština"
    case Catalan              => "katalánština"
    case Chinese              => "čínština"
    case SimplifiedChinese    => "zjednodušená čínština"
    case TraditionalChinese   => "tradiční čínština"
    case TaiwaneseChinese     => "tradiční čínština (Tchaj-wan)"
    case Cantonese            => "kantonština"
    case Croatian             => "chorvatština"
    case Czech                => "čeština"
    case Danish               => "dánština"
    case Dutch                => "nizozemština"
    case English              => "angličtina"
    case BritishEnglish       => "britská angličtina"
    case AmericanEnglish      => "americká angličtina"
    case AustralianEnglish    => "australská angličtina"
    case Estonian             => "estonština"
    case Filipino             => "filipínština"
    case Finnish              => "finština"
    case French               => "francouzština"
    case German               => "němčina"
    case GermanGerman         => "němčina (Německo)"
    case GermanOfSwitzerland  => "němčina (Švýcarsko)"
    case SwissStandardGerman  => "švýcarská spisovná němčina"
    case SwissGerman          => "švýcarská němčina"
    case Greek                => "řečtina"
    case Hebrew               => "hebrejština"
    case Hindi                => "hindština"
    case Hungarian            => "maďarština"
    case Indonesian           => "indonéština"
    case Italian              => "italština"
    case Japanese             => "japonština"
    case Korean               => "korejština"
    case Latvian              => "lotyština"
    case Lithuanian           => "litevština"
    case Malay                => "malajština"
    case Norwegian            => "norština"
    case Persian              => "perština"
    case Polish               => "polština"
    case Portuguese           => "portugalština"
    case BrazilianPortuguese  => "brazilská portugalština"
    case EuropeanPortuguese   => "evropská portugalština"
    case Romanian             => "rumunština"
    case Russian              => "ruština"
    case Serbian              => "srbština"
    case Slovak               => "slovenština"
    case Slovenian            => "slovinština"
    case Spanish              => "španělština"
    case EuropeanSpanish      => "evropská španělština"
    case LatinAmericanSpanish => "latinskoamerická španělština"
    case Swahili              => "svahilština"
    case Swedish              => "švédština"
    case Tamil                => "tamilština"
    case Thai                 => "thajština"
    case Turkish              => "turečtina"
    case Ukrainian            => "ukrajinština"
    case Urdu                 => "urdština"
    case Vietnamese           => "vietnamština"
