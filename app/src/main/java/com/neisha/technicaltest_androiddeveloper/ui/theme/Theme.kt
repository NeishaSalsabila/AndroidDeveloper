package com.neisha.technicaltest_androiddeveloper.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val PrimaryBlue = Color(0xFF5B7FFF)
val PrimaryPurple = Color(0xFF8B5CF6)

val PrimaryBlueLight = Color(0xFFEEF2FF)
val PurpleLight = Color(0xFFFAF5FF)

val GreenLight = Color(0xFFECFDF5)
val GreenText = Color(0xFF059669)

val PurpleText = Color(0xFF9333EA)

val AppBackground = Color(0xFFF4F5FB)
val CardSurface = Color(0xFFFFFFFF)

val TextPrimary = Color(0xFF1A1A2E)
val TextSecondary = Color(0xFFB0B0C0)
val TextHint = Color(0xFFD0D0D8)

val BorderLight = Color(0xFFEBEBEB)

private val LightColorScheme = lightColorScheme(
    primary = PrimaryBlue,
    onPrimary = Color.White,

    secondary = PrimaryPurple,
    onSecondary = Color.White,

    tertiary = GreenText,

    background = AppBackground,
    onBackground = TextPrimary,

    surface = CardSurface,
    onSurface = TextPrimary,

    surfaceVariant = PrimaryBlueLight,

    error = Color(0xFFE53935)
)

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryBlue,
    onPrimary = Color.White,

    secondary = PrimaryPurple,
    onSecondary = Color.White,

    tertiary = GreenText,

    background = Color(0xFF12121E),
    onBackground = Color(0xFFECECFF),

    surface = Color(0xFF1E1E30),
    onSurface = Color(0xFFECECFF)
)

@Composable
fun TechnicalTestAndroidDeveloperTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme =
        if (darkTheme) DarkColorScheme
        else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}