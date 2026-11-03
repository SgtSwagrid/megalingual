package com.alecdorrington.megalingual
package languages

import com.alecdorrington.megalingual.Direction.LeftToRight

object Malay extends Language.Root("ms", LeftToRight):

  override lazy val flag: Flag = Flag.drawn(
    """<rect width="60" height="40" fill="#ffffff"/>""" + (0 until 14 by 2)
      .map(stripe =>
        s"""<rect y="${ stripe * 40.0 / 14 }" width="60" """ +
          s"""height="${ 40.0 / 14 }" fill="#cc0001"/>""",
      )
      .mkString + """<rect width="30" height="22.86" fill="#010066"/>""" +
      """<circle cx="11" cy="11.43" r="7" fill="#ffcc00"/>""" +
      """<circle cx="13" cy="11.43" r="5.8" fill="#010066"/>""" + Flag.star(
        21.5,
        11.43,
        5,
        "#ffcc00",
        points = 14,
        inner = 0.45,
      ),
  )

  override def plurals: Plural.Rule = Plural.invariable

  override def names: PartialFunction[Language, String] =
    case Arabic               => "bahasa Arab"
    case Bengali              => "bahasa Benggali"
    case Bulgarian            => "bahasa Bulgaria"
    case Catalan              => "bahasa Catalonia"
    case Chinese              => "bahasa Cina"
    case SimplifiedChinese    => "bahasa Cina Ringkas"
    case TraditionalChinese   => "bahasa Cina Tradisional"
    case TaiwaneseChinese     => "bahasa Cina Tradisional (Taiwan)"
    case Cantonese            => "bahasa Kantonis"
    case Croatian             => "bahasa Croatia"
    case Czech                => "bahasa Czech"
    case Danish               => "bahasa Denmark"
    case Dutch                => "bahasa Belanda"
    case English              => "bahasa Inggeris"
    case BritishEnglish       => "bahasa Inggeris British"
    case AmericanEnglish      => "bahasa Inggeris Amerika"
    case AustralianEnglish    => "bahasa Inggeris Australia"
    case Estonian             => "bahasa Estonia"
    case Filipino             => "bahasa Filipino"
    case Finnish              => "bahasa Finland"
    case French               => "bahasa Perancis"
    case German               => "bahasa Jerman"
    case GermanGerman         => "bahasa Jerman (Jerman)"
    case GermanOfSwitzerland  => "bahasa Jerman (Switzerland)"
    case SwissStandardGerman  => "bahasa Jerman Halus Switzerland"
    case SwissGerman          => "bahasa Jerman Switzerland"
    case Greek                => "bahasa Greek"
    case Hebrew               => "bahasa Ibrani"
    case Hindi                => "bahasa Hindi"
    case Hungarian            => "bahasa Hungary"
    case Indonesian           => "bahasa Indonesia"
    case Italian              => "bahasa Itali"
    case Japanese             => "bahasa Jepun"
    case Korean               => "bahasa Korea"
    case Latvian              => "bahasa Latvia"
    case Lithuanian           => "bahasa Lithuania"
    case Malay                => "bahasa Melayu"
    case Norwegian            => "bahasa Norway"
    case Persian              => "bahasa Parsi"
    case Polish               => "bahasa Poland"
    case Portuguese           => "bahasa Portugis"
    case BrazilianPortuguese  => "bahasa Portugis Brazil"
    case EuropeanPortuguese   => "bahasa Portugis Eropah"
    case Romanian             => "bahasa Romania"
    case Russian              => "bahasa Rusia"
    case Serbian              => "bahasa Serbia"
    case Slovak               => "bahasa Slovak"
    case Slovenian            => "bahasa Slovenia"
    case Spanish              => "bahasa Sepanyol"
    case EuropeanSpanish      => "bahasa Sepanyol Eropah"
    case LatinAmericanSpanish => "bahasa Sepanyol Amerika Latin"
    case Swahili              => "bahasa Swahili"
    case Swedish              => "bahasa Sweden"
    case Tamil                => "bahasa Tamil"
    case Thai                 => "bahasa Thai"
    case Turkish              => "bahasa Turki"
    case Ukrainian            => "bahasa Ukraine"
    case Urdu                 => "bahasa Urdu"
    case Vietnamese           => "bahasa Vietnam"
