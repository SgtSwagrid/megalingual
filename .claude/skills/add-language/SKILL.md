---
name: add-language
description: Add a language to Megalingual (or revise one's names, flag or plural rule). Use when asked to add a language or a variety, or to fix how one is named.
---

# Add a language to Megalingual

A language is an `object` in a file of its own, `core/src/main/scala/com/alecdorrington/megalingual/languages/<Name>.scala`
(package `com.alecdorrington.megalingual.languages`), and everything the library knows of it is written there by
hand: its code, direction, flag, plural rule and the names it gives languages. Nothing is generated from CLDR or
anything else; write the data yourself, checking it against CLDR where that helps. Then:

1. **Its file.** Copy the shape of a language like it (`Polish.scala` for a root, `BritishEnglish.scala` for a
   variety), with its Scaladoc where something about it needs saying.
   - A language that is no variety of another extends `Language.Root("<code>", <Direction>)`; a variety extends
     `Language.Variety("<code>", of = <Parent>)`, and takes its parent's direction, plural rule and names unless it
     overrides them. The code is its BCP 47 tag, as specific as tells it from the others.
   - Its flag, `override lazy val flag: Flag`, drawn with `Flag`'s shapes: `Flag.bands(...)` for stripes, otherwise
     `Flag.drawn(...)`, a few SVG shapes on a canvas 60 units wide by 40 high, as a Scala string, with `Flag.star`
     and `Flag.canton` where they help. A flag another language has already drawn is taken from it (`Hindi.flag`).
   - Its plural rule, `override def plurals: Plural.Rule` (a root's, or a variety's that differs from its parent's):
     CLDR's cardinal rule for whole numbers, one of `Plural`'s shared rules (`Plural.oneOrOther`) where one fits,
     else its own, `n => ...`, of counts never negative.
   - If browsers name it by tags that would otherwise reach another language, or none (`es-MX` for `es-419`, `no`
     for `nb`), list them in `override def claims`.
   - A second variety of a language turns it into one with varieties: add a file for each beneath it (`en-GB` and
     `en-US` beneath `en`), give the parent `Flag.tile(code)`, and list first among its children the variety a reader
     naming only the language should get. Codes may change: nothing keeps old ones readable.
   - A missing language is read in its siblings before its parent's, so where its readers read one sibling best, as
     Cantonese's read Taiwan's traditional Chinese, the two share a parent of their own (`zh-Hant` above `zh-TW` and
     `zh-HK`, `de-CH` above Swiss Standard German and Swiss German), which is then a language with varieties like
     any other.
2. **List it.** Add it to `Languages.builtIn` (`Languages.scala`) and to the aliases in `Language`'s companion
   (`val <Name>: Language = languages.<Name>`, in `Language.scala`), both in alphabetical order of its English name,
   with a variety after its parent (and after the parent's other varieties).
3. **Its name in every language.** Add a case to the `names` of every root, in the same place as in
   `Languages.builtIn`, and to a variety's only where it names the language otherwise than its parent. Each name is
   the one a native writer of that language uses, as it reads mid-sentence (lower case in French, "anglais");
   `Language.title` capitalises it for menus. Within one language no two languages may share a name, so a parent and
   its varieties all differ.
4. **Every language's name in it.** A root's `names` names every language, itself too, in `Languages.builtIn`'s
   order, as `override def names: PartialFunction[Language, String]` of `case <Language> => "<name>"`. A variety's
   names only those it names otherwise than its parent, as `EuropeanPortuguese.scala` does, or none at all.
5. **Check.** Run `sbt "scalafmtAll; testFull"`, with `WERROR=1` set as CI has it. The tests check that every
   language names every other in its `names` (or its parent's), that no name repeats within a language, that no
   variety repeats its parent's name, that every code finds its language, and that every language has a plural
   category for every count. Add a row for the new language to `PluralSuite`'s table, in `Languages.builtIn`'s
   order, even if it shares a rule with another (with the rule's counts, if the rule is new): the table names every
   language, each with its rule, and must equal `Languages.builtIn`.

Apps using Megalingual need no change: they name the languages they support in a `Localised`, never all of them, and
pickers offer what the app lists. An app or library that needs a language Megalingual lacks can declare it the same
way in its own code, as a top-level object (or one within an object), and give `Languages.builtIn ++ List(<it>)`
where languages are looked for; its `called` names it in Megalingual's languages, which cannot name it themselves.
