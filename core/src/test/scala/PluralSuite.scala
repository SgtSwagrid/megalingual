package com.alecdorrington.megalingual

import com.alecdorrington.megalingual.Language.*
import com.alecdorrington.megalingual.Plural.*
import munit.FunSuite

class PluralSuite extends FunSuite:

  test("Every language has a category for every count"):
    for
      language <- Language.values
      count    <- -3 to 1_000_001 by 997
    do language.plural(count)

  test(
    "Each plural rule categorises counts as CLDR does, negative ones and those beyond an Int too",
  ):
    List(
      (English, List(0, 1, 2, -1), List(Other, One, Other, One)),
      (
        French,
        List(0, 1, 2, 1_000_000, 1_000_001),
        List(One, One, Other, Many, Other),
      ),
      (EuropeanPortuguese, List(0, 2_000_000), List(Other, Many)),
      (Hindi, List(0, 1, 2), List(One, One, Other)),
      (
        Filipino,
        List(0, 1, 4, 5, 6, 9, 14),
        List(One, One, Other, One, Other, Other, Other),
      ),
      (Hebrew, List(1, 2, 3), List(One, Two, Other)),
      (
        Arabic,
        List(0, 1, 2, 3, 10, 11, 99, 100, 102, 103),
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
      ),
      (Czech, List(1, 2, 4, 5, 22), List(One, Few, Few, Other, Other)),
      (
        Polish,
        List(0, 1, 2, 4, 5, 12, 21, 22, 112, 122),
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
      ),
      (
        Russian,
        List(1, 11, 21, 22, 25, 111, 0),
        List(One, Many, One, Few, Many, Many, Many),
      ),
      (Croatian, List(1, 21, 22, 25, 11), List(One, One, Few, Other, Other)),
      (
        Slovenian,
        List(1, 101, 2, 3, 4, 5, 11),
        List(One, One, Two, Few, Few, Other, Other),
      ),
      (
        Latvian,
        List(0, 1, 2, 10, 11, 19, 21),
        List(Zero, One, Other, Zero, Zero, Zero, One),
      ),
      (
        Lithuanian,
        List(1, 2, 9, 10, 11, 21),
        List(One, Few, Few, Other, Other, One),
      ),
      (
        Romanian,
        List(0, 1, 2, 19, 20, 101, 120),
        List(Few, One, Few, Few, Other, Few, Other),
      ),
      (Chinese, List(0, 1, 2), List(Other, Other, Other)),
    ).foreach((language, counts, categories) =>
      assertEquals(
        counts.map(language.plural(_)),
        categories,
        language,
      ),
    )
    assertEquals(BrazilianPortuguese.plural(0), One)
    assertEquals(Russian.plural(10_000_000_021L), One)
    assertEquals(French.plural(3_000_000_000L), Many)
