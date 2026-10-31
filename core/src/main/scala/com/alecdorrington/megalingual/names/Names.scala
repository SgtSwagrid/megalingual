package com.alecdorrington.megalingual
package names

import com.alecdorrington.megalingual.Language.*

/**
  * Every language's name in every other, each written by hand, one object per
  * language named in (`InFrench`). Varieties that name languages alike share
  * one, and one that names a few differently overrides its parent's.
  */
private[megalingual] object Names:

  /** The name of `language` in `in`, as written mid-sentence there. */
  def apply(language: Language, in: Language): String = in match
    case Arabic                      => InArabic(language)
    case Bengali                     => InBengali(language)
    case Bulgarian                   => InBulgarian(language)
    case Catalan                     => InCatalan(language)
    case Chinese | SimplifiedChinese => InSimplifiedChinese(language)
    case TraditionalChinese          => InTraditionalChinese(language)
    case Cantonese                   => InCantonese(language)
    case Croatian                    => InCroatian(language)
    case Czech                       => InCzech(language)
    case Danish                      => InDanish(language)
    case Dutch                       => InDutch(language)
    case English | BritishEnglish | AmericanEnglish | AustralianEnglish =>
      InEnglish(language)
    case Estonian                            => InEstonian(language)
    case Filipino                            => InFilipino(language)
    case Finnish                             => InFinnish(language)
    case French                              => InFrench(language)
    case German | GermanGerman | SwissStandardGerman => InGerman(language)
    case SwissGerman                         => InSwissGerman(language)
    case Greek                               => InGreek(language)
    case Hebrew                              => InHebrew(language)
    case Hindi                               => InHindi(language)
    case Hungarian                           => InHungarian(language)
    case Indonesian                          => InIndonesian(language)
    case Italian                             => InItalian(language)
    case Japanese                            => InJapanese(language)
    case Korean                              => InKorean(language)
    case Latvian                             => InLatvian(language)
    case Lithuanian                          => InLithuanian(language)
    case Malay                               => InMalay(language)
    case Norwegian                           => InNorwegian(language)
    case Persian                             => InPersian(language)
    case Polish                              => InPolish(language)
    case Portuguese | BrazilianPortuguese    => InPortuguese(language)
    case EuropeanPortuguese                  => InEuropeanPortuguese(language)
    case Romanian                            => InRomanian(language)
    case Russian                             => InRussian(language)
    case Serbian                             => InSerbian(language)
    case Slovak                              => InSlovak(language)
    case Slovenian                           => InSlovenian(language)
    case Spanish | EuropeanSpanish | LatinAmericanSpanish => InSpanish(language)
    case Swahili                                          => InSwahili(language)
    case Swedish                                          => InSwedish(language)
    case Tamil                                            => InTamil(language)
    case Thai                                             => InThai(language)
    case Turkish                                          => InTurkish(language)
    case Ukrainian  => InUkrainian(language)
    case Urdu       => InUrdu(language)
    case Vietnamese => InVietnamese(language)
