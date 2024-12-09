package com.michal.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.dp

@Immutable
data class AppShapes (
    val materialShapes:Shapes = Shapes(
        small = RoundedCornerShape(4.dp),
        medium = RoundedCornerShape(8.dp),
        large = RoundedCornerShape(16.dp)
    ),
    val customShape:RoundedCornerShape = RoundedCornerShape(topStart = 64.dp, topEnd = 16.dp, bottomStart = 16.dp, bottomEnd = 64.dp)
)

val LocalAppShapes = compositionLocalOf { AppShapes() }