package com.alecdorrington.megalingual

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
    * A plural rule: the category of each whole number, never negative, by
    * CLDR's cardinal rules for numbers with no fraction digits.
    */
  type Rule = Long => Plural

  /** One alone is one, as English counts. */
  def oneOrOther(n: Long): Plural = if n == 1 then One else Other

  /** Zero and one are one, as Hindi counts. */
  def zeroOrOne(n: Long): Plural = if n <= 1 then One else Other

  /** Zero and one are one, and whole millions many, as French counts. */
  def zeroAsOne(n: Long): Plural = if n <= 1 then One else millions(n)

  /** One alone is one, and whole millions many, as Spanish counts. */
  def oneAndMillions(n: Long): Plural = if n == 1 then One else millions(n)

  /** One, few up to four, and other, as Czech and Slovak count. */
  def westSlavic(n: Long): Plural =
    if n == 1 then One else if n <= 4 && n >= 2 then Few else Other

  /** One, few and many, by the last digits, as Russian and Ukrainian count. */
  def eastSlavic(n: Long): Plural =
    if oneButEleven(n) then One else if few(n) then Few else Many

  /** One, few and other, by the last digits, as Croatian and Serbian count. */
  def southSlavic(n: Long): Plural =
    if oneButEleven(n) then One else if few(n) then Few else Other

  /** Every count alike, as Chinese, Japanese and their kin count. */
  def invariable: Rule = _ => Other

  /** A whole number of millions takes `de` in French, Spanish and kin. */
  private def millions(n: Long): Plural =
    if n != 0 && n % 1000000 == 0 then Many else Other

  /** Ending in 1, but not in 11. */
  private def oneButEleven(n: Long): Boolean = n % 10 == 1 && n % 100 != 11

  /** Ending in 2 to 4, but not in 12 to 14. */
  private[megalingual] def few(n: Long): Boolean = n % 10 >= 2 && n % 10 <= 4 &&
    !teen(n, 12, 14)

  /** Whether its last two digits are from `from` to `to`. */
  private[megalingual] def teen(n: Long, from: Int, to: Int): Boolean =
    n % 100 >= from && n % 100 <= to
