package com.alecdorrington.megalingual

import com.alecdorrington.megalingual.Language.*
import munit.FunSuite

/** A variety of Swiss Standard German respelling what it writes again. */
object Respelt
  extends Language.Variety(
    "de-CH-1996-x-sz",
    of = SwissStandardGerman,
  ):

  override lazy val flag: Flag = SwissStandardGerman.flag

  override def respelling: Respelling =
    Respelling(List(Substitution("ss", "sz")))

class RespellingSuite extends FunSuite:

  test(
    "A respelling substitutes in one pass, each point taking the first substitution matching there",
  ):
    assertEquals(
      Respelling(List(
        Substitution("a", "b"),
        Substitution("b", "c"),
      ))("abc"),
      "bcc",
    )
    assertEquals(
      Respelling(List(
        Substitution("ab", "1"),
        Substitution("a", "2"),
      ))("abac"),
      "12c",
    )
    assertEquals(
      Respelling(List(
        Substitution("a", "2"),
        Substitution("ab", "1"),
      ))("abac"),
      "2b2c",
    )
    assertEquals(
      Respelling(List(
        Substitution("b", "x"),
        Substitution("ab", "y"),
      ))("ab"),
      "y",
    )
    assertEquals(Respelling.none("Straße"), "Straße")
    assertEquals(
      Respelling(List(Substitution("x", "y")))(""),
      "",
    )

  test(
    "A replacement names the pattern's groups by `$1`, and makes a character plain by `\\`",
  ):
    val respelling = Respelling(List(
      Substitution("""(\d+) (EUR|CHF)""", "$2 $1"),
      Substitution("""(x)?y""", "[$1]"),
      Substitution("""\$""", """\$1"""),
    ))
    assertEquals(
      respelling("5 EUR, 7 CHF"),
      "EUR 5, CHF 7",
    )
    assertEquals(respelling("xy y"), "[x] []")
    assertEquals(respelling("$"), "$1")
    intercept[IllegalArgumentException](Substitution("a", "$1"))

  test("An empty match is substituted once, before the character at it"):
    assertEquals(
      Respelling(List(Substitution("(?=b)", "-")))("abab"),
      "a-ba-b",
    )
    assertEquals(
      Respelling(List(Substitution("^", "> ")))("ab"),
      "> ab",
    )
    assertEquals(
      Respelling(List(Substitution("$", ".")))("ab"),
      "ab.",
    )
    assertEquals(
      Respelling(List(Substitution("", "-")))("ab"),
      "-a-b-",
    )

  test(
    "Swiss German writes no ß, and its varieties take that, each respelling in turn from the language the text is in",
  ):
    val text = "Die Straße ist GROẞ."
    for language <- List(SwissStandardGerman, SwissGerman) do
      assertEquals(
        language.respell(text, German),
        "Die Strasse ist GROSS.",
      )
    assertEquals(
      SwissStandardGerman.respell(text, GermanOfSwitzerland),
      text,
    )
    assertEquals(
      SwissStandardGerman.respell(text, SwissStandardGerman),
      text,
    )
    assertEquals(
      GermanGerman.respell(text, German),
      text,
    )
    assertEquals(German.respell(text, German), text)
    assertEquals(
      Respelt.respell(text, German),
      "Die Strasze ist GROSS.",
    )
    assertEquals(
      Respelt.respell("Strasse", SwissStandardGerman),
      "Strasze",
    )
    intercept[IllegalArgumentException](German.respell(text, SwissGerman))
    intercept[IllegalArgumentException](French.respell(text, German))
    assertEquals(
      Language.all.filter(_.respelling != Respelling.none),
      List(GermanOfSwitzerland),
    )
