package com.alecdorrington.megalingual

import com.alecdorrington.megalingual.Language.*
import munit.FunSuite

class LocalisedSuite extends FunSuite:

  private val localised = Localised(BritishEnglish):
    case BritishEnglish  => "en-GB"
    case AmericanEnglish => "en-US"
    case French          => "fr"
    case GermanGerman    => "de-DE"

  test(
    "The supported languages are those the function is defined for, the default among them, or those given to only and all",
  ):
    assertEquals(
      localised.languages.toSet,
      Set(
        BritishEnglish,
        AmericanEnglish,
        French,
        GermanGerman,
      ),
    )
    assertEquals(localised.default, BritishEnglish)
    intercept[IllegalArgumentException](Localised(Japanese) { case French =>
      1
    })
    assertEquals(
      Localised.only(French, German)(SwissGerman),
      German,
    )
    assertEquals(
      Localised.all(English).languages,
      Language.values.toList,
    )
    assertEquals(
      Localised.all(English)(Cantonese),
      Cantonese,
    )

  test(
    "Listed languages add every language two supported ones are varieties of",
  ):
    assertEquals(
      localised.listed.toSet,
      localised.languages.toSet + English,
    )
    assertEquals(
      Localised.only(BritishEnglish, French).listed,
      List(BritishEnglish, French),
    )

  test(
    "A language is read in itself, a variety, one in the same script, or its parent's nearest",
  ):
    assertEquals(localised.resolve(French), French)
    assertEquals(
      localised.resolve(English),
      BritishEnglish,
    )
    assertEquals(
      localised.resolve(AustralianEnglish),
      BritishEnglish,
    )
    assertEquals(localised.nearest(Japanese), None)
    assertEquals(
      localised.resolve(Japanese),
      BritishEnglish,
    )
    val chinese = Localised.only(SimplifiedChinese, TraditionalChinese)
    assertEquals(
      chinese.resolve(Cantonese),
      TraditionalChinese,
    )
    assertEquals(
      chinese.negotiate(None, fromTags(List("zh-Hant-HK"))),
      TraditionalChinese,
    )
    assertEquals(
      Localised.only(SimplifiedChinese, Cantonese).resolve(TraditionalChinese),
      Cantonese,
    )
    assertEquals(
      Localised.only(SimplifiedChinese).resolve(Cantonese),
      SimplifiedChinese,
    )

  test(
    "What the app has is found as the language is read, made only once it is read",
  ):
    assertEquals(localised(AmericanEnglish), "en-US")
    assertEquals(localised(SwissGerman), "de-DE")
    assertEquals(localised(Korean), "en-GB")
    var made    = 0
    val counted = Localised(French):
      case French =>
        made += 1
        "fr"
    assertEquals(made, 0)
    assertEquals(counted(French), "fr")
    assertEquals(made, 1)

  test(
    "A language the reader names comes before another's parent, and before one in the same script",
  ):
    assertEquals(
      Localised
        .only(SimplifiedChinese, TraditionalChinese)
        .negotiate(
          None,
          List(Cantonese, TraditionalChinese),
        ),
      TraditionalChinese,
    )
    assertEquals(
      Localised
        .only(BritishEnglish, AmericanEnglish)
        .negotiate(
          None,
          List(AustralianEnglish, AmericanEnglish),
        ),
      AmericanEnglish,
    )
    assertEquals(
      Localised
        .only(BritishEnglish, French)
        .negotiate(None, List(AustralianEnglish, French)),
      French,
    )
    assertEquals(
      localised.negotiate(None, List(SwissGerman)),
      GermanGerman,
    )
    assertEquals(
      localised.negotiate(Some(AustralianEnglish), List(French)),
      BritishEnglish,
    )
    assertEquals(
      Localised
        .only(SimplifiedChinese, Cantonese)
        .negotiate(
          None,
          List(TraditionalChinese, SimplifiedChinese),
        ),
      SimplifiedChinese,
    )
    assertEquals(
      Localised
        .only(SimplifiedChinese, TraditionalChinese)
        .negotiate(
          None,
          List(Cantonese, SimplifiedChinese),
        ),
      SimplifiedChinese,
    )
    assertEquals(
      Localised
        .only(SimplifiedChinese, TraditionalChinese)
        .negotiate(None, List(Cantonese, English)),
      TraditionalChinese,
    )

  test(
    "A choice comes before preferences, an unsupported one is passed, and a broader one is read in the variety the reader prefers",
  ):
    assertEquals(
      localised.negotiate(Some(French), List(GermanGerman)),
      French,
    )
    assertEquals(
      localised.negotiate(Some(Korean), List(GermanGerman)),
      GermanGerman,
    )
    assertEquals(
      localised.negotiate(None, List(Japanese, French)),
      French,
    )
    assertEquals(
      localised.negotiate(None, Nil),
      BritishEnglish,
    )
    assertEquals(
      localised.negotiate(None, List(English, AmericanEnglish)),
      AmericanEnglish,
    )
    assertEquals(
      localised.negotiate(Some(English), List(AmericanEnglish)),
      AmericanEnglish,
    )
    assertEquals(
      localised.negotiate(Some(English), Nil),
      BritishEnglish,
    )
