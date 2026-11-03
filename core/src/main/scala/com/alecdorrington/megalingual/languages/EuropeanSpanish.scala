package com.alecdorrington.megalingual
package languages

object EuropeanSpanish extends Language.Variety("es-ES", of = Spanish):

  // Four equal bands, so that the yellow is twice as tall.
  override lazy val flag: Flag = Flag.bands(
    vertical = false,
    "#aa151b",
    "#f1bf00",
    "#f1bf00",
    "#aa151b",
  )
