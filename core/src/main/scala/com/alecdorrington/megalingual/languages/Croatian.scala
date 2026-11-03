package com.alecdorrington.megalingual
package languages

import com.alecdorrington.megalingual.Direction.LeftToRight

object Croatian extends Language.Root("hr", LeftToRight):

  override lazy val flag: Flag = Flag.drawn(
    """<rect width="60" height="40" fill="#ffffff"/>""" +
      """<rect width="60" height="13.33" fill="#ff0000"/>""" +
      """<rect y="26.67" width="60" height="13.33" fill="#171796"/>""" +
      """<rect x="24" y="11" width="12" height="15" fill="#ffffff" """ +
      """stroke="#ff0000" stroke-width="0.8"/>""" +
      (for
        row    <- 0 until 5
        column <- 0 until 4 if (row + column) % 2 == 0
      yield s"""<rect x="${ 24 + column * 3 }" y="${ 11 + row * 3 }" """ +
        """width="3" height="3" fill="#ff0000"/>""").mkString,
  )

  override def plurals: Plural.Rule = Plural.southSlavic

  override def names: PartialFunction[Language, String] =
    case Arabic               => "arapski"
    case Bengali              => "bengalski"
    case Bulgarian            => "bugarski"
    case Catalan              => "katalonski"
    case Chinese              => "kineski"
    case SimplifiedChinese    => "pojednostavljeni kineski"
    case TraditionalChinese   => "tradicionalni kineski"
    case TaiwaneseChinese     => "tradicionalni kineski (Tajvan)"
    case Cantonese            => "kantonski"
    case Croatian             => "hrvatski"
    case Czech                => "češki"
    case Danish               => "danski"
    case Dutch                => "nizozemski"
    case English              => "engleski"
    case BritishEnglish       => "britanski engleski"
    case AmericanEnglish      => "američki engleski"
    case AustralianEnglish    => "australski engleski"
    case Estonian             => "estonski"
    case Filipino             => "filipinski"
    case Finnish              => "finski"
    case French               => "francuski"
    case German               => "njemački"
    case GermanGerman         => "njemački (Njemačka)"
    case GermanOfSwitzerland  => "njemački (Švicarska)"
    case SwissStandardGerman  => "švicarski standardni njemački"
    case SwissGerman          => "švicarski njemački"
    case Greek                => "grčki"
    case Hebrew               => "hebrejski"
    case Hindi                => "hindski"
    case Hungarian            => "mađarski"
    case Indonesian           => "indonezijski"
    case Italian              => "talijanski"
    case Japanese             => "japanski"
    case Korean               => "korejski"
    case Latvian              => "latvijski"
    case Lithuanian           => "litavski"
    case Malay                => "malajski"
    case Norwegian            => "norveški"
    case Persian              => "perzijski"
    case Polish               => "poljski"
    case Portuguese           => "portugalski"
    case BrazilianPortuguese  => "brazilski portugalski"
    case EuropeanPortuguese   => "europski portugalski"
    case Romanian             => "rumunjski"
    case Russian              => "ruski"
    case Serbian              => "srpski"
    case Slovak               => "slovački"
    case Slovenian            => "slovenski"
    case Spanish              => "španjolski"
    case EuropeanSpanish      => "europski španjolski"
    case LatinAmericanSpanish => "latinoamerički španjolski"
    case Swahili              => "svahili"
    case Swedish              => "švedski"
    case Tamil                => "tamilski"
    case Thai                 => "tajski"
    case Turkish              => "turski"
    case Ukrainian            => "ukrajinski"
    case Urdu                 => "urdski"
    case Vietnamese           => "vijetnamski"
