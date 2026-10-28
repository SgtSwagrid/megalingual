package com.alecdorrington.megalingual

import com.alecdorrington.megalingual.Language.*
import com.alecdorrington.megalingual.Plural.*
import munit.FunSuite

class PluralSuite extends FunSuite:

  // Each rule as counts and their categories, chosen so that no other rule
  // categorises them all alike, and a language given the wrong rule fails.

  /** One and other, as English counts, for negative numbers too. */
  private val oneOrOther = List(
    0L         -> Other,
    1L         -> One,
    2L         -> Other,
    -1L        -> One,
    1_000_000L -> Other,
  )

  /** Zero as one, and whole millions apart, as French counts. */
  private val zeroAsOne = List(
    0L             -> One,
    1L             -> One,
    2L             -> Other,
    1_000_000L     -> Many,
    1_000_001L     -> Other,
    3_000_000_000L -> Many,
  )

  /** One alone, and whole millions apart, as Spanish counts. */
  private val oneAndMillions = List(
    0L         -> Other,
    1L         -> One,
    2L         -> Other,
    2_000_000L -> Many,
  )

  /** Zero as one, and no millions apart, as Hindi counts. */
  private val zeroOrOne = List(
    0L         -> One,
    1L         -> One,
    2L         -> Other,
    1_000_000L -> Other,
  )

  /** By the last digit, as Filipino counts. */
  private val filipino = List(
    0L  -> One,
    1L  -> One,
    4L  -> Other,
    5L  -> One,
    6L  -> Other,
    9L  -> Other,
    14L -> Other,
  )

  /** One, two and other, as Hebrew counts. */
  private val dual = List(1L -> One, 2L -> Two, 3L -> Other)

  /** All six categories, as Arabic counts. */
  private val arabic = List(
    0L   -> Zero,
    1L   -> One,
    2L   -> Two,
    3L   -> Few,
    10L  -> Few,
    11L  -> Many,
    99L  -> Many,
    100L -> Other,
    102L -> Other,
    103L -> Few,
  )

  /** One, few up to four, and other, as Czech and Slovak count. */
  private val westSlavic = List(
    1L  -> One,
    2L  -> Few,
    4L  -> Few,
    5L  -> Other,
    22L -> Other,
  )

  /** One, few and many, as Polish counts. */
  private val polish = List(
    0L   -> Many,
    1L   -> One,
    2L   -> Few,
    4L   -> Few,
    5L   -> Many,
    11L  -> Many,
    12L  -> Many,
    13L  -> Many,
    14L  -> Many,
    21L  -> Many,
    22L  -> Few,
    112L -> Many,
    122L -> Few,
  )

  /** By the last digits, as Russian and Ukrainian count. */
  private val eastSlavic = List(
    0L              -> Many,
    1L              -> One,
    11L             -> Many,
    12L             -> Many,
    13L             -> Many,
    14L             -> Many,
    21L             -> One,
    22L             -> Few,
    25L             -> Many,
    111L            -> Many,
    10_000_000_021L -> One,
  )

  /** By the last digits, as Croatian and Serbian count. */
  private val southSlavic = List(
    1L  -> One,
    11L -> Other,
    12L -> Other,
    21L -> One,
    22L -> Few,
    25L -> Other,
  )

  /** By the last two digits, as Slovenian counts. */
  private val slovenian = List(
    1L   -> One,
    101L -> One,
    2L   -> Two,
    3L   -> Few,
    4L   -> Few,
    5L   -> Other,
    11L  -> Other,
  )

  /** Tens and teens as zero, as Latvian counts. */
  private val latvian = List(
    0L  -> Zero,
    1L  -> One,
    2L  -> Other,
    10L -> Zero,
    11L -> Zero,
    19L -> Zero,
    21L -> One,
  )

  /** One, few and other, teens apart, as Lithuanian counts. */
  private val lithuanian = List(
    1L  -> One,
    2L  -> Few,
    9L  -> Few,
    10L -> Other,
    11L -> Other,
    21L -> One,
  )

  /** Zero and teens as few, as Romanian counts. */
  private val romanian = List(
    0L   -> Few,
    1L   -> One,
    2L   -> Few,
    19L  -> Few,
    20L  -> Other,
    101L -> Few,
    120L -> Other,
  )

  /** No plural, as Chinese, Japanese and their kin count. */
  private val noPlural = List(0L -> Other, 1L -> Other, 2L -> Other)

  test("Every language has a category for every count"):
    for
      language <- Language.values
      count    <- -3 to 1_000_001 by 997
    do language.plural(count)

  test(
    "Every language categorises counts by its own rule, as CLDR does, negative ones and those beyond an Int too",
  ):
    val rules = List(
      Arabic               -> arabic,
      Bengali              -> zeroOrOne,
      Bulgarian            -> oneOrOther,
      Catalan              -> oneAndMillions,
      Chinese              -> noPlural,
      SimplifiedChinese    -> noPlural,
      TraditionalChinese   -> noPlural,
      Cantonese            -> noPlural,
      Croatian             -> southSlavic,
      Czech                -> westSlavic,
      Danish               -> oneOrOther,
      Dutch                -> oneOrOther,
      English              -> oneOrOther,
      BritishEnglish       -> oneOrOther,
      AmericanEnglish      -> oneOrOther,
      AustralianEnglish    -> oneOrOther,
      Estonian             -> oneOrOther,
      Filipino             -> filipino,
      Finnish              -> oneOrOther,
      French               -> zeroAsOne,
      German               -> oneOrOther,
      GermanGerman         -> oneOrOther,
      SwissGerman          -> oneOrOther,
      Greek                -> oneOrOther,
      Hebrew               -> dual,
      Hindi                -> zeroOrOne,
      Hungarian            -> oneOrOther,
      Indonesian           -> noPlural,
      Italian              -> oneAndMillions,
      Japanese             -> noPlural,
      Korean               -> noPlural,
      Latvian              -> latvian,
      Lithuanian           -> lithuanian,
      Malay                -> noPlural,
      Norwegian            -> oneOrOther,
      Persian              -> zeroOrOne,
      Polish               -> polish,
      Portuguese           -> zeroAsOne,
      BrazilianPortuguese  -> zeroAsOne,
      EuropeanPortuguese   -> oneAndMillions,
      Romanian             -> romanian,
      Russian              -> eastSlavic,
      Serbian              -> southSlavic,
      Slovak               -> westSlavic,
      Slovenian            -> slovenian,
      Spanish              -> oneAndMillions,
      EuropeanSpanish      -> oneAndMillions,
      LatinAmericanSpanish -> oneAndMillions,
      Swahili              -> oneOrOther,
      Swedish              -> oneOrOther,
      Tamil                -> oneOrOther,
      Thai                 -> noPlural,
      Turkish              -> oneOrOther,
      Ukrainian            -> eastSlavic,
      Urdu                 -> oneOrOther,
      Vietnamese           -> noPlural,
    )
    assertEquals(
      rules.map(_._1),
      Language.values.toList,
    )
    rules.foreach((language, rule) =>
      rule.foreach((count, category) =>
        assertEquals(
          language.plural(count),
          category,
          s"$language, $count",
        ),
      ),
    )
