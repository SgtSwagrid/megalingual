package com.alecdorrington.megalingual
package languages

/**
  * German as Switzerland writes and speaks it, under `de-CH`, the tag its
  * readers' browsers send, and read in Swiss Standard German where a variety is
  * needed. Like any language's, a reader naming it is read in any variety
  * supported, so in the dialects where Swiss Standard German is not. It is not
  * `SwissGerman`, which names the dialects. It writes no `ß`, but `ss`, and so
  * do its varieties.
  */
object GermanOfSwitzerland extends Language.Variety("de-CH", of = German):

  override lazy val flag: Flag = Flag.tile(code)

  override def substitutions: List[Substitution] = List(
    Substitution("ß", "ss"),
    Substitution("ẞ", "SS"),
  )
