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
- `Language` is one enum of every language, as a tree of varieties (`parent`), each with its code, `Direction`, flag
  (SVG markup, in `Flag`), plural rule (`Plural.of`) and name in every language (`names/`). Inside the library,
  matches over every language have no wildcard, so that a language added breaks the build until every fact of it is
  written (CI compiles with `-Werror`). Apps must never match on every language: they name those they support in a
  `Supported`, whose partial function's domain is the set and whose values are made only when read.
- Every name is written by hand (agents, not a generator from CLDR), one `In<Language>` object per language named in,
  as written mid-sentence there; `title` capitalises the first letter for menus. Varieties naming languages alike
  share their parent's object, and one differing in a few overrides them and falls back to its parent's
  (`InEuropeanPortuguese`). The `add-language` skill (`.claude/skills/`) says how to add a language.
- `Supported` reads a language outside the set in its nearest supported one: itself, its first supported variety,
  else its parent's nearest, else the default. `negotiate` prefers a variety the reader also prefers.
- No backwards compatibility: codes and formats change freely, and nothing reads superseded ones. Tag matching
  (`fromTag`, `claims`) serves what browsers send now, not old data.
- The picker is an ARIA listbox. Its stylesheet is added to the page once, every rule in `:where()` so that any rule
  of the app's wins, coloured through `--megalingual-*` custom properties defaulting to system colours. Everything
  touching the browser's `Intl` falls back to a plain rule where it is missing.
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
