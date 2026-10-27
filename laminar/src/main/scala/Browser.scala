package com.alecdorrington.megalingual
package laminar

import scala.scalajs.js
import scala.util.Try

/** What the browser says of its user's languages. */
object Browser:

  /**
    * The languages the browser's user prefers, most preferred first, from
    * `navigator.languages`. It reads the global `navigator`, so serves a page,
    * a worker and an extension's service worker alike, and is empty where there
    * is none.
    */
  def preferences: List[Language] = Language.fromTags(tags)

  private def tags: List[String] = Try:
    val navigator = js.Dynamic.global.navigator
    val languages = navigator
      .languages
      .asInstanceOf[js.UndefOr[js.Array[String]]]
    val language = navigator.language.asInstanceOf[js.UndefOr[String]]
    languages.toList.flatMap(_.toList) ++ language.toList
  .getOrElse(Nil)
