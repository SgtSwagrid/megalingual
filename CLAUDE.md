# CLAUDE.md

This file provides guidance to [Claude Code](https://claude.com/product/claude-code) when working with code in this repository.
It is not intended for human eyes.

### Maintenance

You (robot or human) have standing permission to update this file without asking.
Add important patterns, gotchas, or context that would help future sessions.
Keep it concise and actionable.

## Project overview

This is a Scala library of languages with their names and flags, and a Laminar picker for choosing one. It replaces
the language lists and pickers Themis and Codeswitch each had, and holds whatever about a language is not one app's.

- Three modules: `megalingual` (`core/`, JVM and Scala.js, depending on circe alone), `megalingual-laminar`
  (`laminar/`, Scala.js alone, on Laminar 18) and `megalingual-web` (`web/`, Scala.js alone, on cats-effect and
  scalajs-dom, with no Laminar). Nothing in the core touches Laminar or the DOM, so that a JVM server or a project
  without Laminar takes it alone, and what touches the browser but not Laminar is in `web`, so that a project
  without Laminar (as an extension's content script, whose bundle must stay small) takes it without Laminar. Each
  matrix pins its `sourceDirectory` to this build's base.
- `web` holds `Browser.preferences` (`navigator.languages`) and the browser's speech, `Speech[F[_] : Async]` (the
  Web Speech API, generic in its effect as Iris is), moved there from Omnibrowser's `omnibrowser-megalingual`, as
  nothing in it is an extension's. It reads the global `speechSynthesis` each time (none in a worker). `voices`
  waits up to `Speech.patience` (a second) for `voiceschanged` where none are listed, as Chromium lists none until
  first asked. `Voice.best` (pure) takes a voice of the language's tree, the fewest `steps` from its `spoken`
  variety (core's `Language.spoken`, its first variety through each level, and `steps`, up to the nearest language
  both are within, then down), on the device before online, then `Language.all`'s order; an app may choose
  among fewer, as Code Switch keeps Swiss German apart from German. `say` makes the utterance, listens, and calls
  `cancel` then `speak` all in its first step (`Saying.begin`), as a browser may need the user's activation, which
  `IO`'s `syncStep` keeps only up to the first asynchronous step; it ends at `end`, or an `error` of `interrupted`
  or `canceled` (cut short), fails with `SpeechError` on any other, and cancelled, cancels the browser's speech only
  while its utterance is still being said and is the latest begun (`Saying.latest`), as one cut short counts as
  being said until its `interrupted` event, a moment after what cut it short began. The `Saying` holds the
  utterance until then, as Chromium may otherwise collect it and never fire its events. Its tests run in Node,
  which has no speech, over a stub `speechSynthesis` and `SpeechSynthesisUtterance` whose events they fire by
  hand.
- Megalingual's languages are all there are, never one project's: an app needing another gets it added here.
  `Language` is a trait, each language an `object` in a file of its own in `languages/` (package
  `com.alecdorrington.megalingual.languages`), saying all there is of it: its code, `Direction`, parent, `claims` (tags
  it takes where the tree would choose another), flag (SVG markup, drawn with `Flag`'s shapes), plural rule
  (`plurals`, a shared rule of `Plural`'s or its own) and `names` (the names it gives languages). A root extends
  `Language.Root(code, direction)`; a variety `Language.Variety(code, of = parent)`, which takes its parent's
  direction, plural rule and names, so its file holds only what differs (a test fails a variety's name equal to its
  parent's). Their constructors are `private[megalingual]`, so only Megalingual (and its tests) makes a language.
  Flags are `lazy val`s and plural rules and names `def`s, so that code using none of them links none (Code Switch's
  page bundle links no names or flags), and a flag is drawn only when shown; codes and parents are `val`s.
- `Language.all` lists every language, in alphabetical order of their English names, each variety after its parent;
  its order says which variety stands in for a broader language (its first child). It follows the aliases in
  `Language`'s companion, each a `val` typed `Language` (`Language.German`), so that `Set(German, French)` is inferred
  a `Set[Language]`, as the enum's cases were (the objects' own singleton types would infer unions or
  `Language.Root`); before them it would hold nulls. `fromCode`, `fromTag` (a code before a claim, then the longest
  prefix), `fromTags`, `roots`, `children`, `descendants`, the circe codecs, `Localised`, the picker, `Voice.best` and
  `Browser.preferences` all look in it. Tests, not a registry, check that codes are distinct (ignoring case) and that
  each parent is listed before its varieties. Adding a language means its file, its alias and its line in
  `Language.all`; `PluralSuite`'s table, of every alias, must equal `Language.all`, so a language left out of either
  fails it. Apps must never match on every language: they name those they support in a
  `Localised`, whose partial function's domain is the set and whose values are made only when read.
- Every name is written by hand (agents, not a generator from CLDR), in the `names` of the language named in, as
  written mid-sentence there; `title` capitalises the first letter for menus. `name(in)` looks in `in`'s lineage's
  `names`, falling back to the code only for a name missing, which the tests forbid. A variety names only
  what it names otherwise than its parent (`EuropeanPortuguese`'s few, `Cantonese`'s, Hong Kong's, over
  `TraditionalChinese`'s, Taiwan's; `SwissGerman`'s over German's); `Chinese` holds the simplified table that
  `SimplifiedChinese` takes. Tests, not the compiler, check every built-in language names every one: the
  `add-language` skill (`.claude/skills/`) says how to add a language.
- A language's `substitutions` are how it writes its parent's text: each a `Substitution` (a Java regular expression
  and a replacement naming its groups `$1` to `$9`, `\` making the next character plain), made in one pass
  (`Substitution.substituted`, package-private), each point taking the first that matches there (the earliest match,
  ties to the first listed), so that what one writes no other rewrites. They are `Nil` by default; one type, in
  `Substitution.scala`, with no wrapper of its own. `language.respell(text, ancestor)` makes each language's
  substitutions on the way down from `ancestor` (exclusive) to the language, top down, and refuses an `ancestor` the
  language is not within. `GermanOfSwitzerland` alone has any (`ß` as `ss`, `ẞ` as `SS`), which Swiss Standard German
  and Swiss German take by walking the tree, never German or German German.
- `Localised` reads a language outside the set in its nearest supported one: itself, its first supported variety,
  else its parent's nearest, else the default. The tree alone says which languages read each other best: varieties
  whose readers do share a parent of their own (Taiwan's and Hong Kong's under `zh-Hant`, Swiss Standard German and
  Swiss German under `de-CH`), so each is read in the other before the rest. `negotiate` takes the reader's choice
  first, then each preference that is supported itself or by a variety, and only then reads any in another
  (`nearest`), so a language the reader names is never passed over for a sibling; a broader language is read in a
  variety they also prefer. `listed` adds a broader language only where two of its varieties are, or hold,
  supported ones.
- No backwards compatibility: codes and formats change freely, and nothing reads superseded ones. Tag matching
  (`fromTag`, `claims`) serves what browsers send now, not old data.
- The picker is an ARIA listbox, searched by a combobox above it that holds the focus while the list is open: the
  popup prevents every mousedown in it but the box's (scrollbars still work) and passes any focus on to the box. A
  search finds a language by a substring of any of its names in any language, ignoring case and accents, so short
  ones find much (Vietnamese's `tiếng` puts `eng` in every name); the one made active is the first whose name shown
  begins with it. Keys composing text (an input method's) are left alone. Hovering highlights an option only for a
  mousemove with movement, as Safari fires a still one when the arrow keys scroll the list under the pointer. Its interface language, label and name for no language are signals, so that it
  stays mounted (and keeps the focus) when the interface changes language; it never sits in a `child <--` that the
  choice rebuilds. Its stylesheet is added on mounting, once, first in the page's head, each rule of one class: never
  `:where()`, whose zero specificity loses to element resets (Tailwind's `button`, Bootstrap's `ul`), while any later
  class rule of the app's still wins; the search box's own `:focus` rule outranks an app's `input:focus` (Code
  Switch's), whose ring would sit inside the search row's border. Colours are `--megalingual-*` custom properties defaulting to system colours,
  and the active option keeps an outline (a background tint alone is too faint, and lost in forced colours). The
  list's scrollbar is a pill of the text faded, as the browser's own stays light on a dark surface the app sets
  without a dark `color-scheme` (Codeswitch's popup): `::-webkit-scrollbar` in Chromium and Safari (no arrows), and
  `scrollbar-color` only where those are missing (Firefox), as Chromium ignores them wherever it is set (so the list
  resets it, lest it inherit a page's). Every
  name shown, the button's too, carries its language's `lang` and `dir`. Everything touching the browser's `Intl`
  falls back to a plain rule where it is missing.
- The Laminar module's tests run in Node, with no DOM, so they test only what builds no element.
- Omnibrowser's `omnibrowser-megalingual` (`D:\omnibrowser`) and Code Switch are built against a release of this
  library, so a change to `Language`'s API needs them moved to the next release, Omnibrowser first, as Code Switch
  links Omnibrowser's module too.

### Gotchas

- Within its own body, Scala matches an object nested in a class (as a test's `private object`) as anything, so its
  `names` would give every language its own name: declare test languages at the top level or within an object.
- `Language.all` must stay below the aliases in `Language`'s companion: an object's `val`s are set in order.
- `Language.Root` and `Language.Variety` stay abstract classes: as traits with parameters, scalac 3.9.0 crashed in
  erasure ("object English has bad parents"). As `transparent` classes, a join of objects stayed a union.
- `export` cannot take a wildcard from a package, so `Language`'s companion names each language.

## Instructions

### Compilation and Diagnostics

- When the user asks for help with a compilation or type error, start by running `sbt compile` to see the error for yourself.
  If there are many errors, making it unclear which one the user is referring to, ask them to clarify, and then focus only on that issue.
- IntelliJ MCP integration is active. When a request seems to implicitly refer to something the user is looking at, always check
  `mcp__ide__getDiagnostics` first to see which file(s) are open and get associated diagnostics (errors, warnings, and info hints with line numbers).

#### Testing

- After making code changes, always run `sbt compile` to verify that issues are fixed and no new ones are introduced.
- Repeatedly retry upon failure until the build succeeds. If you are unsure how to fix an issue, ask for help or refer to existing code for examples.
- Before trying to fix an error, make sure you first understand it fully.
- You should never report that a feature is complete without testing it first.

### Code Style

- You must read the [Code Style Guidelines](docs/STYLE_GUIDE.md).

### Pull Requests

When asked to publish the code changes, your task is to open one or more pull requests (PRs) to merge the changes into `main` on GitHub:

- Use `git` to check what has changed as compared to the `main` branch on `origin`.
- If the changes are thematically linked, they can be published as a single PR.
- Otherwise, you'll need to divide the changes into multiple PRs using your own judgement.
- Each PR should have a singular focus, shouldn't break anything, and should be able to be merged independently.
- Ensure that all code is staged, committed and pushed. Ensure no new files are left uncommitted, and no debug code is left in the codebase.
- When creating a PR, ensure that the title and description are clear, informative, and comprehensive.
- All feature/bugfix/etc branch names should be formatted as "feature_<short description>" or "fix_<short description>" or similar.
- All PR titles should be formatted as "[<scope>] <Short summary>", e.g. "[renderer] Fixed colour inversion bug."
- Use the `gh` CLI to do the above.
