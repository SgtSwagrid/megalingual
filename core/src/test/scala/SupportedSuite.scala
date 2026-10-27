package com.alecdorrington.megalingual

import com.alecdorrington.megalingual.Language.*
import munit.FunSuite

class SupportedSuite extends FunSuite:

  private val supported = Supported(BritishEnglish):
    case BritishEnglish  => "en-GB"
    case AmericanEnglish => "en-US"
    case French          => "fr"
    case GermanGerman    => "de-DE"

  test("The supported languages are those the function is defined for"):
    assertEquals(
      supported.languages.toSet,
      Set(
        BritishEnglish,
        AmericanEnglish,
        French,
        GermanGerman,
      ),
    )
    assertEquals(supported.default, BritishEnglish)

  test("Listed languages add every language a supported one is a variety of"):
    assertEquals(
      supported.listed.toSet,
      supported.languages.toSet ++ Set(English, German),
    )

  test("A language is read in itself, a variety, or its parent's nearest"):
    assertEquals(supported.resolve(French), French)
    assertEquals(
      supported.resolve(English),
      BritishEnglish,
    )
    assertEquals(
      supported.resolve(AustralianEnglish),
      BritishEnglish,
    )
    assertEquals(
      supported.resolve(SwissGerman),
      GermanGerman,
    )
    assertEquals(supported.nearest(Japanese), None)
    assertEquals(
      supported.resolve(Japanese),
      BritishEnglish,
    )

  test("What the app has is found as the language is read"):
    assertEquals(supported(AmericanEnglish), "en-US")
    assertEquals(supported(SwissGerman), "de-DE")
    assertEquals(supported(Korean), "en-GB")

  test("A choice comes before preferences, and an unsupported one is passed"):
    assertEquals(
      supported.negotiate(Some(French), List(GermanGerman)),
      French,
    )
    assertEquals(
      supported.negotiate(Some(Korean), List(GermanGerman)),
      GermanGerman,
    )
    assertEquals(
      supported.negotiate(None, List(Japanese, French)),
      French,
    )
    assertEquals(
      supported.negotiate(None, Nil),
      BritishEnglish,
    )

  test("A broader language is read in the variety the reader prefers"):
    assertEquals(
      supported.negotiate(None, List(English, AmericanEnglish)),
      AmericanEnglish,
    )
    assertEquals(
      supported.negotiate(Some(English), List(AmericanEnglish)),
      AmericanEnglish,
    )
    assertEquals(
      supported.negotiate(Some(English), Nil),
      BritishEnglish,
    )

  test("What the app has in a language is made only once it is read"):
    var made    = 0
    val counted = Supported(French):
      case French =>
        made += 1
        "fr"
    assertEquals(made, 0)
    assertEquals(counted(French), "fr")
    assertEquals(made, 1)

  test("The default must be supported"):
    intercept[IllegalArgumentException](Supported(Japanese) { case French =>
      1
    })

  test("Only and all support languages giving themselves"):
    assertEquals(
      Supported.only(French, German)(SwissGerman),
      German,
    )
    assertEquals(
      Supported.all(English).languages,
      Language.values.toList,
    )
    assertEquals(
      Supported.all(English)(Cantonese),
      Cantonese,
    )
