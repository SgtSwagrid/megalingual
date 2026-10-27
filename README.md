<div align="center">

  <h1>🌐 Megalingual</h1>
  <p>Languages with their names and flags, and a Laminar picker for choosing one.</p>
  
  <span>
    <a href="https://github.com/SgtSwagrid/megalingual/actions/workflows/build-integrity.yml"><img src="https://github.com/SgtSwagrid/megalingual/actions/workflows/build-integrity.yml/badge.svg" alt="Build status" /></a>
    <a href="https://search.maven.org/artifact/com.alecdorrington/megalingual_3"><img src="https://img.shields.io/maven-central/v/com.alecdorrington/megalingual_3.svg" alt="Maven Central" /></a>
    <a href="https://alecdorrington.com/megalingual"><img src="https://img.shields.io/badge/docs-latest-blue.svg" alt="Documentation" /></a>
  </span>
  
</div>

## ⬇️ Installation

Add the core to your `build.sbt`:

```scala
libraryDependencies += "com.alecdorrington" %% "megalingual" % "0.1.0"
```

And, for a [Laminar](https://laminar.dev/) app, the picker:

```scala
libraryDependencies += "com.alecdorrington" %% "megalingual-laminar" % "0.1.0"
```

The core is for the JVM and, through [Scala.js](https://www.scala-js.org/), for JavaScript, and the picker for
JavaScript alone. sbt 2 resolves `%%` to the right artefact (sbt 1 needs `%%%`).

## 🗣️ Usage

_Megalingual_ knows 56 languages and varieties, as a tree (British English beneath English), each
with its flag, the direction it is written in, its plural rule, and its name in every one of them.

```scala
import com.alecdorrington.megalingual.*
import com.alecdorrington.megalingual.Language.*

French.name(in = German)  // "Französisch"
English.title(in = French) // "Anglais", as it begins a menu's item
Polish.plural(22)          // Plural.Few
Language.fromTag("es-MX")  // Some(LatinAmericanSpanish)
```

An app names the languages it supports in a `Supported`, with what it has in each, such as its
interface's text. A language outside them is read in the nearest one: a variety of it, else its
parent's, else the default.

```scala
val interface = Supported(BritishEnglish):
  case BritishEnglish => BritishStrings
  case French         => FrenchStrings

val reading = interface.negotiate(chosen = None, Browser.preferences)
val strings = interface(reading)
```

The picker offers the supported languages, with their flags and names, varieties beneath the
languages they are varieties of:

```scala
import com.alecdorrington.megalingual.laminar.*

LanguagePicker(
  languages = interface.listed,
  chosen = language.signal,
  choose = language.writer,
  in = reading,
  label = "Language",
  ownFirst = true,
)
```

`Written.in(language)` marks an element as written in a language (`lang` and `dir`), and
`Intl` sorts and lists names as a language does.

## 👁️ See also

- This library was made using [Scala Library Template](https://github.com/SgtSwagrid/scala-library-template).
