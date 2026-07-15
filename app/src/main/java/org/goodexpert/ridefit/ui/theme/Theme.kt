package org.goodexpert.ridefit.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

/**
 * App-specific semantic colours layered on top of Material 3's [MaterialTheme.colorScheme].
 *
 * Every colour used by the screens should be a token here — no hardcoded hex in composables.
 * To add a new theme, create another [RideFitColors] instance (like [LightRideFitColors] /
 * [DarkRideFitColors]) and wire it in [RideFitTheme]; screen code stays unchanged.
 */
data class RideFitColors(
    // Brand
    val brand: Color,
    val brandAccent: Color,
    val confirmGreen: Color,
    val emergencyRed: Color,
    val emergencyRedPressed: Color,
    val onBackgroundSecondary: Color,
    // Surfaces & borders — one brand-tinted neutral ramp
    val cardContainer: Color,   // card / elevated surface
    val subtleContainer: Color, // tonal button bg, selection, tints
    val cardBorder: Color,      // hairline borders
    // Confirm / success
    val confirmContainer: Color,
    val onConfirmContainer: Color,
    // Emergency / danger
    val emergencyContainer: Color,
    val emergencyBorder: Color,
    val onEmergency: Color,
    val onEmergencyVariant: Color,
    // Warning / amber
    val warningContainer: Color,
    val warningBorder: Color,
    val onWarning: Color,
    val onWarningVariant: Color,
    // Muted text on surface
    val onSurfaceFaint: Color,
    val isDark: Boolean,
)

val LightRideFitColors = RideFitColors(
    brand = BrandBlue,
    brandAccent = BrandBlue,
    confirmGreen = ConfirmGreen,
    emergencyRed = EmergencyRed,
    emergencyRedPressed = Color(0xFFB91C1C),
    onBackgroundSecondary = DayOnBackgroundSecondary,
    cardContainer = DaySurface,
    subtleContainer = Color(0xFFEAEEF7),
    cardBorder = Color(0xFFD6DEF0),
    confirmContainer = Color(0xFFDCFCE7),
    onConfirmContainer = Color(0xFF15803D),
    emergencyContainer = Color(0xFFFFF5F5),
    emergencyBorder = Color(0xFFFFCCCC),
    onEmergency = Color(0xFFCC2222),
    onEmergencyVariant = Color(0xFFAA4444),
    warningContainer = Color(0xFFFFF9EC),
    warningBorder = Color(0xFFFFE4A0),
    onWarning = Color(0xFF92400E),
    onWarningVariant = Color(0xFFA85C10),
    onSurfaceFaint = Color(0xFF6677AA),
    isDark = false,
)

val DarkRideFitColors = RideFitColors(
    brand = BrandBlue,
    brandAccent = Color(0xFF5577FF),
    confirmGreen = ConfirmGreen,
    emergencyRed = EmergencyRed,
    emergencyRedPressed = Color(0xFFB91C1C),
    onBackgroundSecondary = NightOnBackgroundSecondary,
    cardContainer = Color(0xFF14142A),
    subtleContainer = Color(0xFF1C1C38),
    cardBorder = Color(0xFF2A2A4A),
    confirmContainer = Color(0xFF052E16),
    onConfirmContainer = Color(0xFF4ADE80),
    emergencyContainer = Color(0xFF1A0808),
    emergencyBorder = Color(0xFF4A1515),
    onEmergency = Color(0xFFFF5555),
    onEmergencyVariant = Color(0xFF883333),
    warningContainer = Color(0xFF1A1400),
    warningBorder = Color(0xFF3A2800),
    onWarning = Color(0xFFFBBF24),
    onWarningVariant = Color(0xFF9A7010),
    onSurfaceFaint = Color(0xFF4A4A7A),
    isDark = true,
)

val LocalRideFitColors = staticCompositionLocalOf { LightRideFitColors }

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
    val rideFitColors = if (darkTheme) DarkRideFitColors else LightRideFitColors

    CompositionLocalProvider(LocalRideFitColors provides rideFitColors) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content,
        )
    }
}
