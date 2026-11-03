package com.alecdorrington.megalingual

import com.alecdorrington.megalingual.Language.*
import com.alecdorrington.megalingual.Substitution.substituted
import munit.FunSuite

/** A variety of Swiss Standard German respelling what it writes again. */
object Respelt
  extends Language.Variety(
    "de-CH-1996-x-sz",
    of = SwissStandardGerman,
  ):

  override lazy val flag: Flag = SwissStandardGerman.flag

  override def substitutions: List[Substitution] =
    List(Substitution("ss", "sz"))

class SubstitutionSuite extends FunSuite:

  test(
    "Substitutions are made in one pass, each point taking the first matching there",
  ):
    assertEquals(
      substituted(
        "abc",
        List(
          Substitution("a", "b"),
          Substitution("b", "c"),
        ),
      ),
      "bcc",
    )
    assertEquals(
      substituted(
        "abac",
        List(
          Substitution("ab", "1"),
          Substitution("a", "2"),
        ),
      ),
      "12c",
    )
    assertEquals(
      substituted(
        "abac",
        List(
          Substitution("a", "2"),
          Substitution("ab", "1"),
        ),
      ),
      "2b2c",
    )
    assertEquals(
      substituted(
        "ab",
        List(
          Substitution("b", "x"),
          Substitution("ab", "y"),
        ),
      ),
      "y",
    )
    assertEquals(substituted("Straße", Nil), "Straße")
    assertEquals(
      substituted("", List(Substitution("x", "y"))),
      "",
    )

  test(
    "A replacement names the pattern's groups by `$1`, and makes a character plain by `\\`",
  ):
    val substitutions = List(
      Substitution("""(\d+) (EUR|CHF)""", "$2 $1"),
      Substitution("""(x)?y""", "[$1]"),
      Substitution("""\$""", """\$1"""),
    )
    assertEquals(
      substituted("5 EUR, 7 CHF", substitutions),
      "EUR 5, CHF 7",
    )
    assertEquals(
      substituted("xy y", substitutions),
      "[x] []",
    )
    assertEquals(substituted("$", substitutions), "$1")
    intercept[IllegalArgumentException](Substitution("a", "$1"))

  test("An empty match is substituted once, before the character at it"):
    assertEquals(
      substituted(
        "abab",
        List(Substitution("(?=b)", "-")),
      ),
      "a-ba-b",
    )
    assertEquals(
      substituted("ab", List(Substitution("^", "> "))),
      "> ab",
    )
    assertEquals(
      substituted("ab", List(Substitution("$", "."))),
      "ab.",
    )
    assertEquals(
      substituted("ab", List(Substitution("", "-"))),
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
      Language.all.filter(_.substitutions.nonEmpty),
      List(GermanOfSwitzerland),
    )
