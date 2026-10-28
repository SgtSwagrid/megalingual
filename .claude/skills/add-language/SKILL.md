---
name: add-language
description: Add a language to Megalingual (or revise one's names, flag or plural rule). Use when asked to add a language or a variety, or to fix how one is named.
---

# Add a language to Megalingual

A language is a case of `enum Language` in `core/src/main/scala/com/alecdorrington/megalingual/Language.scala`, and everything
the library knows of it is written by hand: its flag, its direction, its plural rule and its
names. Nothing is generated from CLDR or anything else; write the data yourself, checking it
against CLDR where that helps. Then:

1. **The case.** Add it to `Language` in alphabetical order of its English name, with a variety
   after its parent (and after the parent's other varieties): its BCP 47 code, as specific as
   tells it from the others; its `Direction`; and its `parent` if it is a variety of a language
   already there.
   - Its flag goes in `Flag.of` (`core/src/main/scala/com/alecdorrington/megalingual/Flag.scala`): `bands(...)` for
     stripes, otherwise `drawn(...)`, a few SVG shapes on a canvas 60 units wide by 40 high, as
     a Scala string.
   - A second variety of a language turns it into one with varieties: add a case for each
     beneath it (`de-DE` and `de-CH` beneath `de`), give the parent a tile of its code in
     `Flag.of`, and list first among its children the variety a reader naming only the language
     should get. Codes may change: nothing keeps old ones readable.
   - If browsers name it by tags that would otherwise reach another language, or none (`es-MX`
     for `es-419`, `no` for `nb`), list them in `Language.claims`. If its readers read another
     language outside its tree best when it is missing, as Cantonese's read traditional Chinese,
     list each in the other's `Language.akin` (the link goes both ways). It changes only which
     supported language `Localised` falls back to.
2. **Its plural rule.** Add it to the match in `Plural.of` (`core/src/main/scala/com/alecdorrington/megalingual/Plural.scala`):
   CLDR's cardinal rule for whole numbers. A variety goes with its parent unless its rule differs.
3. **Its name in every language.** Add a case to every `In*.scala` in
   `core/src/main/scala/com/alecdorrington/megalingual/names/`, in the same place as in `Language`. Each name is the one a
   native writer of that language uses, as it reads mid-sentence (lower case in French,
   "anglais"); `Language.title` capitalises it for menus. Within one file no two languages may
   share a name, so a parent and its varieties all differ.
4. **Every language's name in it.** Unless a variety names languages exactly as its parent's
   file does (then add it to that file's case in `Names.apply`), write `In<Language>.scala`,
   copying `InEnglish.scala`'s shape: every case, in `Language`'s order, no wildcard. A variety
   that names only a few differently overrides those and ends with `case _ => In<Other>(language)`,
   falling back to the object it overrides, as `InEuropeanPortuguese.scala` does to `InPortuguese`. Add its case to `Names.apply`.
5. **Check.** Run `sbt "megalingual/test; megalingualJS/test"` with `-Werror`
   (`sbt 'set ThisBuild / scalacOptions += "-Werror"; Test/compile'`, as CI does): a match
   missing the new case fails to compile. The tests check that every language is named in every
   other, that no name repeats within a language, that every code finds its language, and that
   every language has a plural category for every count. Add a row for the new language to
   `PluralSuite`'s table, in `Language`'s order, even if it shares a rule with another (with the
   rule's counts, if the rule is new): the table names every language, each with its rule.

Apps using Megalingual need no change: they name the languages they support in a `Localised`,
never all of them, and pickers offer what the app lists.
