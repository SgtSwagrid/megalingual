package com.alecdorrington.megalingual
package laminar

import com.alecdorrington.megalingual.Language.*
import com.alecdorrington.megalingual.laminar.LanguagePicker.{Choice, Naming}
import munit.FunSuite

class LanguagePickerSuite extends FunSuite:

  private val inEnglish = Naming(English, ownFirst = false, none = "None")

  test("Varieties follow their nearest offered ancestor, one level deeper"):
    assertEquals(
      LanguagePicker.choices(
        List(
          French,
          BritishEnglish,
          English,
          AmericanEnglish,
          SwissGerman,
        ),
        inEnglish,
      ),
      List(
        Choice(Some(English), 0),
        Choice(Some(AmericanEnglish), 1),
        Choice(Some(BritishEnglish), 1),
        Choice(Some(French), 0),
        Choice(Some(SwissGerman), 0),
      ),
    )

  test("Siblings are sorted by the names leading them"):
    val languages = List(German, Japanese, French)
    assertEquals(
      LanguagePicker.choices(languages, inEnglish).flatMap(_.language),
      List(French, German, Japanese),
    )
    assertEquals(
      LanguagePicker
        .choices(
          languages,
          inEnglish.copy(ownFirst = true),
        )
        .flatMap(_.language),
      List(German, French, Japanese),
    )

  test("A language leads with one name and shows the other aside"):
    assertEquals(inEnglish.lead(French), "French")
    assertEquals(
      inEnglish.aside(French),
      Some(French.title(French)),
    )
    assertEquals(inEnglish.aside(English), None)
    assertEquals(inEnglish.lead(None), "None")

  test("Lists are joined as the language lists things"):
    assertEquals(
      Intl.listed(List("A", "B", "C"), German),
      "A, B und C",
    )
    assertEquals(Intl.listed(List("A"), German), "A")

  test("Names are sorted as the language orders words"):
    assertEquals(
      Intl.sorted(
        List("Österreich", "Zypern", "Ost"),
        German,
      )(identity),
      List("Ost", "Österreich", "Zypern"),
    )
