package com.alecdorrington.megalingual
package laminar

import com.raquo.laminar.api.L.*
import org.scalajs.dom

/** Marks what is written in a language, so that browsers lay it out as one. */
object Written:

  /**
    * Marks an element as written in a language: its `lang` and `dir`.
    *
    * @param language
    *   The language.
    *
    * @return
    *   A modifier setting both attributes.
    */
  def in(language: Language): Modifier[HtmlElement] = Seq(
    lang := language.code,
    dir  := language.direction.attribute,
  )

  /**
    * Marks an element as written in a language, as it changes.
    *
    * @param language
    *   The language.
    *
    * @return
    *   A modifier keeping both attributes in step.
    */
  def in(language: Signal[Language]): Modifier[HtmlElement] = Seq(
    lang <-- language.map(_.code),
    dir <-- language.map(_.direction.attribute),
  )

  /**
    * Marks the whole page as written in a language, on its `<html>` element.
    *
    * @param language
    *   The language.
    */
  def page(language: Language): Unit =
    dom.document.documentElement.setAttribute("lang", language.code)
    dom
      .document
      .documentElement
      .setAttribute("dir", language.direction.attribute)
