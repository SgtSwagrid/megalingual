package com.alecdorrington.megalingual
package languages

/**
  * Swiss Standard German, written without `ß`, under `de-CH-1996`, the reformed
  * Swiss spelling.
  */
object SwissStandardGerman
  extends Language.Variety("de-CH-1996", of = GermanOfSwitzerland):

  override def claims: List[String] = List("de-LI")

  override lazy val flag: Flag = Flag.drawn(
    """<rect width="60" height="40" fill="#da291c"/>""" +
      """<path d="M30,8 V32 M18,20 H42" stroke="#ffffff" stroke-width="8"/>""",
  )
