package com.alecdorrington.megalingual

import com.alecdorrington.megalingual.Direction.{LeftToRight, RightToLeft}
import com.alecdorrington.megalingual.Language.*
import io.circe.{Decoder, Encoder, Json, KeyDecoder, KeyEncoder}
import munit.FunSuite

class LanguageSuite extends FunSuite:

  private val languages = Language.values.toList

  test(
    "Every language is named in every language, and no two share a name in any",
  ):
    for in <- languages do
      for language <- languages do
        val name = language.name(in)
        assert(name.nonEmpty, s"$language in $in")
        assertEquals(name, name.trim, s"$language in $in")
      val repeated = languages
        .groupBy(_.name(in))
        .collect { case (name, named) if named.size > 1 => name -> named }
      assert(repeated.isEmpty, s"In $in: $repeated")

  test(
    "A language's title is its name with a capital, and Hong Kong names a few languages unlike Taiwan",
  ):
    assertEquals(English.name(English), "English")
    assertEquals(French.title(English), "French")
    assertEquals(
      French.name(French).capitalize,
      French.title(French),
    )
    assertEquals(
      Japanese.title(Japanese),
      Japanese.name(Japanese),
    )
    assertEquals(Italian.name(TaiwaneseChinese), "義大利文")
    assertEquals(Italian.name(Cantonese), "意大利文")
    assertEquals(
      Japanese.name(Cantonese),
      Japanese.name(TaiwaneseChinese),
    )

  test("Codes are distinct, and each, exactly, finds its language"):
    assertEquals(
      languages.map(_.code).distinct.size,
      languages.size,
    )
    for language <- languages do
      assertEquals(
        fromCode(language.code),
        Some(language),
      )
      assertEquals(fromTag(language.code), Some(language))
    assertEquals(fromCode("en-gb"), None)
    assertEquals(fromCode("xx"), None)

  test(
    "A tag finds its longest known prefix, or a language claiming it, and tags find theirs in order, each once",
  ):
    List(
      "en_us"              -> Some(AmericanEnglish),
      "en-US-u-ca-gregory" -> Some(AmericanEnglish),
      "de-AT"              -> Some(German),
      "de-CH"              -> Some(GermanOfSwitzerland),
      "de-LI"              -> Some(SwissStandardGerman),
      "gsw-CH"             -> Some(SwissGerman),
      "es-MX"              -> Some(LatinAmericanSpanish),
      "pt-AO"              -> Some(EuropeanPortuguese),
      "zh-Hant-HK"         -> Some(Cantonese),
      "zh-MO"              -> Some(Cantonese),
      "zh-HK"              -> Some(Cantonese),
      "yue"                -> Some(Cantonese),
      "zh-TW"              -> Some(TaiwaneseChinese),
      "zh-Hant"            -> Some(TraditionalChinese),
      "zh-Hant-TW"         -> Some(TaiwaneseChinese),
      "zh-Hans-SG"         -> Some(Chinese),
      "zh-Hans-HK"         -> Some(Chinese),
      "no"                 -> Some(Norwegian),
      "fil-PH"             -> Some(Filipino),
      " fr "               -> Some(French),
      "xx-YY"              -> None,
      ""                   -> None,
    ).foreach((tag, language) => assertEquals(fromTag(tag), language, tag))
    assertEquals(
      fromTags(List("en-US", "en", "xx", "en-US", "fr")),
      List(AmericanEnglish, English, French),
    )

  test("Varieties form a tree"):
    assert(BritishEnglish.within(English))
    assert(!English.within(BritishEnglish))
    assertEquals(
      English.children.headOption,
      Some(BritishEnglish),
    )
    assertEquals(
      BritishEnglish.lineage,
      List(BritishEnglish, English),
    )
    assert(roots.forall(_.parent.isEmpty))
    assert(roots.contains(Chinese) && !roots.contains(Cantonese))
    assertEquals(
      Cantonese.lineage,
      List(Cantonese, TraditionalChinese, Chinese),
    )
    assertEquals(
      SwissGerman.lineage,
      List(
        SwissGerman,
        GermanOfSwitzerland,
        German,
      ),
    )
    assertEquals(
      SwissStandardGerman.lineage,
      List(
        SwissStandardGerman,
        GermanOfSwitzerland,
        German,
      ),
    )
    for language <- languages do
      assert(language.ancestors.forall(ancestor => language.within(ancestor)))

  test("Arabic, Hebrew, Persian and Urdu are written right to left"):
    assertEquals(
      languages.filter(_.direction == RightToLeft).toSet,
      Set(Arabic, Hebrew, Persian, Urdu),
    )
    assertEquals(English.direction, LeftToRight)

  test(
    "Every language has a flag, as a data URI holding nothing a URI misreads, showing its code exactly when it has varieties",
  ):
    for language <- languages do
      assert(
        language.flag.markup.startsWith("<svg"),
        s"$language",
      )
      val uri = language.flag.dataUri
      assert(
        uri.startsWith("data:image/svg+xml;utf8,%3Csvg"),
        uri,
      )
      assert(
        !uri.exists("#\"<>".contains(_)),
        s"$language",
      )
      assertEquals(
        language.flag.markup.contains("<text"),
        language.children.nonEmpty,
        s"$language",
      )
    assert(English.flag.markup.contains(">EN</text>"))
    assert(Chinese.flag.markup.contains(">ZH</text>"))
    assert(TraditionalChinese.flag.markup.contains(">ZH-HANT</text>"))

  test("A language is kept as its code, a map's key too"):
    assertEquals(
      Encoder[Language].apply(SwissStandardGerman),
      Json.fromString("de-CH-1996"),
    )
    assertEquals(
      Decoder[Language].decodeJson(Json.fromString("de-CH-1996")),
      Right(SwissStandardGerman),
    )
    assert(Decoder[Language].decodeJson(Json.fromString("no")).isLeft)
    assertEquals(
      KeyEncoder[Language].apply(SwissStandardGerman),
      "de-CH-1996",
    )
    assertEquals(
      KeyDecoder[Language].apply("de-CH-1996"),
      Some(SwissStandardGerman),
    )
    assertEquals(KeyDecoder[Language].apply("no"), None)
    assertEquals(
      Decoder[Language].decodeJson(Json.fromString("de-CH")),
      Right(GermanOfSwitzerland),
    )
