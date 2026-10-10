package com.example.taskmanager.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = Color(0xFF18181B),
    onPrimary = Color.White,
    background = Color(0xFFFAFAFA),
    onBackground = Color(0xFF18181B),
    surface = Color(0xFFFAFAFA),
    onSurface = Color(0xFF18181B),
    surfaceVariant = Color(0xFFF1F1F3),
    onSurfaceVariant = Color(0xFF71717A),
    outline = Color(0xFFA1A1AA),
    outlineVariant = Color(0xFFE4E4E7),
    error = Color(0xFFE5484D),
    surfaceContainerLowest = Color.White,
    surfaceContainerLow = Color.White,
    surfaceContainer = Color.White,
    surfaceContainerHigh = Color.White,
    surfaceContainerHighest = Color(0xFFF1F1F3)
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFFFAFAFA),
    onPrimary = Color(0xFF18181B),
    background = Color(0xFF0F0F11),
    onBackground = Color(0xFFF4F4F5),
    surface = Color(0xFF0F0F11),
    onSurface = Color(0xFFF4F4F5),
    surfaceVariant = Color(0xFF1C1C1F),
    onSurfaceVariant = Color(0xFFA1A1AA),
    outline = Color(0xFF71717A),
    outlineVariant = Color(0xFF2A2A2E),
    error = Color(0xFFFF6369),
    surfaceContainerLowest = Color(0xFF17171A),
    surfaceContainerLow = Color(0xFF17171A),
    surfaceContainer = Color(0xFF17171A),
    surfaceContainerHigh = Color(0xFF1F1F23),
    surfaceContainerHighest = Color(0xFF26262B)
)

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        content = content
    )
}