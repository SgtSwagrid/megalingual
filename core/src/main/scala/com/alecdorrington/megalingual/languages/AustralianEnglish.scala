package com.alecdorrington.megalingual
package languages

object AustralianEnglish extends Language.Variety("en-AU", of = English):

  // Focused on the Commonwealth Star, which no other flag has.
  override lazy val flag: Flag = Flag.drawn(
    """<rect width="60" height="40" fill="#012169"/>""" +
      Flag.canton(BritishEnglish.flag) + List(
        (15.0, 30.0, 6.0, 7),   // the Commonwealth Star
        (45.0, 33.33, 3.0, 7),  // Alpha Crucis
        (37.5, 17.5, 3.0, 7),   // Beta Crucis
        (45.0, 6.67, 3.0, 7),   // Gamma Crucis
        (51.67, 14.83, 3.0, 7), // Delta Crucis
        (48.0, 21.67, 1.8, 5),  // Epsilon Crucis
      ).map((x, y, radius, points) =>
          Flag.star(
            x,
            y,
            radius,
            "#ffffff",
            points,
            inner = 4.0 / 9,
          ),
        )
        .mkString,
  )
