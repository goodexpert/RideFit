package org.goodexpert.ridefit.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

data class RideFitColors(
    val brand: Color,
    val confirmGreen: Color,
    val emergencyRed: Color,
    val onBackgroundSecondary: Color,
    val isDark: Boolean,
)

val LocalRideFitColors = staticCompositionLocalOf {
    RideFitColors(
        brand = BrandBlue,
        confirmGreen = ConfirmGreen,
        emergencyRed = EmergencyRed,
        onBackgroundSecondary = DayOnBackgroundSecondary,
        isDark = false,
    )
}

val MaterialTheme.rideFitColors: RideFitColors
    @Composable get() = LocalRideFitColors.current

private val LightColorScheme = lightColorScheme(
    primary = BrandBlue,
    onPrimary = Color.White,
    secondary = ConfirmGreen,
    onSecondary = Color.White,
    tertiary = EmergencyRed,
    onTertiary = Color.White,
    background = DayBackground,
    onBackground = DayOnBackground,
    surface = DaySurface,
    onSurface = DayOnBackground,
)

private val DarkColorScheme = darkColorScheme(
    primary = BrandBlue,
    onPrimary = Color.White,
    secondary = ConfirmGreen,
    onSecondary = Color.White,
    tertiary = EmergencyRed,
    onTertiary = Color.White,
    background = NightBackground,
    onBackground = NightOnBackground,
    surface = NightSurface,
    onSurface = NightOnBackground,
)

@Composable
fun RideFitTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    val rideFitColors = if (darkTheme) {
        RideFitColors(
            brand = BrandBlue,
            confirmGreen = ConfirmGreen,
            emergencyRed = EmergencyRed,
            onBackgroundSecondary = NightOnBackgroundSecondary,
            isDark = true,
        )
    } else {
        RideFitColors(
            brand = BrandBlue,
            confirmGreen = ConfirmGreen,
            emergencyRed = EmergencyRed,
            onBackgroundSecondary = DayOnBackgroundSecondary,
            isDark = false,
        )
    }

    CompositionLocalProvider(LocalRideFitColors provides rideFitColors) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content,
        )
    }
}
