package com.alecdorrington.megalingual
package languages

import com.alecdorrington.megalingual.Direction.LeftToRight

object Korean extends Language.Root("ko", LeftToRight):

  override lazy val flag: Flag = Flag.drawn(
    """<rect width="60" height="40" fill="#ffffff"/>""" +
      """<circle cx="30" cy="20" r="10" fill="#0047a0"/>""" +
      """<path d="M20,20 A10,10 0 0 1 40,20 A5,5 0 0 1 30,20 """ +
      """A5,5 0 0 0 20,20 Z" fill="#cd2e3a"/>""" + List(
        (15.9, 10.6, -56.3),
        (44.1, 29.4, -56.3),
        (44.1, 10.6, 56.3),
        (15.9, 29.4, 56.3),
      ).map((x, y, angle) =>
          """<rect x="-4" y="-2.5" width="8" height="5" fill="#000000" """ +
            s"""transform="translate($x,$y) rotate($angle)"/>""",
        )
        .mkString,
  )

  override def plurals: Plural.Rule = Plural.invariable

  override def names: PartialFunction[Language, String] =
    case Arabic               => "아랍어"
    case Bengali              => "벵골어"
    case Bulgarian            => "불가리아어"
    case Catalan              => "카탈로니아어"
    case Chinese              => "중국어"
    case SimplifiedChinese    => "중국어(간체)"
    case TraditionalChinese   => "중국어(번체)"
    case TaiwaneseChinese     => "중국어(번체, 대만)"
    case Cantonese            => "광둥어"
    case Croatian             => "크로아티아어"
    case Czech                => "체코어"
    case Danish               => "덴마크어"
    case Dutch                => "네덜란드어"
    case English              => "영어"
    case BritishEnglish       => "영어(영국)"
    case AmericanEnglish      => "영어(미국)"
    case AustralianEnglish    => "영어(호주)"
    case Estonian             => "에스토니아어"
    case Filipino             => "필리핀어"
    case Finnish              => "핀란드어"
    case French               => "프랑스어"
    case German               => "독일어"
    case GermanGerman         => "독일어(독일)"
    case GermanOfSwitzerland  => "독일어(스위스)"
    case SwissStandardGerman  => "고지 독일어(스위스)"
    case SwissGerman          => "스위스 독일어"
    case Greek                => "그리스어"
    case Hebrew               => "히브리어"
    case Hindi                => "힌디어"
    case Hungarian            => "헝가리어"
    case Indonesian           => "인도네시아어"
    case Italian              => "이탈리아어"
    case Japanese             => "일본어"
    case Korean               => "한국어"
    case Latvian              => "라트비아어"
    case Lithuanian           => "리투아니아어"
    case Malay                => "말레이어"
    case Norwegian            => "노르웨이어"
    case Persian              => "페르시아어"
    case Polish               => "폴란드어"
    case Portuguese           => "포르투갈어"
    case BrazilianPortuguese  => "포르투갈어(브라질)"
    case EuropeanPortuguese   => "포르투갈어(포르투갈)"
    case Romanian             => "루마니아어"
    case Russian              => "러시아어"
    case Serbian              => "세르비아어"
    case Slovak               => "슬로바키아어"
    case Slovenian            => "슬로베니아어"
    case Spanish              => "스페인어"
    case EuropeanSpanish      => "스페인어(스페인)"
    case LatinAmericanSpanish => "스페인어(라틴 아메리카)"
    case Swahili              => "스와힐리어"
    case Swedish              => "스웨덴어"
    case Tamil                => "타밀어"
    case Thai                 => "태국어"
    case Turkish              => "튀르키예어"
    case Ukrainian            => "우크라이나어"
    case Urdu                 => "우르두어"
    case Vietnamese           => "베트남어"
