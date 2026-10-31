package com.alecdorrington.megalingual
package laminar

import com.raquo.laminar.api.L.*

/**
  * A magnifying glass, marking the picker's search. It is drawn in the text's
  * colour, so that it follows the picker's colours and forced colours.
  */
private object Magnifier:

  /** Draws one, hidden from screen readers, as the search box is labelled. */
  def apply(): SvgElement = svg.svg(
    cls               := "megalingual-search-icon",
    aria.hidden       := true,
    svg.viewBox       := "0 0 16 16",
    svg.fill          := "none",
    svg.stroke        := "currentColor",
    svg.strokeWidth   := "1.75",
    svg.strokeLineCap := "round",
    svg.circle(svg.cx := "7", svg.cy := "7", svg.r := "4.75"),
    svg.path(svg.d    := "m10.5 10.5 3.5 3.5"),
  )
