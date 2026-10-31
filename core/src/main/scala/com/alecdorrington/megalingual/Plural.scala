package com.alecdorrington.megalingual

import com.alecdorrington.megalingual.Language.*

/**
  * A plural category, by which a language chooses the form of what it counts,
  * as Unicode's CLDR defines them. A language uses only some
  * ([[Language.plural]]): English `One` and `Other`, Polish `One`, `Few` and
  * `Many`.
  */
enum Plural:

  case Zero, One, Two, Few, Many, Other

object Plural:

  /**
    * The category of a whole number in a language, by CLDR's cardinal rules for
    * numbers with no fraction digits. A variety follows its parent's rules
    * unless it has its own.
    */
  private[megalingual] def of(language: Language, count: Long): Plural =
    val n = math.abs(count)
    language match
      case English | BritishEnglish | AmericanEnglish | AustralianEnglish |
        German | GermanGerman | SwissStandardGerman | SwissGerman | Dutch |
        Danish | Swedish | Norwegian | Finnish | Estonian | Greek | Hungarian |
        Turkish | Bulgarian | Swahili | Urdu | Tamil =>
        if n == 1 then One else Other
      case French | Portuguese | BrazilianPortuguese =>
        if n <= 1 then One else millions(n)
      case Spanish | EuropeanSpanish | LatinAmericanSpanish | Italian |
        Catalan | EuropeanPortuguese => if n == 1 then One else millions(n)
      case Hindi | Bengali | Persian => if n <= 1 then One else Other
      case Filipino => if Set(4L, 6L, 9L)(n % 10) then Other else One
      case Hebrew   => if n == 1 then One else if n == 2 then Two else Other
      case Arabic   => arabic(n)
      case Czech | Slovak =>
        if n == 1 then One else if n <= 4 && n >= 2 then Few else Other
      case Polish              => polish(n)
      case Russian | Ukrainian => eastSlavic(n)
      case Croatian | Serbian  => southSlavic(n)
      case Slovenian           => slovenian(n)
      case Latvian             => latvian(n)
      case Lithuanian          => lithuanian(n)
      case Romanian            => romanian(n)
      case Chinese | SimplifiedChinese | TraditionalChinese | Cantonese |
        Japanese | Korean | Thai | Indonesian | Vietnamese | Malay => Other

  /** A whole number of millions takes `de` in French, Spanish and kin. */
  private def millions(n: Long): Plural =
    if n != 0 && n % 1000000 == 0 then Many else Other

  private def arabic(n: Long): Plural =
    if n == 0 then Zero
    else if n == 1 then One
    else if n == 2 then Two
    else if n % 100 >= 3 && n % 100 <= 10 then Few
    else if n % 100 >= 11 then Many
    else Other

  private def polish(n: Long): Plural =
    if n == 1 then One else if few(n) then Few else Many

  private def eastSlavic(n: Long): Plural =
    if oneButEleven(n) then One else if few(n) then Few else Many

  private def southSlavic(n: Long): Plural =
    if oneButEleven(n) then One else if few(n) then Few else Other

  /** Ending in 1, but not in 11. */
  private def oneButEleven(n: Long): Boolean = n % 10 == 1 && n % 100 != 11

  /** Ending in 2 to 4, but not in 12 to 14. */
  private def few(n: Long): Boolean = n % 10 >= 2 && n % 10 <= 4 &&
    !teen(n, 12, 14)

  private def teen(n: Long, from: Int, to: Int): Boolean = n % 100 >= from &&
    n % 100 <= to

  private def slovenian(n: Long): Plural = n % 100 match
    case 1     => One
    case 2     => Two
    case 3 | 4 => Few
    case _     => Other

  private def latvian(n: Long): Plural =
    if n % 10 == 0 || teen(n, 11, 19) then Zero
    else if n % 10 == 1 then One
    else Other

  private def lithuanian(n: Long): Plural =
    if teen(n, 11, 19) then Other
    else if n % 10 == 1 then One
    else if n % 10 >= 2 then Few
    else Other

  private def romanian(n: Long): Plural =
    if n == 1 then One else if n == 0 || teen(n, 1, 19) then Few else Other
