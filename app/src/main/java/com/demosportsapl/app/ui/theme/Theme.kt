package com.demosportsapl.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColors = darkColorScheme(
    primary = Color(0xFF1565C0),
    secondary = Color(0xFF0288D1),
    tertiary = Color(0xFFFFD600)
)

private val LightColors = lightColorScheme(
    primary = Color(0xFF1565C0),
    secondary = Color(0xFF0288D1),
    tertiary = Color(0xFFFFD600),
    background = Color(0xFFF5F5F5),
    surface = Color.White
)

@Composable
fun DemosportsAPLTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColors,
        content = content
    )
}
