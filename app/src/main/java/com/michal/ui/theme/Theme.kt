package com.michal.ui.theme
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val LightPrimaryColor = Color(0xFF8E24AA)         // Light Purple
private val LightOnPrimary = Color(0xFFFFFFFF)            // White text on Primary
private val LightPrimaryContainer = Color(0xFFF3E5F5)     // Lighter Purple Container
private val LightOnPrimaryContainer = Color(0xFF4A0072)   // Dark Purple text on Container

private val LightSecondaryColor = Color(0xFFFFFFFF)       // White for light theme
private val LightOnSecondary = Color(0xFF000000)          // Black text on Secondary
private val LightSecondaryContainer = Color(0xFFE0E0E0)   // Light Gray Container
private val LightOnSecondaryContainer = Color(0xFF303030) // Darker Gray text on Container

private val LightTertiaryColor = Color(0xFFF39C12)        // Light Orange
private val LightOnTertiary = Color(0xFFFFFFFF)           // Black text on Tertiary
private val LightTertiaryContainer = Color(0xFFFFE0B2)    // Light Orange Container
private val LightOnTertiaryContainer = Color(0xFF5D4037)  // Dark Brown text on Container

private val LightBackground = Color(0xFFF5F5F5)           // Light gray for background
private val LightOnBackground = Color(0xFF000000)         // Black text on Background
private val LightSurface = Color(0xFFFFFFFF)              // White for surface elements
private val LightOnSurface = Color(0xFF000000)            // Black text on Surface
private val LightSurfaceVariant = Color(0xFFE0E0E0)       // Light Gray Surface Variant
private val LightOnSurfaceVariant = Color(0xFF616161)     // Medium Gray text on Surface Variant

private val LightOutline = Color(0xFFBDBDBD)              // Light Gray for borders

// Dark Theme Colors
private val DarkPrimaryColor = Color(0xFF7B1FA2)          // Darker Purple
private val DarkOnPrimary = Color(0xFFFFFFFF)             // White text on Primary
private val DarkPrimaryContainer = Color(0xFF4A0072)      // Dark Purple Container
private val DarkOnPrimaryContainer = Color(0xFFE1BEE7)    // Lighter Purple text on Container

private val DarkSecondaryColor = Color(0xFF000000)        // Black for dark theme
private val DarkOnSecondary = Color(0xFFFFFFFF)           // White text on Secondary
private val DarkSecondaryContainer = Color(0xFF424242)    // Dark Gray Container
private val DarkOnSecondaryContainer = Color(0xFFE0E0E0)  // Light Gray text on Container

private val DarkTertiaryColor = Color(0xFFD35400)         // Soft, muted Orange
private val DarkOnTertiary = Color(0xFF000000)            // Black text on Tertiary
private val DarkTertiaryContainer = Color(0xFF5D4037)     // Dark Brown Container
private val DarkOnTertiaryContainer = Color(0xFFFFCC80)   // Light Orange text on Container

private val DarkBackground = Color(0xFF121212)            // Dark gray background
private val DarkOnBackground = Color(0xFFFFFFFF)          // White text on Background
private val DarkSurface = Color(0xFF1E1E1E)               // Darker surface color
private val DarkOnSurface = Color(0xFFFFFFFF)             // White text on Surface
private val DarkSurfaceVariant = Color(0xFF373737)        // Medium Gray Surface Variant
private val DarkOnSurfaceVariant = Color(0xFFBDBDBD)      // Light Gray text on Surface Variant

private val DarkOutline = Color(0xFF757575)               // Medium Gray for borders

// Light color scheme
private val LightColorScheme = lightColorScheme(
    primary = LightPrimaryColor,
    onPrimary = LightOnPrimary,
    primaryContainer = LightPrimaryContainer,
    onPrimaryContainer = LightOnPrimaryContainer,
    secondary = LightSecondaryColor,
    onSecondary = LightOnSecondary,
    secondaryContainer = LightSecondaryContainer,
    onSecondaryContainer = LightOnSecondaryContainer,
    tertiary = LightTertiaryColor,
    onTertiary = LightOnTertiary,
    tertiaryContainer = LightTertiaryContainer,
    onTertiaryContainer = LightOnTertiaryContainer,
    background = LightBackground,
    onBackground = LightOnBackground,
    surface = LightSurface,
    onSurface = LightOnSurface,
    surfaceVariant = LightSurfaceVariant,
    onSurfaceVariant = LightOnSurfaceVariant,
    outline = LightOutline
)

// Dark color scheme
private val DarkColorScheme = darkColorScheme(
    primary = DarkPrimaryColor,
    onPrimary = DarkOnPrimary,
    primaryContainer = DarkPrimaryContainer,
    onPrimaryContainer = DarkOnPrimaryContainer,
    secondary = DarkSecondaryColor,
    onSecondary = DarkOnSecondary,
    secondaryContainer = DarkSecondaryContainer,
    onSecondaryContainer = DarkOnSecondaryContainer,
    tertiary = DarkTertiaryColor,
    onTertiary = DarkOnTertiary,
    tertiaryContainer = DarkTertiaryContainer,
    onTertiaryContainer = DarkOnTertiaryContainer,
    background = DarkBackground,
    onBackground = DarkOnBackground,
    surface = DarkSurface,
    onSurface = DarkOnSurface,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = DarkOnSurfaceVariant,
    outline = DarkOutline
)
@Immutable
data class ColorFamily(
    val color: Color,
    val onColor: Color,
    val colorContainer: Color,
    val onColorContainer: Color
)

val unspecified_scheme = ColorFamily(
    Color.Unspecified, Color.Unspecified, Color.Unspecified, Color.Unspecified
)

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        content = content
    )
}