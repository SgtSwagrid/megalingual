package com.alecdorrington.megalingual

import com.alecdorrington.megalingual.Direction.LeftToRight
import com.alecdorrington.megalingual.Language.{
  English, French, German, LatinAmericanSpanish,
}

/** Languages another library might add, to test what Megalingual makes of them. */
object Added:

  /** A language Megalingual's own do not name, which names a few itself. */
  object Esperanto extends Language.Root("eo", LeftToRight):

    override lazy val flag: Flag =
      Flag.drawn("""<rect width="60" height="40" fill="#009900"/>""")

    override def plurals: Plural.Rule = Plural.oneOrOther

    override def names: PartialFunction[Language, String] =
      case Esperanto => "Esperanto"
      case English   => "angla"
      case German    => "germana"

    override def called: PartialFunction[Language, String] =
      case French => "espéranto"

  /** A variety of German that names nothing itself. */
  object AustrianGerman extends Language.Variety("de-AT", of = German):

    override lazy val flag: Flag = Flag.bands(
      vertical = false,
      "#c8102e",
      "#ffffff",
      "#c8102e",
    )

    override def called: PartialFunction[Language, String] =
      case English => "Austrian German"
      case German  => "österreichisches Deutsch"

  /** A variety under a tag that a language of Megalingual's claims. */
  object MexicanSpanish
    extends Language.Variety("es-MX", of = LatinAmericanSpanish):

    override lazy val flag: Flag = Flag.bands(
      vertical = true,
      "#006847",
      "#ffffff",
      "#ce1126",
    )

  /** A variety that is named nowhere. */
  object Nameless extends Language.Variety("en-NZ", of = English):

    override lazy val flag: Flag = Flag.tile(code)

  /** Megalingual's languages and these. */
  val languages: Languages = Languages.builtIn ++ List(
    Esperanto,
    AustrianGerman,
    MexicanSpanish,
    Nameless,
  )
