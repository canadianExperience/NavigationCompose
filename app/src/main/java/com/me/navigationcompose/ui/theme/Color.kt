package com.me.navigationcompose.ui.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.material.darkColors

val Green500 = Color(0xFF1EB980)
val DarkBlue900 = Color(0xFF26282F)

// Rally is always dark themed.
val ColorPalette = darkColors(
    primary = Green500,
    surface = DarkBlue900,
    onSurface = Color.White,
    background = DarkBlue900,
    onBackground = Color.White
)