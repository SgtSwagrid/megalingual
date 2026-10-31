package com.alecdorrington.megalingual
package laminar

import com.alecdorrington.megalingual.Language.*
import com.alecdorrington.megalingual.laminar.LanguagePicker.{Choice, Naming}
import munit.FunSuite

class LanguagePickerSuite extends FunSuite:

  private val inEnglish = Naming(English, ownFirst = false, none = "None")

  test(
    "Varieties follow their nearest offered ancestor, one level deeper, and siblings are sorted by the names leading them",
  ):
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
        query = "",
      ),
      List(
        Choice(Some(English), 0),
        Choice(Some(AmericanEnglish), 1),
        Choice(Some(BritishEnglish), 1),
        Choice(Some(French), 0),
        Choice(Some(SwissGerman), 0),
      ),
    )
    val languages = List(German, Japanese, French)
    assertEquals(
      LanguagePicker.choices(languages, inEnglish, "").flatMap(_.language),
      List(French, German, Japanese),
    )
    assertEquals(
      LanguagePicker
        .choices(
          languages,
          inEnglish.copy(ownFirst = true),
          "",
        )
        .flatMap(_.language),
      List(German, French, Japanese),
    )

  test(
    "A language leads with one name and shows the other aside, and a letter typed finds either, ignoring accents",
  ):
    assertEquals(inEnglish.lead(French), "French")
    assertEquals(
      inEnglish.aside(French),
      Some(French.title(French)),
    )
    assertEquals(inEnglish.aside(English), None)
    assertEquals(inEnglish.lead(None), "None")
    val ownFirst = inEnglish.copy(ownFirst = true)
    assert(ownFirst.begins(Some(Czech), "c"))
    assert(ownFirst.begins(Some(Czech), "C"))
    assert(inEnglish.begins(Some(Czech), "c"))
    assert(inEnglish.begins(Some(German), "d"))
    assert(!inEnglish.begins(Some(German), "x"))
    assert(inEnglish.begins(None, "n"))
    val inCzech = Naming(Czech, ownFirst = false, none = "Žádný")
    assert(inCzech.begins(Some(Czech), "c"))
    assert(inCzech.begins(None, "z"))

  test(
    "A search finds the languages any of whose names, in any language, holds it, ignoring case and accents",
  ):
    assert(inEnglish.finds(Some(German), "deutsch"))
    assert(inEnglish.finds(Some(German), "LEMAN"))
    assert(inEnglish.finds(Some(German), "ドイツ"))
    assert(inEnglish.finds(Some(Czech), "cestin"))
    assert(inEnglish.finds(Some(German), ""))
    assert(!inEnglish.finds(Some(German), "x"))
    assert(inEnglish.finds(None, "on"))
    assert(!inEnglish.finds(None, "deutsch"))

  test(
    "The languages found keep their tree, and the one made active is the chosen, else the first whose name shown begins with what was typed",
  ):
    val languages = List(
      French,
      Bengali,
      English,
      BritishEnglish,
      AmericanEnglish,
    )
    assertEquals(
      LanguagePicker.choices(languages, inEnglish, "engl"),
      List(
        Choice(Some(English), 0),
        Choice(Some(AmericanEnglish), 1),
        Choice(Some(BritishEnglish), 1),
      ),
    )
    assertEquals(
      LanguagePicker.choices(languages, inEnglish, "brit"),
      List(Choice(Some(BritishEnglish), 0)),
    )
    assertEquals(
      LanguagePicker.choices(languages, inEnglish, "x"),
      Nil,
    )
    val found = List(
      Choice(Some(Bengali), 0),
      Choice(Some(English), 0),
      Choice(Some(BritishEnglish), 1),
    )
    assertEquals(
      LanguagePicker.likeliest(found, inEnglish, "eng", Some(French)),
      1,
    )
    assertEquals(
      LanguagePicker.likeliest(found, inEnglish, "ngl", Some(French)),
      0,
    )
    assertEquals(
      LanguagePicker.likeliest(
        found,
        inEnglish,
        "",
        Some(BritishEnglish),
      ),
      2,
    )
    assertEquals(
      LanguagePicker.likeliest(found, inEnglish, "", Some(French)),
      0,
    )

  test(
    "Lists are joined as the language lists things, and names sorted as it orders words",
  ):
    assertEquals(
      Intl.listed(List("A", "B", "C"), German),
      "A, B und C",
    )
    assertEquals(Intl.listed(List("A"), German), "A")
    assertEquals(
      Intl.sorted(
        List("Österreich", "Zypern", "Ost"),
        German,
      )(identity),
      List("Ost", "Österreich", "Zypern"),
    )
