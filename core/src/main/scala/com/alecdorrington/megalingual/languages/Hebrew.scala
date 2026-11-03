package com.alecdorrington.megalingual
package languages

import com.alecdorrington.megalingual.Direction.RightToLeft
import com.alecdorrington.megalingual.Plural.{One, Other, Two}

object Hebrew extends Language.Root("he", RightToLeft):

  override lazy val flag: Flag = Flag.drawn(
    """<rect width="60" height="40" fill="#ffffff"/>""" +
      """<path d="M0,6.5 H60 M0,33.5 H60" stroke="#0038b8" """ +
      """stroke-width="5"/>""" +
      """<path d="M30,12 L36.93,24 H23.07 Z M30,28 L36.93,16 H23.07 Z" """ +
      """fill="none" stroke="#0038b8" stroke-width="1.5"/>""",
  )

  override def plurals: Plural.Rule =
    n => if n == 1 then One else if n == 2 then Two else Other

  override def names: PartialFunction[Language, String] =
    case Arabic               => "ערבית"
    case Bengali              => "בנגלית"
    case Bulgarian            => "בולגרית"
    case Catalan              => "קטלאנית"
    case Chinese              => "סינית"
    case SimplifiedChinese    => "סינית פשוטה"
    case TraditionalChinese   => "סינית מסורתית"
    case TaiwaneseChinese     => "סינית מסורתית (טייוואן)"
    case Cantonese            => "קנטונזית"
    case Croatian             => "קרואטית"
    case Czech                => "צ׳כית"
    case Danish               => "דנית"
    case Dutch                => "הולנדית"
    case English              => "אנגלית"
    case BritishEnglish       => "אנגלית בריטית"
    case AmericanEnglish      => "אנגלית אמריקאית"
    case AustralianEnglish    => "אנגלית אוסטרלית"
    case Estonian             => "אסטונית"
    case Filipino             => "פיליפינית"
    case Finnish              => "פינית"
    case French               => "צרפתית"
    case German               => "גרמנית"
    case GermanGerman         => "גרמנית (גרמניה)"
    case GermanOfSwitzerland  => "גרמנית (שווייץ)"
    case SwissStandardGerman  => "גרמנית תקנית שוויצרית"
    case SwissGerman          => "גרמנית שוויצרית"
    case Greek                => "יוונית"
    case Hebrew               => "עברית"
    case Hindi                => "הינדי"
    case Hungarian            => "הונגרית"
    case Indonesian           => "אינדונזית"
    case Italian              => "איטלקית"
    case Japanese             => "יפנית"
    case Korean               => "קוריאנית"
    case Latvian              => "לטבית"
    case Lithuanian           => "ליטאית"
    case Malay                => "מלאית"
    case Norwegian            => "נורווגית"
    case Persian              => "פרסית"
    case Polish               => "פולנית"
    case Portuguese           => "פורטוגזית"
    case BrazilianPortuguese  => "פורטוגזית ברזילאית"
    case EuropeanPortuguese   => "פורטוגזית אירופאית"
    case Romanian             => "רומנית"
    case Russian              => "רוסית"
    case Serbian              => "סרבית"
    case Slovak               => "סלובקית"
    case Slovenian            => "סלובנית"
    case Spanish              => "ספרדית"
    case EuropeanSpanish      => "ספרדית אירופאית"
    case LatinAmericanSpanish => "ספרדית לטינו־אמריקאית"
    case Swahili              => "סווהילי"
    case Swedish              => "שוודית"
    case Tamil                => "טמילית"
    case Thai                 => "תאית"
    case Turkish              => "טורקית"
    case Ukrainian            => "אוקראינית"
    case Urdu                 => "אורדו"
    case Vietnamese           => "וייטנאמית"
