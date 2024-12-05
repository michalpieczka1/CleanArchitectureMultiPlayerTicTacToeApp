package com.michal.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight

import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.unit.sp
import com.example.tictactoe.R

val provider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs
)

val bodyFontFamily = FontFamily(
    Font(
        googleFont = GoogleFont("Cairo"),
        fontProvider = provider,
    )
)

val displayFontFamily = FontFamily(
    Font(
        googleFont = GoogleFont("Coda"),
        fontProvider = provider,
    )
)

// Default Material 3 typography values
val baseline = Typography()

val AppTypography = Typography(
    displayLarge = baseline.displayLarge.copy(fontFamily = displayFontFamily, fontSize = 38.sp, lineHeight = 46.sp, fontWeight = FontWeight.Black),
    displayMedium = baseline.displayMedium.copy(fontFamily = displayFontFamily, fontSize = 32.sp, lineHeight = 40.sp, fontWeight = FontWeight.ExtraBold),
    displaySmall = baseline.displaySmall.copy(fontFamily = displayFontFamily, fontSize = 28.sp, lineHeight = 34.sp, fontWeight = FontWeight.Bold),
    headlineLarge = baseline.headlineLarge.copy(fontFamily = displayFontFamily, fontSize = 26.sp, lineHeight = 32.sp, fontWeight = FontWeight.SemiBold),
    headlineMedium = baseline.headlineMedium.copy(fontFamily = displayFontFamily, fontSize = 22.sp, lineHeight = 28.sp, fontWeight = FontWeight.Medium),
    headlineSmall = baseline.headlineSmall.copy(fontFamily = displayFontFamily, fontSize = 20.sp, lineHeight = 26.sp, fontWeight = FontWeight.Normal),
    titleLarge = baseline.titleLarge.copy(fontFamily = bodyFontFamily, fontSize = 18.sp, lineHeight = 24.sp, fontWeight = FontWeight.SemiBold),
    titleMedium = baseline.titleMedium.copy(fontFamily = bodyFontFamily, fontSize = 16.sp, lineHeight = 22.sp, fontWeight = FontWeight.Medium),
    titleSmall = baseline.titleSmall.copy(fontFamily = bodyFontFamily, fontSize = 14.sp, lineHeight = 20.sp, fontWeight = FontWeight.Light),
    bodyLarge = baseline.bodyLarge.copy(fontFamily = bodyFontFamily, fontSize = 16.sp, lineHeight = 24.sp, fontWeight = FontWeight.Normal),
    bodyMedium = baseline.bodyMedium.copy(fontFamily = bodyFontFamily, fontSize = 14.sp, lineHeight = 20.sp, fontWeight = FontWeight.Light),
    bodySmall = baseline.bodySmall.copy(fontFamily = bodyFontFamily, fontSize = 12.sp, lineHeight = 18.sp, fontWeight = FontWeight.Thin),
    labelLarge = baseline.labelLarge.copy(fontFamily = bodyFontFamily, fontSize = 14.sp, lineHeight = 20.sp, fontWeight = FontWeight.Medium),
    labelMedium = baseline.labelMedium.copy(fontFamily = bodyFontFamily, fontSize = 12.sp, lineHeight = 16.sp, fontWeight = FontWeight.Normal),
    labelSmall = baseline.labelSmall.copy(fontFamily = bodyFontFamily, fontSize = 10.sp, lineHeight = 14.sp, fontWeight = FontWeight.Light),
)

