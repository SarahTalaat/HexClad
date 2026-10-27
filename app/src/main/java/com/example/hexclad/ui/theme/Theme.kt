package com.example.hexclad.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val HexCladBlack = Color(0xFF171717)
private val HexCladCream = Color(0xFFF7F6F2)
private val HexCladLime = Color(0xFFD7FF00)
private val HexCladMuted = Color(0xFF6B6B67)
private val HexCladDarkSurface = Color(0xFF242424)

private val LightColorScheme = lightColorScheme(
    primary = HexCladLime,
    onPrimary = HexCladBlack,
    secondary = HexCladBlack,
    onSecondary = Color.White,
    tertiary = HexCladMuted,
    background = HexCladCream,
    onBackground = HexCladBlack,
    surface = HexCladCream,
    onSurface = HexCladBlack,
    surfaceVariant = Color(0xFFECEBE5),
    onSurfaceVariant = HexCladMuted,
    outline = Color(0xFFB8B7B0)
)

private val DarkColorScheme = darkColorScheme(
    primary = HexCladLime,
    onPrimary = HexCladBlack,
    secondary = Color(0xFFE8E8E3),
    onSecondary = HexCladBlack,
    tertiary = Color(0xFFC2C2BB),
    background = HexCladBlack,
    onBackground = Color(0xFFF7F6F2),
    surface = HexCladDarkSurface,
    onSurface = Color(0xFFF7F6F2),
    surfaceVariant = Color(0xFF333333),
    onSurfaceVariant = Color(0xFFC2C2BB),
    outline = Color(0xFF777770)
)

/**
 * Uses a fixed HexClad palette rather than Android dynamic colors so the brand appearance
 * remains consistent across devices. Dark mode follows the system setting.
 */
@Composable
fun HexCladTheme(
    darkTheme: Boolean = false,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
