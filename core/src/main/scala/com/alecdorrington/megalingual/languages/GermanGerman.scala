package com.alecdorrington.megalingual
package languages

object GermanGerman extends Language.Variety("de-DE", of = German):

  override lazy val flag: Flag = Flag.bands(
    vertical = false,
    "#000000",
    "#dd0000",
    "#ffce00",
  )
