package com.alecdorrington.megalingual

import java.util.regex.{Matcher, Pattern}
import scala.util.matching.Regex

/**
  * A regular-expression substitution, by which a language writes some of what
  * its parent does otherwise (`ß` as `ss`).
  *
  * @param pattern
  *   What it replaces, as a Java regular expression.
  *
  * @param replacement
  *   What each match becomes, in which `$1` to `$9` stand for the text of the
  *   pattern's groups, and `\` makes the next character plain (`\$`).
  */
final case class Substitution(pattern: String, replacement: String):

  private val regex = Pattern.compile(pattern)

  require(
    Substitution
      .tokens
      .findAllMatchIn(replacement)
      .flatMap(token => Option(token.group(2)))
      .forall(_.toInt <= regex.matcher("").groupCount),
    s"Each group $replacement names is one of $pattern's.",
  )

  /** Its first match in a text from `start` on, if any. */
  private[megalingual] def find(text: String, start: Int): Option[Matcher] =
    Some(regex.matcher(text)).filter(_.find(start))

  /** What a match of it becomes. */
  private[megalingual] def replace(found: Matcher): String = Substitution
    .tokens
    .replaceAllIn(
      replacement,
      token => Regex.quoteReplacement(expanded(token, found)),
    )

  /** What a token of the replacement stands for in a match. */
  private def expanded(token: Regex.Match, found: Matcher): String = Option(
    token.group(1),
  ).orElse(Option(found.group(token.group(2).toInt))).getOrElse("")

object Substitution:

  /** A character made plain, or a group named, in a replacement. */
  private val tokens = """(?s)\\(.)|\$(\d)""".r

/**
  * How a language writes its parent's text, by substitutions applied in one
  * pass: each point of the text, from its start, takes the first that matches
  * there, and what that writes is never rewritten by another.
  *
  * @param substitutions
  *   The substitutions, the first preferred where several match at one point.
  */
final case class Respelling(substitutions: List[Substitution]):

  /**
    * Respells text.
    *
    * @param text
    *   The text, as the parent writes it.
    *
    * @return
    *   The text as the language writes it.
    */
  def apply(text: String): String = Iterator
    .unfold(Option(0))(_.map(respelt(text, _)))
    .mkString

  /**
    * The text from `start` up to and through its next match, respelt, and where
    * to go on from, if anywhere: the match's end, or past one more character
    * after an empty match.
    */
  private def respelt(text: String, start: Int): (String, Option[Int]) =
    next(text, start).fold(text.substring(start) -> None):
      (substitution, found) =>
        val before = text.substring(start, found.start) +
          substitution.replace(found)
        if found.end > found.start then before -> Some(found.end)
        else
          (before + text.slice(found.start, found.start + 1)) ->
            Option.when(found.start < text.length)(found.start + 1)

  /** The substitution matching first from `start` on, and its match. */
  private def next(text: String, start: Int): Option[(Substitution, Matcher)] =
    substitutions
      .flatMap(substitution =>
        substitution.find(text, start).map(substitution -> _),
      )
      .minByOption(_._2.start)

object Respelling:

  /** Writing as the parent does. */
  val none: Respelling = Respelling(Nil)
