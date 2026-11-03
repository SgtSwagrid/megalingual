package com.alecdorrington.megalingual
package languages

object BritishEnglish extends Language.Variety("en-GB", of = English):

  override lazy val flag: Flag = Flag.drawn(
    """<rect width="60" height="40" fill="#012169"/>""" +
      """<path d="M0,0 L60,40 M60,0 L0,40" stroke="#ffffff" stroke-width="8"/>""" +
      """<path d="M0,0 L60,40 M60,0 L0,40" stroke="#c8102e" stroke-width="3"/>""" +
      """<path d="M30,0 V40 M0,20 H60" stroke="#ffffff" stroke-width="12"/>""" +
      """<path d="M30,0 V40 M0,20 H60" stroke="#c8102e" stroke-width="7"/>""",
  )
