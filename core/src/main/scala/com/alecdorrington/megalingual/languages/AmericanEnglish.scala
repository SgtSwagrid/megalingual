package com.alecdorrington.megalingual
package languages

object AmericanEnglish extends Language.Variety("en-US", of = English):

  override lazy val flag: Flag = Flag.drawn(
    """<rect width="60" height="40" fill="#b31942"/>""" +
      """<path d="M0,4.62 H60 M0,10.77 H60 M0,16.92 H60 M0,23.08 H60 """ +
      """M0,29.23 H60 M0,35.38 H60" stroke="#ffffff" stroke-width="3.08"/>""" +
      """<rect width="24" height="21.54" fill="#0a3161"/>""",
  )
