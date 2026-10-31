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
      Localised.only(French, German)(SwissStandardGerman),
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
    "Listed languages add every language with two varieties holding supported ones",
  ):
    assertEquals(
      localised.listed.toSet,
      localised.languages.toSet + English,
    )
    assertEquals(
      Localised.only(BritishEnglish, French).listed,
      List(BritishEnglish, French),
    )
    assertEquals(
      Localised.only(TaiwaneseChinese, Cantonese).listed,
      List(
        TraditionalChinese,
        TaiwaneseChinese,
        Cantonese,
      ),
    )
    assertEquals(
      Localised.only(SimplifiedChinese, Cantonese).listed,
      List(Chinese, SimplifiedChinese, Cantonese),
    )

  test("A language is read in itself, a variety, or its parent's nearest"):
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
    val chinese = Localised.only(SimplifiedChinese, TaiwaneseChinese)
    assertEquals(
      chinese.resolve(Cantonese),
      TaiwaneseChinese,
    )
    assertEquals(
      chinese.negotiate(None, fromTags(List("zh-Hant-HK"))),
      TaiwaneseChinese,
    )
    assertEquals(
      Localised.only(SimplifiedChinese, Cantonese).resolve(TaiwaneseChinese),
      Cantonese,
    )
    assertEquals(
      Localised.only(SimplifiedChinese).resolve(Cantonese),
      SimplifiedChinese,
    )
    assertEquals(
      Localised.only(GermanGerman, SwissStandardGerman).resolve(SwissGerman),
      SwissStandardGerman,
    )
    assertEquals(
      Localised.only(GermanGerman, SwissGerman).resolve(SwissStandardGerman),
      SwissGerman,
    )
    assertEquals(
      Localised.only(GermanGerman).resolve(SwissGerman),
      GermanGerman,
    )
    assertEquals(
      Localised
        .only(
          GermanGerman,
          SwissStandardGerman,
          SwissGerman,
        )
        .negotiate(None, fromTags(List("de-CH"))),
      SwissStandardGerman,
    )

  test(
    "What the app has is found as the language is read, made only once it is read",
  ):
    assertEquals(localised(AmericanEnglish), "en-US")
    assertEquals(
      localised(SwissStandardGerman),
      "de-DE",
    )
    assertEquals(localised(Korean), "en-GB")
    var made    = 0
    val counted = Localised(French):
      case French =>
        made += 1
        "fr"
    assertEquals(made, 0)
    assertEquals(counted(French), "fr")
    assertEquals(made, 1)

  test("A language the reader names comes before another's parent or sibling"):
    assertEquals(
      Localised
        .only(SimplifiedChinese, TaiwaneseChinese)
        .negotiate(None, List(Cantonese, TaiwaneseChinese)),
      TaiwaneseChinese,
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
      localised.negotiate(None, List(SwissStandardGerman)),
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
          List(TaiwaneseChinese, SimplifiedChinese),
        ),
      SimplifiedChinese,
    )
    assertEquals(
      Localised
        .only(SimplifiedChinese, TaiwaneseChinese)
        .negotiate(
          None,
          List(Cantonese, SimplifiedChinese),
        ),
      SimplifiedChinese,
    )
    assertEquals(
      Localised
        .only(SimplifiedChinese, TaiwaneseChinese)
        .negotiate(None, List(Cantonese, English)),
      TaiwaneseChinese,
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
