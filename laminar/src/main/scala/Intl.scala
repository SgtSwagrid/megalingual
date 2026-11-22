package com.alecdorrington.megalingual
package laminar

import scala.scalajs.js
import scala.util.Try

/**
  * The browser's own rules for a language's text (`Intl`), for what Megalingual
  * does not write itself: how a language orders words, how it lists them, and
  * what it calls a region. Each asks for the language, else the nearest it is a
  * variety of that the browser knows, and where the browser lacks a rule, each
  * falls back to a plain one.
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

  /**
    * Names a region as a language names it (`Schweiz` for `CH` in German).
    *
    * @param code
    *   The region's code, as [[Language.region]] gives one: ISO 3166-1's two
    *   letters (`CH`), or UN M.49's three digits (`419`).
    *
    * @param in
    *   The language to name it in.
    *
    * @return
    *   A name for the region, or its code where the browser knows none.
    */
  def region(code: String, in: Language): String = Try(
    construct(
      global.DisplayNames,
      in,
      js.Dynamic.literal(`type` = "region"),
    ).of(code).asInstanceOf[js.UndefOr[String]].toOption,
  ).toOption.flatten.getOrElse(code)

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
    : js.Dynamic = js
    .Dynamic
    .newInstance(kind)(
      js.Array(in.lineage.map(_.code)*),
      options,
    )
