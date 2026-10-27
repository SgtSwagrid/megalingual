package com.alecdorrington.megalingual
package laminar

import com.raquo.laminar.api.L.*
import com.raquo.laminar.codecs.Codec
import org.scalajs.dom
import scala.scalajs.js

/**
  * A picker of languages: a button showing the chosen one, opening a list of
  * those offered, each with its flag and its names, varieties beneath the
  * languages they are varieties of.
  *
  * It is an ARIA listbox: arrow keys, Home and End move through it, a letter
  * moves to the next language either of whose names begins with it (ignoring
  * accents), Enter or Space chooses, and Escape, Tab or a click elsewhere
  * closes it.
  *
  * The interface's language and the label are signals, so that the picker stays
  * where it is, and keeps the focus, when the interface changes language.
  *
  * Its classes all begin `megalingual-`. Its stylesheet is added once, first in
  * the page's head, its rules each of one class, so that they outlast element
  * resets (Tailwind's, Bootstrap's) and any later rule of the app's overrides
  * them. It does not reach into a shadow root. Its colours are custom
  * properties: `--megalingual-text`, `-surface`, `-border`, `-accent`,
  * `-active`, `-radius` and `-shadow`, by default the system's.
  */
object LanguagePicker:

  /**
    * Makes a picker of one of the given languages.
    *
    * @param languages
    *   The languages offered, such as a [[Supported]]'s `listed`.
    *
    * @param chosen
    *   The chosen language.
    *
    * @param choose
    *   Receives each language chosen.
    *
    * @param in
    *   The interface's language, which names and orders the languages.
    *
    * @param label
    *   What is chosen, such as `Language`, for screen readers.
    *
    * @param ownFirst
    *   Whether each language leads with its own name, as for choosing the
    *   interface's language, which a reader looks for in one they can read.
    *
    * @param named
    *   Whether the button names the chosen language beside its flag.
    *
    * @return
    *   A picker, closed.
    */
  def apply
    (
      languages: List[Language],
      chosen: Signal[Language],
      choose: Observer[Language],
      in: Signal[Language],
      label: Signal[String],
      ownFirst: Boolean = false,
      named: Boolean = true,
    )
    : HtmlElement = Picker(
    in.map(Naming(_, ownFirst, none = "")),
    naming => choices(languages, naming),
    chosen.map(Some(_)),
    choose.contracollect { case Some(language) => language },
    label,
    named,
  ).element

  /**
    * Makes a picker of one of the given languages or none, the first choice.
    *
    * @param languages
    *   The languages offered, such as a [[Supported]]'s `listed`.
    *
    * @param chosen
    *   The chosen language, if any.
    *
    * @param choose
    *   Receives each choice.
    *
    * @param in
    *   The interface's language, which names and orders the languages.
    *
    * @param label
    *   What is chosen, such as `Language`, for screen readers.
    *
    * @param none
    *   The name of choosing no language, in the interface's language.
    *
    * @param ownFirst
    *   Whether each language leads with its own name, as for choosing the
    *   interface's language, which a reader looks for in one they can read.
    *
    * @param named
    *   Whether the button names the chosen language beside its flag.
    *
    * @return
    *   A picker, closed.
    */
  def optional
    (
      languages: List[Language],
      chosen: Signal[Option[Language]],
      choose: Observer[Option[Language]],
      in: Signal[Language],
      label: Signal[String],
      none: Signal[String],
      ownFirst: Boolean = false,
      named: Boolean = true,
    )
    : HtmlElement = Picker(
    in.combineWith(none).map((in, none) => Naming(in, ownFirst, none)),
    naming => Choice(None, 0) :: choices(languages, naming),
    chosen,
    choose,
    label,
    named,
  ).element

  /** One choice in the list, at its depth in the tree. */
  private[laminar] final case class Choice
    (language: Option[Language], depth: Int)

  /**
    * How the choices are named: in the interface's language, or first in their
    * own, with the other name aside.
    */
  private[laminar] final case class Naming
    (
      in: Language,
      ownFirst: Boolean,
      none: String,
    ):

    def lead(language: Option[Language]): String = language.fold(none)(lead)

    def lead(language: Language): String = language.title(leadIn(language))

    def aside(language: Language): Option[String] = Option
      .when(asideIn(language) != leadIn(language))(language.title(asideIn(
        language,
      )))
      .filter(_ != lead(language))

    /** The language a choice's leading name is written in. */
    def leadIn(language: Option[Language]): Language = language.fold(in)(leadIn)

    def leadIn(language: Language): Language = if ownFirst then language else in

    def asideIn(language: Language): Language =
      if ownFirst then in else language

    /** Whether either of a choice's names begins with what was typed. */
    def begins
      (
        language: Option[Language],
        typed: String,
      )
      : Boolean = (lead(language) :: language.toList.flatMap(aside)).exists(
      name => folded(name).startsWith(folded(typed)),
    )

  /** Text in lower case without its accents (`Čeština` as `cestina`). */
  private def folded(text: String): String = text
    .asInstanceOf[js.Dynamic]
    .normalize("NFD")
    .asInstanceOf[String]
    .filterNot(char => char >= 0x300 && char <= 0x36F)
    .toLowerCase

  /**
    * The languages as a tree, each with its depth: a variety follows its
    * nearest offered ancestor, one level deeper, and siblings are sorted by
    * their leading names.
    */
  private[laminar] def choices
    (
      languages: List[Language],
      naming: Naming,
    )
    : List[Choice] =
    val offered  = languages.distinct
    val byParent = offered.groupBy(_.ancestors.find(offered.contains))
    def beneath(parent: Option[Language], depth: Int): List[Choice] = Intl
      .sorted(
        byParent.getOrElse(parent, Nil),
        naming.in,
      )(naming.lead)
      .flatMap(language =>
        Choice(Some(language), depth) :: beneath(Some(language), depth + 1),
      )
    beneath(None, 0)

  /** How many pickers there have been, to give each's elements their own ids. */
  private var pickers = 0

  /**
    * One picker's state and parts.
    *
    * @param namings
    *   How the choices are named, as the interface's language changes.
    *
    * @param offer
    *   The choices, named and ordered.
    */
  private final class Picker
    (
      namings: Signal[Naming],
      offer: Naming => List[Choice],
      chosen: Signal[Option[Language]],
      choose: Observer[Option[Language]],
      label: Signal[String],
      named: Boolean,
    ):

    pickers += 1

    private val id      = s"megalingual-picker-$pickers"
    private val open    = Var(false)
    private val active  = Var(0)
    private val current = Var(Option.empty[Language])
    private val choices = Var(List.empty[Choice])

    // In step with `namings` from mounting on, before which nothing reads it.
    private val naming = Var(Naming(Language.English, ownFirst = false, ""))

    private val toggle: HtmlElement = button(
      tpe                                         := "button",
      cls                                         := "megalingual-button",
      htmlAttr("aria-haspopup", Codec.stringAsIs) := "listbox",
      aria.expanded <-- open.signal,
      // Named only while the list it names is there; a null removes it.
      aria.controls <-- open.signal.map(if _ then s"$id-list" else null),
      span(
        cls := "megalingual-hidden",
        child.text <-- label,
      ),
      child <-- chosen.combineWith(namings).map(chosenFlag),
      Option.when(named)(span(
        cls := "megalingual-name",
        child <-- chosen.combineWith(namings).map(chosenName),
      )),
      span(
        cls         := "megalingual-caret",
        aria.hidden := true,
        "▾",
      ),
      onClick --> (_ => if open.now() then close(refocus = false) else show()),
      onKeyDown
        .filter(event => opening(event.key) && !open.now())
        .preventDefault --> (_ => show()),
    )

    /** The picker. */
    val element: HtmlElement = div(
      cls := "megalingual-picker",
      onMountCallback(_ => Stylesheet.install()),
      chosen --> current.writer,
      namings --> naming.writer,
      toggle,
      inContext(root =>
        documentEvents(_.onMouseDown).filter(event =>
          !root.ref.contains(event.target.asInstanceOf[dom.Node]),
        ) --> (_ => if open.now() then close(refocus = false)),
      ),
      child.maybe <-- open.signal.map(Option.when(_)(list(naming.now()))),
    )

    /** The chosen language's flag, named for screen readers if not beside it. */
    private def chosenFlag
      (
        language: Option[Language],
        naming: Naming,
      ) = flag(
      language,
      Option.unless(named)(naming.lead(language) -> naming.leadIn(language)),
    )

    private def chosenName
      (
        language: Option[Language],
        naming: Naming,
      ) = span(
      Written.in(naming.leadIn(language)),
      naming.lead(language),
    )

    private def opening(key: String): Boolean =
      Set("ArrowDown", "ArrowUp", "Enter", " ")(key)

    private def show(): Unit =
      val offered = offer(naming.now())
      choices.set(offered)
      active.set(offered.indexWhere(_.language == current.now()).max(0))
      open.set(true)

    private def close(refocus: Boolean): Unit =
      open.set(false)
      if refocus then toggle.ref.focus()

    private def pick(language: Option[Language]): Unit =
      close(refocus = true)
      choose.onNext(language)

    private def list(naming: Naming): HtmlElement = ul(
      idAttr   := s"$id-list",
      cls      := "megalingual-list",
      role     := "listbox",
      tabIndex := -1,
      aria.label <-- label,
      aria.activeDescendant <-- active.signal.map(activeId),
      onMountCallback(context => context.thisNode.ref.focus()),
      active.signal --> (index => reveal(s"$id-$index")),
      onKeyDown --> (event => keyed(event)),
      eventProp[dom.FocusEvent]("focusout") --> (event => left(event)),
      choices.now().zipWithIndex.map(option(naming)),
    )

    /** The active choice's id, or none in an empty list. */
    private def activeId(index: Int): String =
      if choices.now().isEmpty then null else s"$id-$index"

    /** Closes the list once focus leaves the picker, as by Tab. */
    private def left(event: dom.FocusEvent): Unit = Option(event.relatedTarget)
      .collect { case node: dom.Node => node }
      .filterNot(element.ref.contains)
      .foreach(_ => close(refocus = false))

    private def option
      (naming: Naming)
      (choice: Choice, index: Int)
      : HtmlElement = li(
      idAttr := s"$id-$index",
      role   := "option",
      cls    := "megalingual-option",
      cls("megalingual-option-active") <-- active.signal.map(_ == index),
      aria.selected <-- chosen.map(_ == choice.language),
      styleProp[String]("--megalingual-depth") := choice.depth.toString,
      onMouseMove --> (_ => active.set(index)),
      onClick --> (_ => pick(choice.language)),
      flag(choice.language, alt = None),
      choice.language match
        case Some(language) => names(language, naming)
        case None           => Seq(span(cls := "megalingual-lead", naming.none)),
    )

    private def names(language: Language, naming: Naming): Seq[HtmlElement] =
      written(
        "megalingual-lead",
        naming.lead(language),
        naming.leadIn(language),
      ) ::
        naming
          .aside(language)
          .map(written(
            "megalingual-aside",
            _,
            naming.asideIn(language),
          ))
          .toList

    /** Moves through the list, chooses or closes, by the key pressed. */
    private def keyed(event: dom.KeyboardEvent): Unit = event.key match
      case "ArrowDown"   => move(event, active.now() + 1)
      case "ArrowUp"     => move(event, active.now() - 1)
      case "Home"        => move(event, 0)
      case "End"         => move(event, choices.now().size - 1)
      case "Enter" | " " =>
        event.preventDefault()
        choices
          .now()
          .lift(active.now())
          .foreach(choice => pick(choice.language))
      case "Escape" =>
        // Closes only the list, not a dialog it is in.
        event.preventDefault()
        event.stopPropagation()
        close(refocus = true)
      case "Tab"               => close(refocus = false)
      case key if typed(event) => next(key).foreach(move(event, _))
      case _                   => ()

    private def move(event: dom.KeyboardEvent, index: Int): Unit =
      event.preventDefault()
      active.set(index.min(choices.now().size - 1).max(0))

    /** The next choice after the active one, wrapping round, begun by `key`. */
    private def next(key: String): Option[Int] =
      val offered = choices.now()
      (1 to offered.size)
        .map(step => (active.now() + step) % offered.size)
        .find(index => naming.now().begins(offered(index).language, key))

  private def typed(event: dom.KeyboardEvent): Boolean =
    event.key.length == 1 && !event.ctrlKey && !event.metaKey && !event.altKey

  /**
    * A name in its own language, inside a span keeping the row's direction, so
    * that a name written right to left still lines up with the others.
    */
  private def written(kind: String, name: String, in: Language): HtmlElement =
    span(cls := kind, span(Written.in(in), name))

  private def reveal(id: String): Unit = Option(dom.document.getElementById(id))
    .foreach(
      _.asInstanceOf[js.Dynamic]
        .scrollIntoView(js.Dynamic.literal(block = "nearest")),
    )

  /**
    * A language's flag, with its name for screen readers, in the language it is
    * written in, if nothing beside it names it.
    */
  private def flag
    (
      language: Option[Language],
      alt: Option[(String, Language)],
    )
    : HtmlElement = language.fold(span(
    cls := "megalingual-flag megalingual-flag-none",
  ))(language =>
    img(
      cls                         := "megalingual-flag",
      src                         := language.flag.dataUri,
      com.raquo.laminar.api.L.alt := alt.fold("")(_._1),
      alt.map((_, in) => Written.in(in)),
    ),
  )
