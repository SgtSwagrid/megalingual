package com.alecdorrington.megalingual
package languages

import com.alecdorrington.megalingual.Direction.LeftToRight

object Greek extends Language.Root("el", LeftToRight):

  override lazy val flag: Flag = Flag.drawn(
    """<rect width="60" height="40" fill="#0d5eaf"/>""" +
      """<path d="M0,6.67 H60 M0,15.56 H60 M0,24.44 H60 M0,33.33 H60" """ +
      """stroke="#ffffff" stroke-width="4.44"/>""" +
      """<rect width="22.22" height="22.22" fill="#0d5eaf"/>""" +
      """<path d="M11.11,0 V22.22 M0,11.11 H22.22" stroke="#ffffff" """ +
      """stroke-width="4.44"/>""",
  )

  override def plurals: Plural.Rule = Plural.oneOrOther

  override def names: PartialFunction[Language, String] =
    case Arabic               => "αραβικά"
    case Bengali              => "βεγγαλικά"
    case Bulgarian            => "βουλγαρικά"
    case Catalan              => "καταλανικά"
    case Chinese              => "κινεζικά"
    case SimplifiedChinese    => "απλοποιημένα κινεζικά"
    case TraditionalChinese   => "παραδοσιακά κινεζικά"
    case TaiwaneseChinese     => "παραδοσιακά κινεζικά (Ταϊβάν)"
    case Cantonese            => "καντονέζικα"
    case Croatian             => "κροατικά"
    case Czech                => "τσεχικά"
    case Danish               => "δανικά"
    case Dutch                => "ολλανδικά"
    case English              => "αγγλικά"
    case BritishEnglish       => "αγγλικά Βρετανίας"
    case AmericanEnglish      => "αγγλικά Αμερικής"
    case AustralianEnglish    => "αγγλικά Αυστραλίας"
    case Estonian             => "εσθονικά"
    case Filipino             => "φιλιππινικά"
    case Finnish              => "φινλανδικά"
    case French               => "γαλλικά"
    case German               => "γερμανικά"
    case GermanGerman         => "γερμανικά (Γερμανία)"
    case GermanOfSwitzerland  => "γερμανικά (Ελβετία)"
    case SwissStandardGerman  => "υψηλά γερμανικά Ελβετίας"
    case SwissGerman          => "ελβετικά γερμανικά"
    case Greek                => "ελληνικά"
    case Hebrew               => "εβραϊκά"
    case Hindi                => "χίντι"
    case Hungarian            => "ουγγρικά"
    case Indonesian           => "ινδονησιακά"
    case Italian              => "ιταλικά"
    case Japanese             => "ιαπωνικά"
    case Korean               => "κορεατικά"
    case Latvian              => "λετονικά"
    case Lithuanian           => "λιθουανικά"
    case Malay                => "μαλαισιανά"
    case Norwegian            => "νορβηγικά"
    case Persian              => "περσικά"
    case Polish               => "πολωνικά"
    case Portuguese           => "πορτογαλικά"
    case BrazilianPortuguese  => "πορτογαλικά Βραζιλίας"
    case EuropeanPortuguese   => "πορτογαλικά Ευρώπης"
    case Romanian             => "ρουμανικά"
    case Russian              => "ρωσικά"
    case Serbian              => "σερβικά"
    case Slovak               => "σλοβακικά"
    case Slovenian            => "σλοβενικά"
    case Spanish              => "ισπανικά"
    case EuropeanSpanish      => "ισπανικά Ευρώπης"
    case LatinAmericanSpanish => "ισπανικά Λατινικής Αμερικής"
    case Swahili              => "σουαχίλι"
    case Swedish              => "σουηδικά"
    case Tamil                => "ταμιλικά"
    case Thai                 => "ταϊλανδικά"
    case Turkish              => "τουρκικά"
    case Ukrainian            => "ουκρανικά"
    case Urdu                 => "ούρντου"
    case Vietnamese           => "βιετναμικά"
