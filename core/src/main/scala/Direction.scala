package com.alecdorrington.megalingual

/**
  * A direction a language is written in.
  *
  * @param attribute
  *   The value of HTML's `dir` attribute for the direction.
  */
enum Direction(val attribute: String):

  case LeftToRight extends Direction("ltr")

  case RightToLeft extends Direction("rtl")
