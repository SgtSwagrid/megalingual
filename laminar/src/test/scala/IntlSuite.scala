package com.alecdorrington.megalingual
package laminar

import com.alecdorrington.megalingual.Language.*
import munit.FunSuite

class IntlSuite extends FunSuite:

  test("A region is named as its language names it, else by its code"):
    assertEquals(Intl.region("CH", German), "Schweiz")
    assertEquals(Intl.region("CH", French), "Suisse")
    assertEquals(
      Intl.region("CH", GermanGerman),
      "Schweiz",
    )
    assertEquals(Intl.region("C", English), "C")
