package com.alecdorrington.megalingual
package languages

/**
  * Mandarin in simplified characters. As Chinese's first variety, it is what
  * tags such as `zh` and `zh-SG` find where a variety is needed.
  */
object SimplifiedChinese extends Language.Variety("zh-CN", of = Chinese):

  override lazy val flag: Flag = Flag.drawn(
    """<rect width="60" height="40" fill="#ee1c25"/>""" +
      Flag.star(10, 10, 6, "#ffff00") + Flag.star(20, 4, 2, "#ffff00") +
      Flag.star(24, 8, 2, "#ffff00") + Flag.star(24, 14, 2, "#ffff00") +
      Flag.star(20, 18, 2, "#ffff00"),
  )
