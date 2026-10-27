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
  * moves to the next language beginning with it, Enter or Space chooses, and
  * Escape, Tab or a click elsewhere closes it.
  *
  * Its classes all begin `megalingual-`, and its stylesheet, added to the page
  * once, has no specificity, so that any rule of the app's overrides it. Its
  * colours are custom properties: `--megalingual-text`, `-surface`, `-border`,
  * `-accent`, `-active`, `-radius` and `-shadow`, by default the system's.
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
      in: Language,
      label: String,
      ownFirst: Boolean = false,
      named: Boolean = true,
    )
    : HtmlElement = picker(
    Naming(in, ownFirst, none = ""),
    choices(languages, Naming(in, ownFirst, none = "")),
    chosen.map(Some(_)),
    choose.contracollect { case Some(language) => language },
    label,
    named,
  )

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
      in: Language,
      label: String,
      none: String,
      ownFirst: Boolean = false,
      named: Boolean = true,
    )
    : HtmlElement =
    val naming = Naming(in, ownFirst, none)
    picker(
      naming,
      Choice(None, 0) :: choices(languages, naming),
      chosen,
      choose,
      label,
      named,
    )

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

    def leadIn(language: Language): Language = if ownFirst then language else in

    def asideIn(language: Language): Language =
      if ownFirst then in else language

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

  private var pickers = 0

  private def picker
    (
      naming: Naming,
      choices: List[Choice],
      chosen: Signal[Option[Language]],
      choose: Observer[Option[Language]],
      label: String,
      named: Boolean,
    )
    : HtmlElement =
    Stylesheet.install()
    pickers += 1
    val id      = s"megalingual-picker-$pickers"
    val open    = Var(false)
    val active  = Var(0)
    val current = Var(Option.empty[Language])
    val toggle  = button(
      tpe                                         := "button",
      cls                                         := "megalingual-button",
      htmlAttr("aria-haspopup", Codec.stringAsIs) := "listbox",
      aria.expanded <-- open.signal,
      aria.controls := s"$id-list",
      span(cls := "megalingual-hidden", label),
      child <-- chosen.map(language =>
        flag(
          language,
          alt = if named then "" else naming.lead(language),
        ),
      ),
      Option.when(named)(span(
        cls := "megalingual-name",
        child.text <-- chosen.map(naming.lead),
      )),
      span(
        cls         := "megalingual-caret",
        aria.hidden := true,
        "▾",
      ),
      chosen --> current.writer,
    )
    def index: Int   = choices.indexWhere(_.language == current.now()).max(0)
    def show(): Unit =
      active.set(index)
      open.set(true)
    def close(refocus: Boolean): Unit =
      open.set(false)
      if refocus then toggle.ref.focus()
    def pick(language: Option[Language]): Unit =
      close(refocus = true)
      choose.onNext(language)
    def list: HtmlElement = ul(
      idAttr     := s"$id-list",
      cls        := "megalingual-list",
      role       := "listbox",
      tabIndex   := -1,
      aria.label := label,
      aria.activeDescendant <-- active.signal.map(index => s"$id-$index"),
      onMountCallback(context => context.thisNode.ref.focus()),
      active.signal --> (index => reveal(s"$id-$index")),
      onKeyDown -->
        (event =>
          keyed(
            event,
            choices,
            naming,
            active,
            pick,
            close,
          )
        ),
      eventProp[dom.FocusEvent]("focusout") -->
        (event =>
          Option(event.relatedTarget)
            .collect { case node: dom.Node => node }
            .filterNot(toggle.ref.parentNode.contains)
            .foreach(_ => close(refocus = false))
        ),
      choices
        .zipWithIndex
        .map((choice, index) =>
          option(
            id,
            choice,
            index,
            naming,
            active,
            chosen,
            pick,
          ),
        ),
    )
    div(
      cls := "megalingual-picker",
      toggle.amend(
        onClick -->
          (_ => if open.now() then close(refocus = false) else show()),
        onKeyDown
          .filter(event =>
            Set("ArrowDown", "ArrowUp", "Enter", " ")(event.key) && !open.now(),
          )
          .preventDefault --> (_ => show()),
      ),
      inContext(root =>
        documentEvents(_.onMouseDown).filter(event =>
          !root.ref.contains(event.target.asInstanceOf[dom.Node]),
        ) --> (_ => if open.now() then close(refocus = false)),
      ),
      child.maybe <-- open.signal.map(Option.when(_)(list)),
    )

  private def option
    (
      id: String,
      choice: Choice,
      index: Int,
      naming: Naming,
      active: Var[Int],
      chosen: Signal[Option[Language]],
      pick: Option[Language] => Unit,
    )
    : HtmlElement = li(
    idAttr := s"$id-$index",
    role   := "option",
    cls    := "megalingual-option",
    cls("megalingual-option-active") <-- active.signal.map(_ == index),
    aria.selected <-- chosen.map(_ == choice.language),
    styleProp[String]("--megalingual-depth") := choice.depth.toString,
    onMouseMove --> (_ => active.set(index)),
    onClick --> (_ => pick(choice.language)),
    flag(choice.language, alt = ""),
    choice.language match
      case Some(language) => names(language, naming)
      case None           => Seq(span(cls := "megalingual-lead", naming.none)),
  )

  private def names(language: Language, naming: Naming): Seq[HtmlElement] =
    named(
      "megalingual-lead",
      naming.lead(language),
      naming.leadIn(language),
    ) ::
      naming
        .aside(language)
        .map(named(
          "megalingual-aside",
          _,
          naming.asideIn(language),
        ))
        .toList

  /**
    * A name in its own language, inside a span keeping the row's direction, so
    * that a name written right to left still lines up with the others.
    */
  private def named(kind: String, name: String, in: Language): HtmlElement =
    span(cls := kind, span(Written.in(in), name))

  /** Moves through the list, chooses or closes, by the key pressed. */
  private def keyed
    (
      event: dom.KeyboardEvent,
      choices: List[Choice],
      naming: Naming,
      active: Var[Int],
      pick: Option[Language] => Unit,
      close: Boolean => Unit,
    )
    : Unit =
    val last                   = choices.size - 1
    def move(index: Int): Unit =
      event.preventDefault()
      active.set(index.max(0).min(last))
    event.key match
      case "ArrowDown"   => move(active.now() + 1)
      case "ArrowUp"     => move(active.now() - 1)
      case "Home"        => move(0)
      case "End"         => move(last)
      case "Enter" | " " =>
        event.preventDefault()
        pick(choices(active.now()).language)
      case "Escape" =>
        event.preventDefault()
        close(true)
      case "Tab"               => close(false)
      case key if typed(event) =>
        next(choices, naming, active.now(), key).foreach(move)
      case _ => ()

  private def typed(event: dom.KeyboardEvent): Boolean =
    event.key.length == 1 && !event.ctrlKey && !event.metaKey && !event.altKey

  /** The next choice after `from`, wrapping round, whose name begins `key`. */
  private def next
    (
      choices: List[Choice],
      naming: Naming,
      from: Int,
      key: String,
    )
    : Option[Int] = (1 to choices.size)
    .map(step => (from + step) % choices.size)
    .find(index =>
      naming
        .lead(choices(index).language)
        .toLowerCase
        .startsWith(key.toLowerCase),
    )

  private def reveal(id: String): Unit = Option(dom.document.getElementById(id))
    .foreach(
      _.asInstanceOf[js.Dynamic]
        .scrollIntoView(js.Dynamic.literal(block = "nearest")),
    )

  private def flag(language: Option[Language], alt: String): HtmlElement =
    language.fold(span(cls := "megalingual-flag megalingual-flag-none"))(
      language =>
        img(
          cls                         := "megalingual-flag",
          src                         := language.flag.dataUri,
          com.raquo.laminar.api.L.alt := alt,
        ),
    )
