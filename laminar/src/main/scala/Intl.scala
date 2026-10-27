package com.alecdorrington.megalingual
package laminar

import scala.scalajs.js
import scala.util.Try

/**
  * The browser's own rules for a language's text (`Intl`), for what Megalingual
  * does not write itself: how a language orders words, and how it lists them.
  * Where the browser lacks a rule, each falls back to a plain one.
  */
object Intl:

  /**
    * Sorts things by their names, as a language orders words.
    *
    * @param things
    *   The things to sort.
    *
    * @param in
    *   The language whose order to follow.
    *
    * @param name
    *   Names each thing in that language.
    *
    * @tparam A
    *   The things' type.
    *
    * @return
    *   The things in order.
    */
  def sorted[A](things: List[A], in: Language)(name: A => String): List[A] =
    val compare = comparison(in)
    things
      .map(thing => thing -> name(thing))
      .sortWith((a, b) => compare(a._2, b._2) < 0)
      .map(_._1)

  /**
    * Joins names into one list, as a language lists things (`Ana, Ben and
    * Cal`).
    *
    * @param names
    *   The names, in order.
    *
    * @param in
    *   The language to list them in.
    *
    * @return
    *   A list of the names.
    */
  def listed(names: List[String], in: Language): String = Try(
    construct(
      global.ListFormat,
      in,
      js.Dynamic.literal(`type` = "conjunction"),
    ).format(js.Array(names*)).asInstanceOf[String],
  ).getOrElse(names.mkString(", "))

  private def comparison(in: Language): (String, String) => Double =
    Try(construct(
      global.Collator,
      in,
      js.Dynamic.literal(),
    )).fold(
      _ => (a, b) => a.compareTo(b).toDouble,
      collator => (a, b) => collator.compare(a, b).asInstanceOf[Double],
    )

  private def global: js.Dynamic = js.Dynamic.global.Intl

  private def construct
    (
      kind: js.Dynamic,
      in: Language,
      options: js.Object,
    )
    : js.Dynamic = js.Dynamic.newInstance(kind)(js.Array(in.code), options)
