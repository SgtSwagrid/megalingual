package com.alecdorrington.megalingual
package languages

object LatinAmericanSpanish extends Language.Variety("es-419", of = Spanish):

  override def claims: List[String] = List(
    "AR",
    "BO",
    "CL",
    "CO",
    "CR",
    "CU",
    "DO",
    "EC",
    "GT",
    "HN",
    "MX",
    "NI",
    "PA",
    "PE",
    "PR",
    "PY",
    "SV",
    "US",
    "UY",
    "VE",
  ).map("es-" + _)

  override lazy val flag: Flag = Flag.drawn(
    """<rect width="60" height="40" fill="#ffffff"/>""" +
      """<rect width="20" height="40" fill="#006847"/>""" +
      """<rect x="40" width="20" height="40" fill="#ce1126"/>""" +
      """<circle cx="30" cy="20" r="5" fill="#8c5a2b"/>""",
  )
