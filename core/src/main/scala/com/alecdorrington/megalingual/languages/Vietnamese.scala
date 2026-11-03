package com.alecdorrington.megalingual
package languages

import com.alecdorrington.megalingual.Direction.LeftToRight

object Vietnamese extends Language.Root("vi", LeftToRight):

  override lazy val flag: Flag = Flag.drawn(
    """<rect width="60" height="40" fill="#da251d"/>""" +
      Flag.star(30, 20, 12, "#ffff00"),
  )

  override def plurals: Plural.Rule = Plural.invariable

  override def names: PartialFunction[Language, String] =
    case Arabic               => "tiếng Ả Rập"
    case Bengali              => "tiếng Bengal"
    case Bulgarian            => "tiếng Bulgaria"
    case Catalan              => "tiếng Catalan"
    case Chinese              => "tiếng Trung"
    case SimplifiedChinese    => "tiếng Trung giản thể"
    case TraditionalChinese   => "tiếng Trung phồn thể"
    case TaiwaneseChinese     => "tiếng Trung phồn thể (Đài Loan)"
    case Cantonese            => "tiếng Quảng Đông"
    case Croatian             => "tiếng Croatia"
    case Czech                => "tiếng Séc"
    case Danish               => "tiếng Đan Mạch"
    case Dutch                => "tiếng Hà Lan"
    case English              => "tiếng Anh"
    case BritishEnglish       => "tiếng Anh (Anh)"
    case AmericanEnglish      => "tiếng Anh (Mỹ)"
    case AustralianEnglish    => "tiếng Anh (Úc)"
    case Estonian             => "tiếng Estonia"
    case Filipino             => "tiếng Filipino"
    case Finnish              => "tiếng Phần Lan"
    case French               => "tiếng Pháp"
    case German               => "tiếng Đức"
    case GermanGerman         => "tiếng Đức (Đức)"
    case GermanOfSwitzerland  => "tiếng Đức (Thụy Sĩ)"
    case SwissStandardGerman  => "tiếng Thượng Giéc-man (Thụy Sĩ)"
    case SwissGerman          => "tiếng Đức Thụy Sĩ"
    case Greek                => "tiếng Hy Lạp"
    case Hebrew               => "tiếng Do Thái"
    case Hindi                => "tiếng Hindi"
    case Hungarian            => "tiếng Hungary"
    case Indonesian           => "tiếng Indonesia"
    case Italian              => "tiếng Ý"
    case Japanese             => "tiếng Nhật"
    case Korean               => "tiếng Hàn"
    case Latvian              => "tiếng Latvia"
    case Lithuanian           => "tiếng Litva"
    case Malay                => "tiếng Mã Lai"
    case Norwegian            => "tiếng Na Uy"
    case Persian              => "tiếng Ba Tư"
    case Polish               => "tiếng Ba Lan"
    case Portuguese           => "tiếng Bồ Đào Nha"
    case BrazilianPortuguese  => "tiếng Bồ Đào Nha (Brazil)"
    case EuropeanPortuguese   => "tiếng Bồ Đào Nha (châu Âu)"
    case Romanian             => "tiếng Romania"
    case Russian              => "tiếng Nga"
    case Serbian              => "tiếng Serbia"
    case Slovak               => "tiếng Slovak"
    case Slovenian            => "tiếng Slovenia"
    case Spanish              => "tiếng Tây Ban Nha"
    case EuropeanSpanish      => "tiếng Tây Ban Nha (châu Âu)"
    case LatinAmericanSpanish => "tiếng Tây Ban Nha (Mỹ La tinh)"
    case Swahili              => "tiếng Swahili"
    case Swedish              => "tiếng Thụy Điển"
    case Tamil                => "tiếng Tamil"
    case Thai                 => "tiếng Thái"
    case Turkish              => "tiếng Thổ Nhĩ Kỳ"
    case Ukrainian            => "tiếng Ukraina"
    case Urdu                 => "tiếng Urdu"
    case Vietnamese           => "tiếng Việt"
