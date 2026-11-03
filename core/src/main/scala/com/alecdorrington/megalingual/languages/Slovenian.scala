package com.alecdorrington.megalingual
package languages

import com.alecdorrington.megalingual.Direction.LeftToRight
import com.alecdorrington.megalingual.Plural.{Few, One, Other, Two}

object Slovenian extends Language.Root("sl", LeftToRight):

  override lazy val flag: Flag = Flag.drawn(
    """<rect width="60" height="40" fill="#ed1c24"/>""" +
      """<rect width="60" height="26.67" fill="#005da4"/>""" +
      """<rect width="60" height="13.33" fill="#ffffff"/>""" +
      """<path d="M10,6 H24 V16 Q24,23 17,25 Q10,23 10,16 Z" """ +
      """fill="#005da4" stroke="#ed1c24" stroke-width="1.2"/>""" +
      """<polygon points="11,20 14.5,14 17,17.5 19.5,14 23,20" fill="#ffffff"/>""",
  )

  override def plurals: Plural.Rule = n =>
    n % 100 match
      case 1     => One
      case 2     => Two
      case 3 | 4 => Few
      case _     => Other

  override def names: PartialFunction[Language, String] =
    case Arabic               => "arabščina"
    case Bengali              => "bengalščina"
    case Bulgarian            => "bolgarščina"
    case Catalan              => "katalonščina"
    case Chinese              => "kitajščina"
    case SimplifiedChinese    => "poenostavljena kitajščina"
    case TraditionalChinese   => "tradicionalna kitajščina"
    case TaiwaneseChinese     => "tradicionalna kitajščina (Tajvan)"
    case Cantonese            => "kantonščina"
    case Croatian             => "hrvaščina"
    case Czech                => "češčina"
    case Danish               => "danščina"
    case Dutch                => "nizozemščina"
    case English              => "angleščina"
    case BritishEnglish       => "britanska angleščina"
    case AmericanEnglish      => "ameriška angleščina"
    case AustralianEnglish    => "avstralska angleščina"
    case Estonian             => "estonščina"
    case Filipino             => "filipinščina"
    case Finnish              => "finščina"
    case French               => "francoščina"
    case German               => "nemščina"
    case GermanGerman         => "nemščina (Nemčija)"
    case GermanOfSwitzerland  => "nemščina (Švica)"
    case SwissStandardGerman  => "švicarska knjižna nemščina"
    case SwissGerman          => "švicarska nemščina"
    case Greek                => "grščina"
    case Hebrew               => "hebrejščina"
    case Hindi                => "hindijščina"
    case Hungarian            => "madžarščina"
    case Indonesian           => "indonezijščina"
    case Italian              => "italijanščina"
    case Japanese             => "japonščina"
    case Korean               => "korejščina"
    case Latvian              => "latvijščina"
    case Lithuanian           => "litovščina"
    case Malay                => "malajščina"
    case Norwegian            => "norveščina"
    case Persian              => "perzijščina"
    case Polish               => "poljščina"
    case Portuguese           => "portugalščina"
    case BrazilianPortuguese  => "brazilska portugalščina"
    case EuropeanPortuguese   => "evropska portugalščina"
    case Romanian             => "romunščina"
    case Russian              => "ruščina"
    case Serbian              => "srbščina"
    case Slovak               => "slovaščina"
    case Slovenian            => "slovenščina"
    case Spanish              => "španščina"
    case EuropeanSpanish      => "evropska španščina"
    case LatinAmericanSpanish => "latinskoameriška španščina"
    case Swahili              => "svahili"
    case Swedish              => "švedščina"
    case Tamil                => "tamilščina"
    case Thai                 => "tajščina"
    case Turkish              => "turščina"
    case Ukrainian            => "ukrajinščina"
    case Urdu                 => "urdujščina"
    case Vietnamese           => "vietnamščina"
