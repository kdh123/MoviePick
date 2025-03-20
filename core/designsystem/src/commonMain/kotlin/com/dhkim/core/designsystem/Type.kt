package com.dhkim.core.designsystem

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

private val SansSerifStyle = TextStyle(
    fontFamily = FontFamily.SansSerif,
    fontWeight = FontWeight.Normal,
)

internal val Typography = MoviePickTypography(
    displayLarge = SansSerifStyle.copy(
        fontSize = 57.sp,
        lineHeight = 64.sp,
        letterSpacing = (-0.25).sp,
    ),
    displayMedium = SansSerifStyle.copy(
        fontSize = 48.sp,
        lineHeight = 52.sp,
        letterSpacing = 0.sp,
    ),
    displayMediumPrimary = SansSerifStyle.copy(
        fontSize = 48.sp,
        lineHeight = 52.sp,
        letterSpacing = 0.sp,
        color = Primary
    ),
    displayMediumGray = SansSerifStyle.copy(
        fontSize = 48.sp,
        lineHeight = 52.sp,
        letterSpacing = 0.sp,
        color = Gray
    ),
    displayMediumWhite = SansSerifStyle.copy(
        fontSize = 48.sp,
        lineHeight = 52.sp,
        letterSpacing = 0.sp,
        color = Color.White
    ),
    displaySmall = SansSerifStyle.copy(
        fontSize = 36.sp,
        lineHeight = 44.sp,
        letterSpacing = 0.sp,
    ),
    headlineLarge = SansSerifStyle.copy(
        fontSize = 32.sp,
        lineHeight = 40.sp,
        letterSpacing = 0.sp,
    ),
    headlineLargeBold = SansSerifStyle.copy(
        fontSize = 32.sp,
        lineHeight = 40.sp,
        letterSpacing = 0.sp,
        fontWeight = FontWeight.Bold
    ),
    headlineMedium = SansSerifStyle.copy(
        fontSize = 28.sp,
        lineHeight = 36.sp,
        letterSpacing = 0.sp,
    ),
    headlineMediumBold = SansSerifStyle.copy(
        fontSize = 28.sp,
        lineHeight = 36.sp,
        letterSpacing = 0.sp,
        fontWeight = FontWeight.Bold
    ),
    headlineSmall = SansSerifStyle.copy(
        fontSize = 24.sp,
        lineHeight = 32.sp,
        letterSpacing = 0.sp,
    ),
    headlineSmallBold = SansSerifStyle.copy(
        fontSize = 24.sp,
        lineHeight = 32.sp,
        letterSpacing = 0.sp,
        fontWeight = FontWeight.Bold
    ),
    titleLarge = SansSerifStyle.copy(
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp,
    ),
    titleLargeBold = SansSerifStyle.copy(
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp,
        fontWeight = FontWeight.Bold
    ),
    titleMedium = SansSerifStyle.copy(
        fontSize = 18.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.15.sp,
    ),
    titleMediumBold = SansSerifStyle.copy(
        fontSize = 18.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.15.sp,
        fontWeight = FontWeight.Bold
    ),
    titleSmall = SansSerifStyle.copy(
        fontSize = 16.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp,
    ),
    titleSmallBold = SansSerifStyle.copy(
        fontSize = 16.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp,
        fontWeight = FontWeight.Bold
    ),
    labelLarge = SansSerifStyle.copy(
        fontSize = 16.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp,
    ),
    labelLargeBold = SansSerifStyle.copy(
        fontSize = 16.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp,
        fontWeight = FontWeight.Bold
    ),
    labelMedium = SansSerifStyle.copy(
        fontSize = 14.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp,
    ),
    labelSmall = SansSerifStyle.copy(
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp,
    ),
    bodyLarge = SansSerifStyle.copy(
        fontSize = 18.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp,
    ),
    bodyLargeBold = SansSerifStyle.copy(
        fontSize = 18.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp,
        fontWeight = FontWeight.Bold
    ),
    bodyLargeGray = SansSerifStyle.copy(
        fontSize = 18.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp,
        color = Gray
    ),
    bodyLargeBlack = SansSerifStyle.copy(
        fontSize = 18.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp,
        color = Color.Black
    ),
    bodyLargeWhite = SansSerifStyle.copy(
        fontSize = 18.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp,
        color = Color.White
    ),
    bodyLargeGrayBold = SansSerifStyle.copy(
        fontSize = 18.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp,
        color = Color.Gray,
        fontWeight = FontWeight.Bold
    ),
    bodyLargeWhiteBold = SansSerifStyle.copy(
        fontSize = 18.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp,
        color = Color.White,
        fontWeight = FontWeight.Bold
    ),
    bodyMedium = SansSerifStyle.copy(
        fontSize = 16.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.25.sp,
    ),
    bodyMediumBlack = SansSerifStyle.copy(
        fontSize = 16.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.25.sp,
        color = Color.Black
    ),
    bodyMediumGray = SansSerifStyle.copy(
        fontSize = 16.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.25.sp,
        color = Gray
    ),
    bodyMediumBold = SansSerifStyle.copy(
        fontSize = 16.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.25.sp,
        fontWeight = FontWeight.Bold
    ),
    bodyMediumWhiteBold = SansSerifStyle.copy(
        fontSize = 16.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.25.sp,
        fontWeight = FontWeight.Bold,
        color = Color.White
    ),
    bodySmall = SansSerifStyle.copy(
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.4.sp,
    ),
    bodySmallGray = SansSerifStyle.copy(
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.4.sp,
        color = Gray
    ),
    bodySmallPrimary = SansSerifStyle.copy(
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.4.sp,
        color = Primary
    ),
    bodySmallBold = SansSerifStyle.copy(
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.4.sp,
        fontWeight = FontWeight.Bold
    ),
)

val LocalTypography = staticCompositionLocalOf {
    MoviePickTypography(
        displayLarge = SansSerifStyle,
        displayMedium = SansSerifStyle,
        displayMediumPrimary = SansSerifStyle,
        displayMediumGray = SansSerifStyle,
        displayMediumWhite = SansSerifStyle,
        displaySmall = SansSerifStyle,
        headlineLarge = SansSerifStyle,
        headlineLargeBold = SansSerifStyle,
        headlineMedium = SansSerifStyle,
        headlineMediumBold = SansSerifStyle,
        headlineSmall = SansSerifStyle,
        headlineSmallBold = SansSerifStyle,
        titleLarge = SansSerifStyle,
        titleLargeBold = SansSerifStyle,
        titleMedium = SansSerifStyle,
        titleMediumBold = SansSerifStyle,
        titleSmall = SansSerifStyle,
        titleSmallBold = SansSerifStyle,
        bodyLarge = SansSerifStyle,
        bodyLargeBlack = SansSerifStyle,
        bodyLargeWhite = SansSerifStyle,
        bodyLargeGray = SansSerifStyle,
        bodyLargeBold = SansSerifStyle,
        bodyLargeGrayBold = SansSerifStyle,
        bodyLargeWhiteBold = SansSerifStyle,
        bodyMedium = SansSerifStyle,
        bodyMediumBlack = SansSerifStyle,
        bodyMediumGray = SansSerifStyle,
        bodyMediumBold = SansSerifStyle,
        bodyMediumWhiteBold = SansSerifStyle,
        bodySmall = SansSerifStyle,
        bodySmallGray = SansSerifStyle,
        bodySmallPrimary = SansSerifStyle,
        bodySmallBold = SansSerifStyle,
        labelLarge = SansSerifStyle,
        labelLargeBold = SansSerifStyle,
        labelMedium = SansSerifStyle,
        labelSmall = SansSerifStyle,
    )
}

@Immutable
data class MoviePickTypography(
    val displayLarge: TextStyle,
    val displayMedium: TextStyle,
    val displayMediumPrimary: TextStyle,
    val displayMediumGray: TextStyle,
    val displayMediumWhite: TextStyle,
    val displaySmall: TextStyle,
    val headlineLarge: TextStyle,
    val headlineLargeBold: TextStyle,
    val headlineMedium: TextStyle,
    val headlineMediumBold: TextStyle,
    val headlineSmall: TextStyle,
    val headlineSmallBold: TextStyle,
    val titleLarge: TextStyle,
    val titleLargeBold: TextStyle,
    val titleMedium: TextStyle,
    val titleMediumBold: TextStyle,
    val titleSmall: TextStyle,
    val titleSmallBold: TextStyle,
    val bodyLarge: TextStyle,
    val bodyLargeBlack: TextStyle,
    val bodyLargeWhite: TextStyle,
    val bodyLargeGray: TextStyle,
    val bodyLargeBold: TextStyle,
    val bodyLargeGrayBold: TextStyle,
    val bodyLargeWhiteBold: TextStyle,
    val bodyMedium: TextStyle,
    val bodyMediumBlack: TextStyle,
    val bodyMediumGray: TextStyle,
    val bodyMediumBold: TextStyle,
    val bodyMediumWhiteBold: TextStyle,
    val bodySmall: TextStyle,
    val bodySmallGray: TextStyle,
    val bodySmallPrimary: TextStyle,
    val bodySmallBold: TextStyle,
    val labelLarge: TextStyle,
    val labelLargeBold: TextStyle,
    val labelMedium: TextStyle,
    val labelSmall: TextStyle
)