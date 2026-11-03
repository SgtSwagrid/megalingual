package com.alecdorrington.megalingual
package languages

object EuropeanPortuguese extends Language.Variety("pt-PT", of = Portuguese):

  override def claims: List[String] =
    List("AO", "CV", "GW", "MO", "MZ", "ST", "TL").map("pt-" + _)

  override lazy val flag: Flag = Flag.drawn(
    """<rect width="60" height="40" fill="#da291c"/>""" +
      """<rect width="24" height="40" fill="#046a38"/>""" +
      """<circle cx="24" cy="20" r="8" fill="none" stroke="#ffe900" """ +
      """stroke-width="2"/>""" +
      """<path d="M20,15 H28 V21 Q28,25 24,26 Q20,25 20,21 Z" """ +
      """fill="#ffffff" stroke="#da291c" stroke-width="1.5"/>""",
  )

  override def plurals: Plural.Rule = Plural.oneAndMillions

  /**
    * Every language's name in European Portuguese, where it differs from
    * Brazilian Portuguese's.
    */
  override def names: PartialFunction[Language, String] =
    case Bengali             => "bengalês"
    case Czech               => "checo"
    case Dutch               => "neerlandês"
    case Estonian            => "estónio"
    case Polish              => "polaco"
    case BrazilianPortuguese => "português do Brasil"
