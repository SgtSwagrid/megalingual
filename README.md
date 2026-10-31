<div align="center">

  <h1>🌐 Megalingual</h1>
  <p>Internationalisation helpers with languages and flags.</p>
  
  <span>
    <a href="https://github.com/SgtSwagrid/megalingual/actions/workflows/build-integrity.yml"><img src="https://github.com/SgtSwagrid/megalingual/actions/workflows/build-integrity.yml/badge.svg" alt="Build status" /></a>
    <a href="https://search.maven.org/artifact/com.alecdorrington/megalingual_3"><img src="https://img.shields.io/maven-central/v/com.alecdorrington/megalingual_3.svg" alt="Maven Central" /></a>
    <a href="https://alecdorrington.com/megalingual"><img src="https://img.shields.io/badge/docs-latest-blue.svg" alt="Documentation" /></a>
  </span>
  
</div>

> [!WARNING]
> Megalingual is still a prototype. Anything could be broken or change between minor versions.

## ⬇️ Installation

There are several components which can be installed separately, but the same version number (e.g. [latest](../../releases/latest)) must be used across all of them.
The following applies for [Scala 3](https://www.scala-lang.org/) projects built with [sbt 2](https://www.scala-sbt.org/).
Add the desired components to your `build.sbt` file:

```scala
// Core (required)
libraryDependencies += "com.alecdorrington" %% "megalingual" % "{version}"

// Laminar language picker component
libraryDependencies += "com.alecdorrington" %% "megalingual-laminar" % "{version}"
```

## 🗣️ Usage

_Megalingual_ knows 56 languages and varieties, as a tree (e.g. with British English beneath English), each
with its flag, the direction it's written in (left-to-right vs. right-to-left), its plural rule, and the full matrix of language names.

```scala
import com.alecdorrington.megalingual.*
import com.alecdorrington.megalingual.Language.*

French.name(in = German)  // "Französisch"
English.title(in = French) // "Anglais", as it begins a menu's item
Polish.plural(22)          // Plural.Few
Language.fromTag("es-MX")  // Some(LatinAmericanSpanish)
```

An app names the languages it supports in a `Localised`, with what it has in each, such as its
interface's text. A language outside them is read in the nearest one: a variety of it, else a kindred
one (traditional Chinese for Cantonese, Swiss Standard German for Swiss German), else its parent's
nearest, else the default. A reader's language is negotiated from
their choice and their browser's preferences, and the picker offers the supported languages, with
their flags and names, varieties beneath the languages they are varieties of, searched by their names
in any language:

```scala
import com.alecdorrington.megalingual.*
import com.alecdorrington.megalingual.Language.*
import com.alecdorrington.megalingual.laminar.*
import com.raquo.laminar.api.L.*

trait Strings:
  def languageLabel: String
  def greeting: String

object BritishStrings extends Strings:
  val languageLabel = "Language"
  val greeting      = "Hello"

object FrenchStrings extends Strings:
  val languageLabel = "Langue"
  val greeting      = "Bonjour"

val strings = Localised(BritishEnglish):
  case BritishEnglish => BritishStrings
  case French         => FrenchStrings

val reading = Var(strings.negotiate(chosen = None, Browser.preferences))

val app = div(
  Written.in(reading.signal),
  LanguagePicker(
    languages = strings.listed,
    chosen = reading.signal,
    choose = reading.writer.contramap: chosen =>
      strings.negotiate(Some(chosen), Browser.preferences),
    in = reading.signal,
    label = reading.signal.map(strings(_).languageLabel),
    ownFirst = true,
  ),
  p(child.text <-- reading.signal.map(strings(_).greeting)),
)
```

`Written.in(language)` marks an element as written in a language (`lang` and `dir`), and
`Intl` sorts and lists names as a language does. The picker's stylesheet goes in the page's head,
so a picker inside a shadow root is unstyled.

## 👁️ See also

- This library was made using [Scala Library Template](https://github.com/SgtSwagrid/scala-library-template).
