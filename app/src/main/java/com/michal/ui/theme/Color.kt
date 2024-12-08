package com.michal.ui.theme
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

val LightPrimaryColor = Color(0xFF8E24AA)         // Light Purple
val LightOnPrimary = Color(0xFFFFFFFF)            // White text on Primary
val LightPrimaryContainer = Color(0xFFF3E5F5)     // Lighter Purple Container
val LightOnPrimaryContainer = Color(0xFF4A0072)   // Dark Purple text on Container

val LightSecondaryColor = Color(0xFFFFFFFF)       // White for light theme
val LightOnSecondary = Color(0xFF000000)          // Black text on Secondary
val LightSecondaryContainer = Color(0xFFE0E0E0)   // Light Gray Container
val LightOnSecondaryContainer = Color(0xFF303030) // Darker Gray text on Container

val LightTertiaryColor = Color(0xFFF39C12)        // Light Orange
val LightOnTertiary = Color(0xFFFFFFFF)           // Black text on Tertiary
val LightTertiaryContainer = Color(0xFFFFE0B2)    // Light Orange Container
val LightOnTertiaryContainer = Color(0xFF5D4037)  // Dark Brown text on Container

val LightBackground = Color(0xFFF5F5F5)           // Light gray for background
val LightOnBackground = Color(0xFF000000)         // Black text on Background
val LightSurface = Color(0xFFFFFFFF)              // White for surface elements
val LightOnSurface = Color(0xFF000000)            // Black text on Surface
val LightSurfaceVariant = Color(0xFFE0E0E0)       // Light Gray Surface Variant
val LightOnSurfaceVariant = Color(0xFF616161)     // Medium Gray text on Surface Variant

val LightOutline = Color(0xFFBDBDBD)              // Light Gray for borders

val DarkPrimaryColor = Color(0xFF7B1FA2)          // Darker Purple
val DarkOnPrimary = Color(0xFFFFFFFF)             // White text on Primary
val DarkPrimaryContainer = Color(0xFF4A0072)      // Dark Purple Container
val DarkOnPrimaryContainer = Color(0xFFE1BEE7)    // Lighter Purple text on Container

val DarkSecondaryColor = Color(0xFF000000)        // Black for dark theme
val DarkOnSecondary = Color(0xFFFFFFFF)           // White text on Secondary
val DarkSecondaryContainer = Color(0xFF424242)    // Dark Gray Container
val DarkOnSecondaryContainer = Color(0xFFE0E0E0)  // Light Gray text on Container

val DarkTertiaryColor = Color(0xFFD35400)         // Soft, muted Orange
val DarkOnTertiary = Color(0xFF000000)            // Black text on Tertiary
val DarkTertiaryContainer = Color(0xFF5D4037)     // Dark Brown Container
val DarkOnTertiaryContainer = Color(0xFFFFCC80)   // Light Orange text on Container

val DarkBackground = Color(0xFF121212)            // Dark gray background
val DarkOnBackground = Color(0xFFFFFFFF)          // White text on Background
val DarkSurface = Color(0xFF1E1E1E)               // Darker surface color
val DarkOnSurface = Color(0xFFFFFFFF)             // White text on Surface
val DarkSurfaceVariant = Color(0xFF373737)        // Medium Gray Surface Variant
val DarkOnSurfaceVariant = Color(0xFFBDBDBD)      // Light Gray text on Surface Variant

val DarkOutline = Color(0xFF757575)

val LoginColor = Color(0xFF4cd137)

val RegisterColor = Color(0xFF386CEC)

val LightGradientBrush = Brush.radialGradient(
    colors = listOf(
        LightOnPrimaryContainer,
        LightPrimaryColor
    )
)

val DarkGradientBrush = Brush.radialGradient(
    colors = listOf(
        DarkOnPrimaryContainer,
        DarkPrimaryColor
    )
)