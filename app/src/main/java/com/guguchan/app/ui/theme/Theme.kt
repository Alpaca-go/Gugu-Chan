package com.guguchan.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = Color(0xFFC45E3B),
    secondary = Color(0xFF7F4F3A),
    tertiary = Color(0xFFD58A5D),
    background = Color(0xFFFFF7EF),
    surface = Color(0xFFFFFBF8)
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFFFFB38F),
    secondary = Color(0xFFE1B39D),
    tertiary = Color(0xFFF6C6A6)
)

@Composable
fun GuguChanTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColors,
        content = content
    )
}
