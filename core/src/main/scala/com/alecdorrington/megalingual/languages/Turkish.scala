package com.alecdorrington.megalingual
package languages

import com.alecdorrington.megalingual.Direction.LeftToRight

object Turkish extends Language.Root("tr", LeftToRight):

  override lazy val flag: Flag = Flag.drawn(
    """<rect width="60" height="40" fill="#e30a17"/>""" +
      """<circle cx="22" cy="20" r="10" fill="#ffffff"/>""" +
      """<circle cx="24.5" cy="20" r="8" fill="#e30a17"/>""" +
      Flag.star(35, 20, 4.5, "#ffffff"),
  )

  override def plurals: Plural.Rule = Plural.oneOrOther

  override def names: PartialFunction[Language, String] =
    case Arabic               => "Arapça"
    case Bengali              => "Bengalce"
    case Bulgarian            => "Bulgarca"
    case Catalan              => "Katalanca"
    case Chinese              => "Çince"
    case SimplifiedChinese    => "Basitleştirilmiş Çince"
    case TraditionalChinese   => "Geleneksel Çince"
    case TaiwaneseChinese     => "Geleneksel Çince (Tayvan)"
    case Cantonese            => "Kantonca"
    case Croatian             => "Hırvatça"
    case Czech                => "Çekçe"
    case Danish               => "Danca"
    case Dutch                => "Felemenkçe"
    case English              => "İngilizce"
    case BritishEnglish       => "İngiliz İngilizcesi"
    case AmericanEnglish      => "Amerikan İngilizcesi"
    case AustralianEnglish    => "Avustralya İngilizcesi"
    case Estonian             => "Estonca"
    case Filipino             => "Filipince"
    case Finnish              => "Fince"
    case French               => "Fransızca"
    case German               => "Almanca"
    case GermanGerman         => "Almanca (Almanya)"
    case GermanOfSwitzerland  => "Almanca (İsviçre)"
    case SwissStandardGerman  => "İsviçre Yüksek Almancası"
    case SwissGerman          => "İsviçre Almancası"
    case Greek                => "Yunanca"
    case Hebrew               => "İbranice"
    case Hindi                => "Hintçe"
    case Hungarian            => "Macarca"
    case Indonesian           => "Endonezce"
    case Italian              => "İtalyanca"
    case Japanese             => "Japonca"
    case Korean               => "Korece"
    case Latvian              => "Letonca"
    case Lithuanian           => "Litvanca"
    case Malay                => "Malayca"
    case Norwegian            => "Norveççe"
    case Persian              => "Farsça"
    case Polish               => "Lehçe"
    case Portuguese           => "Portekizce"
    case BrazilianPortuguese  => "Brezilya Portekizcesi"
    case EuropeanPortuguese   => "Avrupa Portekizcesi"
    case Romanian             => "Rumence"
    case Russian              => "Rusça"
    case Serbian              => "Sırpça"
    case Slovak               => "Slovakça"
    case Slovenian            => "Slovence"
    case Spanish              => "İspanyolca"
    case EuropeanSpanish      => "Avrupa İspanyolcası"
    case LatinAmericanSpanish => "Latin Amerika İspanyolcası"
    case Swahili              => "Svahili dili"
    case Swedish              => "İsveççe"
    case Tamil                => "Tamilce"
    case Thai                 => "Tayca"
    case Turkish              => "Türkçe"
    case Ukrainian            => "Ukraynaca"
    case Urdu                 => "Urduca"
    case Vietnamese           => "Vietnamca"
