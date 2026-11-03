package com.alecdorrington.megalingual
package languages

import com.alecdorrington.megalingual.Direction.LeftToRight

object Slovak extends Language.Root("sk", LeftToRight):

  override lazy val flag: Flag = Flag.drawn(
    """<rect width="60" height="40" fill="#ee1c25"/>""" +
      """<rect width="60" height="26.67" fill="#0b4ea2"/>""" +
      """<rect width="60" height="13.33" fill="#ffffff"/>""" +
      """<path d="M13,8 H31 V22 Q31,31 22,34 Q13,31 13,22 Z" """ +
      """fill="#ee1c25" stroke="#ffffff" stroke-width="1.2"/>""" +
      """<path d="M14.5,29 Q22,24 29.5,29 Q26,32.5 22,33.5 Q18,32.5 14.5,29 Z" """ +
      """fill="#0b4ea2"/>""" +
      """<path d="M22,11 V27 M18,15 H26 M17,19.5 H27" stroke="#ffffff" """ +
      """stroke-width="1.8"/>""",
  )

  override def plurals: Plural.Rule = Plural.westSlavic

  override def names: PartialFunction[Language, String] =
    case Arabic               => "arabčina"
    case Bengali              => "bengálčina"
    case Bulgarian            => "bulharčina"
    case Catalan              => "katalánčina"
    case Chinese              => "čínština"
    case SimplifiedChinese    => "zjednodušená čínština"
    case TraditionalChinese   => "tradičná čínština"
    case TaiwaneseChinese     => "tradičná čínština (Taiwan)"
    case Cantonese            => "kantončina"
    case Croatian             => "chorvátčina"
    case Czech                => "čeština"
    case Danish               => "dánčina"
    case Dutch                => "holandčina"
    case English              => "angličtina"
    case BritishEnglish       => "britská angličtina"
    case AmericanEnglish      => "americká angličtina"
    case AustralianEnglish    => "austrálska angličtina"
    case Estonian             => "estónčina"
    case Filipino             => "filipínčina"
    case Finnish              => "fínčina"
    case French               => "francúzština"
    case German               => "nemčina"
    case GermanGerman         => "nemčina (Nemecko)"
    case GermanOfSwitzerland  => "nemčina (Švajčiarsko)"
    case SwissStandardGerman  => "švajčiarska spisovná nemčina"
    case SwissGerman          => "švajčiarska nemčina"
    case Greek                => "gréčtina"
    case Hebrew               => "hebrejčina"
    case Hindi                => "hindčina"
    case Hungarian            => "maďarčina"
    case Indonesian           => "indonézština"
    case Italian              => "taliančina"
    case Japanese             => "japončina"
    case Korean               => "kórejčina"
    case Latvian              => "lotyština"
    case Lithuanian           => "litovčina"
    case Malay                => "malajčina"
    case Norwegian            => "nórčina"
    case Persian              => "perzština"
    case Polish               => "poľština"
    case Portuguese           => "portugalčina"
    case BrazilianPortuguese  => "brazílska portugalčina"
    case EuropeanPortuguese   => "európska portugalčina"
    case Romanian             => "rumunčina"
    case Russian              => "ruština"
    case Serbian              => "srbčina"
    case Slovak               => "slovenčina"
    case Slovenian            => "slovinčina"
    case Spanish              => "španielčina"
    case EuropeanSpanish      => "európska španielčina"
    case LatinAmericanSpanish => "latinskoamerická španielčina"
    case Swahili              => "swahilčina"
    case Swedish              => "švédčina"
    case Tamil                => "tamilčina"
    case Thai                 => "thajčina"
    case Turkish              => "turečtina"
    case Ukrainian            => "ukrajinčina"
    case Urdu                 => "urdčina"
    case Vietnamese           => "vietnamčina"
