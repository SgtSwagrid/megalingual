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

- Two modules: `megalingual` (`core/`, JVM and Scala.js, depending on circe alone) and `megalingual-laminar`
  (`laminar/`, Scala.js alone, on Laminar 18). Nothing in the core touches Laminar or the DOM, so that a JVM server
  or a project without Laminar takes it alone. Each matrix pins its `sourceDirectory` to this build's base.
- `Language` is one enum of every language, as a tree of varieties (`parent`), each with its code and `Direction`
  in its case, and its flag (`Flag.of`, SVG markup), plural rule (`Plural.of`) and name in every language (`names/`)
  each in a match of its own, in its type's companion where it has one, so that code using none of them links none, and a flag is drawn only when shown. Those
  matches have no wildcard, so that a language added breaks the build until every fact of it is written (CI compiles
  with `-Werror`); `claims` lists only the exceptions. Apps must never match on every language: they name
  those they support in a `Localised`, whose partial function's domain is the set and whose values are made only
  when read.
- Every name is written by hand (agents, not a generator from CLDR), one `In<Language>` object per language named in,
  as written mid-sentence there; `title` capitalises the first letter for menus. Varieties naming languages alike
  share one object, and one differing in a few overrides them and falls back to the object it overrides
  (`InEuropeanPortuguese` to `InPortuguese`, `InCantonese`, Hong Kong's, to `InTraditionalChinese`, Taiwan's). The `add-language` skill (`.claude/skills/`) says how to add a language.
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
  and the active option keeps an outline (a background tint alone is too faint, and lost in forced colours). Every
  name shown, the button's too, carries its language's `lang` and `dir`. Everything touching the browser's `Intl`
  falls back to a plain rule where it is missing.
- The Laminar module's tests run in Node, with no DOM, so they test only what builds no element.

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
