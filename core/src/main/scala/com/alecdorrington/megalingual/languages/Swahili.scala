package com.alecdorrington.megalingual
package languages

import com.alecdorrington.megalingual.Direction.LeftToRight

object Swahili extends Language.Root("sw", LeftToRight):

  override lazy val flag: Flag = Flag.drawn(
    """<rect width="60" height="40" fill="#00a3dd"/>""" +
      """<polygon points="0,0 60,0 0,40" fill="#1eb53a"/>""" +
      """<path d="M0,40 L60,0" stroke="#fcd116" stroke-width="14"/>""" +
      """<path d="M0,40 L60,0" stroke="#000000" stroke-width="9"/>""",
  )

  override def plurals: Plural.Rule = Plural.oneOrOther

  override def names: PartialFunction[Language, String] =
    case Arabic               => "Kiarabu"
    case Bengali              => "Kibengali"
    case Bulgarian            => "Kibulgaria"
    case Catalan              => "Kikatalani"
    case Chinese              => "Kichina"
    case SimplifiedChinese    => "Kichina kilichorahisishwa"
    case TraditionalChinese   => "Kichina cha jadi"
    case TaiwaneseChinese     => "Kichina cha jadi (Taiwan)"
    case Cantonese            => "Kikantoni"
    case Croatian             => "Kikroeshia"
    case Czech                => "Kicheki"
    case Danish               => "Kidenmaki"
    case Dutch                => "Kiholanzi"
    case English              => "Kiingereza"
    case BritishEnglish       => "Kiingereza (Uingereza)"
    case AmericanEnglish      => "Kiingereza (Marekani)"
    case AustralianEnglish    => "Kiingereza (Australia)"
    case Estonian             => "Kiestonia"
    case Filipino             => "Kifilipino"
    case Finnish              => "Kifini"
    case French               => "Kifaransa"
    case German               => "Kijerumani"
    case GermanGerman         => "Kijerumani (Ujerumani)"
    case GermanOfSwitzerland  => "Kijerumani (Uswisi)"
    case SwissStandardGerman  => "Kijerumani sanifu cha Uswisi"
    case SwissGerman          => "Kijerumani cha Uswisi"
    case Greek                => "Kigiriki"
    case Hebrew               => "Kiebrania"
    case Hindi                => "Kihindi"
    case Hungarian            => "Kihungaria"
    case Indonesian           => "Kiindonesia"
    case Italian              => "Kiitaliano"
    case Japanese             => "Kijapani"
    case Korean               => "Kikorea"
    case Latvian              => "Kilatvia"
    case Lithuanian           => "Kilithuania"
    case Malay                => "Kimalei"
    case Norwegian            => "Kinorwe"
    case Persian              => "Kiajemi"
    case Polish               => "Kipolandi"
    case Portuguese           => "Kireno"
    case BrazilianPortuguese  => "Kireno (Brazil)"
    case EuropeanPortuguese   => "Kireno (Ulaya)"
    case Romanian             => "Kiromania"
    case Russian              => "Kirusi"
    case Serbian              => "Kiserbia"
    case Slovak               => "Kislovakia"
    case Slovenian            => "Kislovenia"
    case Spanish              => "Kihispania"
    case EuropeanSpanish      => "Kihispania (Ulaya)"
    case LatinAmericanSpanish => "Kihispania (Amerika ya Kilatini)"
    case Swahili              => "Kiswahili"
    case Swedish              => "Kiswidi"
    case Tamil                => "Kitamili"
    case Thai                 => "Kithai"
    case Turkish              => "Kituruki"
    case Ukrainian            => "Kiukraini"
    case Urdu                 => "Kiurdu"
    case Vietnamese           => "Kivietinamu"
