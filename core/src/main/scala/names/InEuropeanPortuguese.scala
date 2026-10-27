package com.alecdorrington.megalingual
package names

import com.alecdorrington.megalingual.Language.*

/**
  * Every language's name in European Portuguese, where it differs from
  * Brazilian Portuguese's.
  */
private[megalingual] object InEuropeanPortuguese:

  def apply(language: Language): String = language match
    case Czech               => "checo"
    case Dutch               => "neerlandês"
    case Estonian            => "estónio"
    case Hindi               => "hindi"
    case Polish              => "polaco"
    case BrazilianPortuguese => "português do Brasil"
    case _                   => InPortuguese(language)
