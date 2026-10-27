package com.alecdorrington.megalingual

import com.alecdorrington.megalingual.Language.*
import com.alecdorrington.megalingual.Plural.*
import munit.FunSuite

class PluralSuite extends FunSuite:

  private def categories(language: Language, counts: Int*): List[Plural] =
    counts.map(language.plural(_)).toList

  test("Every language has a category for every count"):
    for
      language <- Language.values
      count    <- -3 to 1_000_001 by 997
    do language.plural(count)

  test("English has one and other, for negative numbers too"):
    assertEquals(
      categories(English, 0, 1, 2, -1),
      List(Other, One, Other, One),
    )
    assertEquals(AustralianEnglish.plural(1), One)

  test("French counts zero as one, and millions apart"):
    assertEquals(
      categories(French, 0, 1, 2, 1_000_000, 1_000_001),
      List(One, One, Other, Many, Other),
    )

  test("Brazilian and European Portuguese differ on zero"):
    assertEquals(BrazilianPortuguese.plural(0), One)
    assertEquals(Portuguese.plural(0), One)
    assertEquals(EuropeanPortuguese.plural(0), Other)
    assertEquals(
      EuropeanPortuguese.plural(2_000_000),
      Many,
    )

  test("Polish has one, few and many"):
    assertEquals(
      categories(Polish, 0, 1, 2, 4, 5, 12, 21, 22, 112, 122),
      List(
        Many,
        One,
        Few,
        Few,
        Many,
        Many,
        Many,
        Few,
        Many,
        Few,
      ),
    )

  test("Russian and Ukrainian count by the last digits"):
    for language <- List(Russian, Ukrainian) do
      assertEquals(
        categories(language, 1, 11, 21, 22, 25, 111, 0),
        List(One, Many, One, Few, Many, Many, Many),
      )

  test("Croatian and Serbian have one, few and other"):
    for language <- List(Croatian, Serbian) do
      assertEquals(
        categories(language, 1, 21, 22, 25, 11),
        List(One, One, Few, Other, Other),
      )

  test("Czech and Slovak have one, few and other"):
    for language <- List(Czech, Slovak) do
      assertEquals(
        categories(language, 1, 2, 4, 5, 22),
        List(One, Few, Few, Other, Other),
      )

  test("Slovenian counts by the last two digits"):
    assertEquals(
      categories(Slovenian, 1, 101, 2, 3, 4, 5, 11),
      List(One, One, Two, Few, Few, Other, Other),
    )

  test("Arabic has all six categories"):
    assertEquals(
      categories(Arabic, 0, 1, 2, 3, 10, 11, 99, 100, 102, 103),
      List(
        Zero,
        One,
        Two,
        Few,
        Few,
        Many,
        Many,
        Other,
        Other,
        Few,
      ),
    )

  test("Counts beyond an Int are categorised alike"):
    assertEquals(Russian.plural(10_000_000_021L), One)
    assertEquals(French.plural(3_000_000_000L), Many)

  test("Hebrew has a dual"):
    assertEquals(
      categories(Hebrew, 1, 2, 3),
      List(One, Two, Other),
    )

  test("Latvian counts tens and teens as zero"):
    assertEquals(
      categories(Latvian, 0, 1, 2, 10, 11, 19, 21),
      List(Zero, One, Other, Zero, Zero, Zero, One),
    )

  test("Lithuanian has one, few and other"):
    assertEquals(
      categories(Lithuanian, 1, 2, 9, 10, 11, 21),
      List(One, Few, Few, Other, Other, One),
    )

  test("Romanian counts zero and teens as few"):
    assertEquals(
      categories(Romanian, 0, 1, 2, 19, 20, 101, 120),
      List(Few, One, Few, Few, Other, Few, Other),
    )

  test("Filipino counts by its last digit"):
    assertEquals(
      categories(Filipino, 0, 1, 4, 5, 6, 9, 14),
      List(One, One, Other, One, Other, Other, Other),
    )

  test("Hindi counts zero as one"):
    assertEquals(
      categories(Hindi, 0, 1, 2),
      List(One, One, Other),
    )

  test("Chinese, Japanese and their kin have no plural"):
    for language <- List(
        Chinese,
        Cantonese,
        Japanese,
        Korean,
        Thai,
        Malay,
      )
    do
      assertEquals(
        categories(language, 0, 1, 2),
        List(Other, Other, Other),
      )
