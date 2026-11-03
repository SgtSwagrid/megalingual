package com.alecdorrington.megalingual
package languages

object BrazilianPortuguese extends Language.Variety("pt-BR", of = Portuguese):

  override lazy val flag: Flag = Flag.drawn(
    """<rect width="60" height="40" fill="#009c3b"/>""" +
      """<polygon points="6,20 30,4 54,20 30,36" fill="#ffdf00"/>""" +
      """<circle cx="30" cy="20" r="8.5" fill="#002776"/>""",
  )
