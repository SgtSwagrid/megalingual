package com.alecdorrington.megalingual

import com.alecdorrington.megalingual.Direction.{LeftToRight, RightToLeft}
import com.alecdorrington.megalingual.Language.*
import io.circe.{Decoder, Encoder, Json, KeyDecoder, KeyEncoder}
import munit.FunSuite

class LanguageSuite extends FunSuite:

  private val languages = Language.values.toList

  test("Every language is named in every language"):
    for
      language <- languages
      in       <- languages
    do
      val name = language.name(in)
      assert(name.nonEmpty, s"$language in $in")
      assertEquals(name, name.trim, s"$language in $in")

  test("No two languages share a name in any language"):
    for in <- languages do
      val repeated = languages
        .groupBy(_.name(in))
        .collect { case (name, named) if named.size > 1 => name -> named }
      assert(repeated.isEmpty, s"In $in: $repeated")

  test("A language's title is its name with a capital"):
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

  test("Codes are distinct, and each finds its language"):
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

  test("Only an exact code finds a language"):
    assertEquals(
      fromCode("en-GB"),
      Some(BritishEnglish),
    )
    assertEquals(fromCode("en-gb"), None)
    assertEquals(fromCode("xx"), None)

  test("A tag finds its longest known prefix, or a language claiming it"):
    assertEquals(
      fromTag("en_us"),
      Some(AmericanEnglish),
    )
    assertEquals(fromTag("de-AT"), Some(German))
    assertEquals(
      fromTag("es-MX"),
      Some(LatinAmericanSpanish),
    )
    assertEquals(
      fromTag("pt-AO"),
      Some(EuropeanPortuguese),
    )
    assertEquals(fromTag("zh-Hant-HK"), Some(Cantonese))
    assertEquals(
      fromTag("zh-Hant"),
      Some(TraditionalChinese),
    )
    assertEquals(fromTag("zh-Hans-SG"), Some(Chinese))
    assertEquals(fromTag("zh-Hans-HK"), Some(Chinese))
    assertEquals(fromTag("zh-MO"), Some(Cantonese))
    assertEquals(
      fromTag("zh-Hant-TW"),
      Some(TraditionalChinese),
    )
    assertEquals(fromTag("no"), Some(Norwegian))
    assertEquals(fromTag("fil-PH"), Some(Filipino))
    assertEquals(fromTag(" fr "), Some(French))
    assertEquals(fromTag("xx-YY"), None)
    assertEquals(fromTag(""), None)

  test("Tags find their languages in order, each once"):
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
    for language <- languages do
      assert(language.ancestors.forall(ancestor => language.within(ancestor)))

  test("Arabic, Hebrew, Persian and Urdu are written right to left"):
    assertEquals(
      languages.filter(_.direction == RightToLeft).toSet,
      Set(Arabic, Hebrew, Persian, Urdu),
    )
    assertEquals(English.direction, LeftToRight)

  test(
    "Every language has a flag, as a data URI holding nothing a URI misreads",
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

  test("A language's flag shows its code exactly when it has varieties"):
    for language <- languages do
      assertEquals(
        language.flag.markup.contains("<text"),
        language.children.nonEmpty,
        s"$language",
      )
    assert(English.flag.markup.contains(">EN</text>"))
    assert(Chinese.flag.markup.contains(">ZH</text>"))

  test("A language is kept as its code"):
    assertEquals(
      Encoder[Language].apply(SwissGerman),
      Json.fromString("de-CH"),
    )
    assertEquals(
      Decoder[Language].decodeJson(Json.fromString("de-CH")),
      Right(SwissGerman),
    )
    assert(Decoder[Language].decodeJson(Json.fromString("no")).isLeft)

  test("A language keys a map by its code"):
    assertEquals(
      KeyEncoder[Language].apply(SwissGerman),
      "de-CH",
    )
    assertEquals(
      KeyDecoder[Language].apply("de-CH"),
      Some(SwissGerman),
    )
    assertEquals(KeyDecoder[Language].apply("no"), None)

  test("Hong Kong names a few languages unlike Taiwan"):
    assertEquals(
      Italian.name(TraditionalChinese),
      "義大利文",
    )
    assertEquals(Italian.name(Cantonese), "意大利文")
    assertEquals(
      Japanese.name(Cantonese),
      Japanese.name(TraditionalChinese),
    )
