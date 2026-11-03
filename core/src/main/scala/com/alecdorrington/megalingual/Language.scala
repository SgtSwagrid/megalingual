package com.alecdorrington.megalingual

import io.circe.{Codec, Decoder, Encoder, KeyDecoder, KeyEncoder}

/**
  * A language, perhaps a variety of another.
  *
  * Languages form a tree of any depth: a language's `parent` is the broader
  * language it is a variety of (British English of English), and nothing is
  * read from a code's shape. A broader language is read in its first variety
  * where a more specific one is needed.
  *
  * Each language is an object saying all there is of it, a [[Language.Root]] or
  * a [[Language.Variety]], which takes from its parent what it does not say.
  * Megalingual's are all there are, each in a file of its own in the
  * `languages` package: [[Language.all]] lists them, and this companion holds
  * each as a language (`Language.German`).
  *
  * An app supporting only some languages says so with a [[Localised]], and
  * never matches on every language itself, so that a language added breaks no
  * app.
  */
trait Language:

  /**
    * The language's BCP 47 tag, as specific as tells it from the others (`en`,
    * `en-GB`, `es-419`). A variety's need not begin with its parent's.
    */
  def code: String

  /** The language this is a variety of, if any. */
  def parent: Option[Language]

  /** The direction the language is written in. */
  def direction: Direction

  /**
    * The language's flag. A language spoken in one country takes its flag; one
    * with varieties, a tile of its code, telling it apart from them
    * ([[Flag.tile]]). It is a `lazy val` or a `def`, so that it is drawn only
    * when first shown, and code showing no flag links none.
    */
  def flag: Flag

  /**
    * The language's plural rule, by Unicode's CLDR cardinal rules for numbers
    * with no fraction digits (see [[plural]]). It is a `def`, so that code
    * counting nothing links none.
    */
  def plurals: Plural.Rule

  /**
    * The names the language gives languages, itself among them, each as a
    * native writer of the language writes it mid-sentence (`anglais` in
    * French), no two alike. A variety gives only those it gives otherwise than
    * its parent, whose others it takes. It is a `def`, so that code naming
    * nothing links none.
    */
  def names: PartialFunction[Language, String]

  /**
    * The tags besides its code that choose this language where the tree would
    * choose another, or none: `es-MX` would find Spanish, and `no` nothing.
    * None by default.
    */
  def claims: List[String] = Nil

  /**
    * How the language writes its parent's text, by default as its parent does
    * ([[Respelling.none]]), so that a variety takes the respellings of the
    * languages it is a variety of (see [[respell]]). It is a `def`, so that
    * code respelling nothing links none.
    */
  def respelling: Respelling = Respelling.none

  /**
    * Names the language in another, as written mid-sentence there (`anglais` in
    * French): as the other, or the nearest language it is a variety of, names
    * it. Every language names every one, so its code stands in only for a name
    * missing.
    *
    * @param in
    *   The language to name it in.
    *
    * @return
    *   A name for the language.
    */
  final def name(in: Language): String = in
    .lineage
    .iterator
    .flatMap(_.names.lift(this))
    .nextOption()
    .getOrElse(code)

  /**
    * Names the language in another, as it begins a menu's item or a heading
    * there (`Anglais` in French): [[name]] with its first letter in upper case.
    *
    * @param in
    *   The language to name it in.
    *
    * @return
    *   A name for the language.
    */
  final def title(in: Language): String = name(in).capitalize

  /**
    * Finds the plural category of a whole number in the language, by its
    * [[plurals]] (`22` is [[Plural.Few]] in Polish).
    *
    * @param count
    *   The number of things counted.
    *
    * @return
    *   A category, which the language words its own way.
    */
  final def plural(count: Long): Plural = plurals(math.abs(count))

  /**
    * Writes text as this language writes it, from as a language it belongs to
    * writes it: respelt by each language on the way down from that one to this
    * one, in turn (Swiss Standard German writes German's `Straße` as
    * `Strasse`).
    *
    * @param text
    *   The text, as `ancestor` writes it.
    *
    * @param ancestor
    *   This language, or one it is a variety of.
    *
    * @return
    *   The text as this language writes it.
    */
  final def respell(text: String, ancestor: Language): String =
    require(
      within(ancestor),
      s"$this is $ancestor or a variety of it.",
    )
    lineage
      .takeWhile(_ != ancestor)
      .reverse
      .foldLeft(text)((text, language) => language.respelling(text))

  /** The languages this is a variety of, the nearest first. */
  final def ancestors: List[Language] = parent.toList.flatMap(_.lineage)

  /** This language, then each language it is a variety of, nearest first. */
  final def lineage: List[Language] = this :: ancestors

  /**
    * The direct varieties, in [[Language.all]]'s order, the first standing in
    * for it.
    */
  final def children: List[Language] = Language
    .all
    .filter(_.parent.contains(this))

  /** Every variety of this language, however deep, each before its own. */
  final def descendants: List[Language] =
    children.flatMap(child => child :: child.descendants)

  /**
    * Whether this language is the given one, or a variety of it.
    *
    * @param language
    *   The broader language.
    */
  final def within(language: Language): Boolean = lineage.contains(language)

  /**
    * The variety that the language's words are said in, as a broader language
    * is read in its first variety where a more specific one is needed: German
    * in German German, and Chinese in Simplified Chinese.
    *
    * @return
    *   Its first variety, through each level, or itself if it has none.
    */
  final def spoken: Language = children.headOption.fold(this)(_.spoken)

  /**
    * How far this language is from another in the tree: how many steps up from
    * this to the nearest language that both are within, then down to the other,
    * as British English is two from American English.
    *
    * @param other
    *   The other language.
    *
    * @return
    *   The steps, if both are of one tree.
    */
  final def steps(other: Language): Option[Int] =
    val up = lineage.indexWhere(other.within)
    Option.when(up >= 0)(up + other.lineage.indexOf(lineage(up)))

  /** The language's code. */
  override def toString: String = code

object Language:

  // Megalingual's own languages, each as a language rather than an object of
  // its own, so that what holds several is inferred to hold languages, as
  // `Set(German, French)` a `Set[Language]`.

  val Arabic: Language               = languages.Arabic
  val Bengali: Language              = languages.Bengali
  val Bulgarian: Language            = languages.Bulgarian
  val Catalan: Language              = languages.Catalan
  val Chinese: Language              = languages.Chinese
  val SimplifiedChinese: Language    = languages.SimplifiedChinese
  val TraditionalChinese: Language   = languages.TraditionalChinese
  val TaiwaneseChinese: Language     = languages.TaiwaneseChinese
  val Cantonese: Language            = languages.Cantonese
  val Croatian: Language             = languages.Croatian
  val Czech: Language                = languages.Czech
  val Danish: Language               = languages.Danish
  val Dutch: Language                = languages.Dutch
  val English: Language              = languages.English
  val BritishEnglish: Language       = languages.BritishEnglish
  val AmericanEnglish: Language      = languages.AmericanEnglish
  val AustralianEnglish: Language    = languages.AustralianEnglish
  val Estonian: Language             = languages.Estonian
  val Filipino: Language             = languages.Filipino
  val Finnish: Language              = languages.Finnish
  val French: Language               = languages.French
  val German: Language               = languages.German
  val GermanGerman: Language         = languages.GermanGerman
  val GermanOfSwitzerland: Language  = languages.GermanOfSwitzerland
  val SwissStandardGerman: Language  = languages.SwissStandardGerman
  val SwissGerman: Language          = languages.SwissGerman
  val Greek: Language                = languages.Greek
  val Hebrew: Language               = languages.Hebrew
  val Hindi: Language                = languages.Hindi
  val Hungarian: Language            = languages.Hungarian
  val Indonesian: Language           = languages.Indonesian
  val Italian: Language              = languages.Italian
  val Japanese: Language             = languages.Japanese
  val Korean: Language               = languages.Korean
  val Latvian: Language              = languages.Latvian
  val Lithuanian: Language           = languages.Lithuanian
  val Malay: Language                = languages.Malay
  val Norwegian: Language            = languages.Norwegian
  val Persian: Language              = languages.Persian
  val Polish: Language               = languages.Polish
  val Portuguese: Language           = languages.Portuguese
  val BrazilianPortuguese: Language  = languages.BrazilianPortuguese
  val EuropeanPortuguese: Language   = languages.EuropeanPortuguese
  val Romanian: Language             = languages.Romanian
  val Russian: Language              = languages.Russian
  val Serbian: Language              = languages.Serbian
  val Slovak: Language               = languages.Slovak
  val Slovenian: Language            = languages.Slovenian
  val Spanish: Language              = languages.Spanish
  val EuropeanSpanish: Language      = languages.EuropeanSpanish
  val LatinAmericanSpanish: Language = languages.LatinAmericanSpanish
  val Swahili: Language              = languages.Swahili
  val Swedish: Language              = languages.Swedish
  val Tamil: Language                = languages.Tamil
  val Thai: Language                 = languages.Thai
  val Turkish: Language              = languages.Turkish
  val Ukrainian: Language            = languages.Ukrainian
  val Urdu: Language                 = languages.Urdu
  val Vietnamese: Language           = languages.Vietnamese

  /**
    * Every language, in alphabetical order of their English names, each variety
    * after its parent. It follows the aliases, which would be unset before
    * them.
    */
  val all: List[Language] = List(
    Arabic,
    Bengali,
    Bulgarian,
    Catalan,
    Chinese,
    SimplifiedChinese,
    TraditionalChinese,
    TaiwaneseChinese,
    Cantonese,
    Croatian,
    Czech,
    Danish,
    Dutch,
    English,
    BritishEnglish,
    AmericanEnglish,
    AustralianEnglish,
    Estonian,
    Filipino,
    Finnish,
    French,
    German,
    GermanGerman,
    GermanOfSwitzerland,
    SwissStandardGerman,
    SwissGerman,
    Greek,
    Hebrew,
    Hindi,
    Hungarian,
    Indonesian,
    Italian,
    Japanese,
    Korean,
    Latvian,
    Lithuanian,
    Malay,
    Norwegian,
    Persian,
    Polish,
    Portuguese,
    BrazilianPortuguese,
    EuropeanPortuguese,
    Romanian,
    Russian,
    Serbian,
    Slovak,
    Slovenian,
    Spanish,
    EuropeanSpanish,
    LatinAmericanSpanish,
    Swahili,
    Swedish,
    Tamil,
    Thai,
    Turkish,
    Ukrainian,
    Urdu,
    Vietnamese,
  )

  /**
    * A language that is no variety of another. It gives its flag, plural rule
    * and names as members. Only Megalingual makes one.
    *
    * @param code
    *   The language's BCP 47 tag.
    *
    * @param direction
    *   The direction the language is written in.
    */
  abstract class Root private[megalingual] (
    override val code: String,
    override val direction: Direction,
  ) extends Language:

    override final val parent: Option[Language] = None

  /**
    * A variety of another language, written and counted as it is, and naming
    * languages as it does but where it gives names of its own. It gives its
    * flag as a member, and overrides any other where it differs. Only
    * Megalingual makes one.
    *
    * @param code
    *   The variety's BCP 47 tag.
    *
    * @param of
    *   The language it is a variety of.
    */
  abstract class Variety private[megalingual] (
    override val code: String,
    of: Language,
  ) extends Language:

    override final val parent: Option[Language] = Some(of)

    override def direction: Direction = of.direction

    override def plurals: Plural.Rule = of.plurals

    override def names: PartialFunction[Language, String] =
      PartialFunction.empty

  /** The languages that are no variety of another, in their order. */
  def roots: List[Language] = all.filter(_.parent.isEmpty)

  /**
    * Finds the language with exactly the given code.
    *
    * @param code
    *   The language's code, as [[Language.code]] gives it.
    *
    * @return
    *   A language, if one has the code.
    */
  def fromCode(code: String): Option[Language] = all.find(_.code == code)

  /**
    * Finds the language a BCP 47 tag names, as a browser or a `lang` attribute
    * gives it, ignoring case: the language with the whole tag as its code, else
    * the one claiming it, else the language with the longest prefix of it
    * (`de-AT` finds German, `es-MX` Latin American Spanish).
    *
    * @param tag
    *   The tag, its parts joined by `-` or `_`.
    *
    * @return
    *   A language, if the tag names one.
    */
  def fromTag(tag: String): Option[Language] =
    val parts = tag.trim.split("[-_]").toList.filter(_.nonEmpty)
    (parts.size to 1 by -1)
      .iterator
      .map(parts.take(_).mkString("-"))
      .flatMap(tagged)
      .nextOption()

  /** The language with a tag as its code, else the one claiming it. */
  private def tagged(tag: String): Option[Language] = all
    .find(_.code.equalsIgnoreCase(tag))
    .orElse(all.find(_.claims.exists(_.equalsIgnoreCase(tag))))

  /**
    * Finds the languages that BCP 47 tags name, such as a browser's
    * preferences.
    *
    * @param tags
    *   The tags, most preferred first.
    *
    * @return
    *   The languages named, in the tags' order, each once.
    */
  def fromTags(tags: Seq[String]): List[Language] = tags
    .flatMap(fromTag)
    .distinct
    .toList

  /** Kept as its code, refusing any other. */
  given Codec[Language] = Codec.from(
    Decoder
      .decodeString
      .emap(code => fromCode(code).toRight(s"No language has the code $code.")),
    Encoder.encodeString.contramap(_.code),
  )

  /** A map's key kept as its code. */
  given KeyEncoder[Language] = KeyEncoder.encodeKeyString.contramap(_.code)

  /** A map's key read from its code, refusing any other. */
  given KeyDecoder[Language] = KeyDecoder.instance(fromCode)
