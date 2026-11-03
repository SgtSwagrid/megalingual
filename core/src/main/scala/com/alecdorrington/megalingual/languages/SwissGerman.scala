package com.alecdorrington.megalingual
package languages

/**
  * The Alemannic dialects of Switzerland, under `gsw`, their own code. Their
  * speakers mostly write Swiss Standard German instead. Alsatian shares the
  * code, having no case of its own, so `gsw-FR` finds Swiss German too.
  */
object SwissGerman extends Language.Variety("gsw", of = GermanOfSwitzerland):

  // Switzerland's, as Swiss Standard German's.
  override lazy val flag: Flag = SwissStandardGerman.flag

  /**
    * The names Swiss German gives otherwise than German, as Unicode's CLDR
    * spells them, having no standard spelling of its own.
    */
  override def names: PartialFunction[Language, String] =
    case Bulgarian            => "Bulgaarisch"
    case Catalan              => "Katalaanisch"
    case Chinese              => "Chineesisch"
    case SimplifiedChinese    => "veräifachts Chineesisch"
    case TraditionalChinese   => "tradizionells Chineesisch"
    case TaiwaneseChinese     => "tradizionells Chineesisch (Taiwan)"
    case Cantonese            => "Kantoneesisch"
    case Croatian             => "Kroazisch"
    case Danish               => "Tänisch"
    case Dutch                => "Niderländisch"
    case English              => "Änglisch"
    case BritishEnglish       => "britischs Änglisch"
    case AmericanEnglish      => "amerikanischs Änglisch"
    case AustralianEnglish    => "auschtralischs Änglisch"
    case Estonian             => "Eestnisch"
    case German               => "Tüütsch"
    case GermanGerman         => "Tüütsch (Tüütschland)"
    case GermanOfSwitzerland  => "Tüütsch (Schwiiz)"
    case SwissStandardGerman  => "Schwiizer Hochtüütsch"
    case SwissGerman          => "Schwiizertüütsch"
    case Italian              => "Italiänisch"
    case Korean               => "Koreaanisch"
    case Portuguese           => "Portugiisisch"
    case BrazilianPortuguese  => "brasilianischs Portugiisisch"
    case EuropeanPortuguese   => "europäischs Portugiisisch"
    case Spanish              => "Schpanisch"
    case EuropeanSpanish      => "europäischs Schpanisch"
    case LatinAmericanSpanish => "latiinamerikanischs Schpanisch"
    case Swahili              => "Suaheli"
    case Tamil                => "Tamilisch"
