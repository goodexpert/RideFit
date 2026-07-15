package org.goodexpert.ridefit.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.unit.sp
import org.goodexpert.ridefit.R

private val provider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs,
)

private val notoSansKr = GoogleFont("Noto Sans KR")

val NotoSansKrFamily = FontFamily(
    Font(googleFont = notoSansKr, fontProvider = provider, weight = FontWeight.Normal),
    Font(googleFont = notoSansKr, fontProvider = provider, weight = FontWeight.Bold),
    Font(googleFont = notoSansKr, fontProvider = provider, weight = FontWeight.Black),
)

val Typography = Typography(
    // Screen title — 28sp / Black
    headlineLarge = TextStyle(
        fontFamily = NotoSansKrFamily,
        fontWeight = FontWeight.Black,
        fontSize = 28.sp,
        lineHeight = 36.sp,
        letterSpacing = 0.sp,
    ),
    // Button label — 22sp / Black
    titleLarge = TextStyle(
        fontFamily = NotoSansKrFamily,
        fontWeight = FontWeight.Black,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp,
    ),
    // Card title — 18sp / Bold
    titleMedium = TextStyle(
        fontFamily = NotoSansKrFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
        lineHeight = 26.sp,
        letterSpacing = 0.sp,
    ),
    // Body / voice script — 16sp / Regular
    bodyLarge = TextStyle(
        fontFamily = NotoSansKrFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.sp,
    ),
    bodyMedium = TextStyle(
        fontFamily = NotoSansKrFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.sp,
    ),
    // Hint / secondary — 12sp (minimum allowed)
    labelSmall = TextStyle(
        fontFamily = NotoSansKrFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.sp,
    ),
)
