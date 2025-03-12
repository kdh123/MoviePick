package com.dhkim.core.designsystem

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

private val DarkColorScheme = darkColorScheme(
    background = Black,
    onBackground = White,
    primary = Primary,
    onPrimary = White,
    secondary = PurpleGrey80,
    tertiary = Pink80,
    surface = Black,
    onSurface = White,
    surfaceContainer = Black,
    secondaryContainer = DarkGray
)

private val LightColorScheme = lightColorScheme(
    primary = Primary,
    onPrimary = White,
    secondary = PurpleGrey40,
    tertiary = Pink40,
    surfaceContainer = White,
    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

object MoviePickTheme {
    val typography: MoviePickTypography
        @Composable
        get() = LocalTypography.current
}


@Composable
fun MoviePickTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> {
            DarkColorScheme
        }
        else -> {
            LightColorScheme
        }
    }

    CompositionLocalProvider(
        LocalTypography provides Typography
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            content = content
        )
    }
}