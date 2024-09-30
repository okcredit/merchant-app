package app.okcredit.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val okCreditDarkColorScheme =
    darkColorScheme(
        primary = dark_brand_primary,
        onPrimary = grey900,
        primaryContainer = green_300,
        onPrimaryContainer = grey200,
        secondary = dark_brand_accent,
        onSecondary = green_300,
        error = red_300,
        onError = white,
        errorContainer = red_900,
        onErrorContainer = white,
        surface = grey900,
        onSurface = grey200,
        background = grey850,
        onBackground = white,
    )

private val okCreditLightColorScheme =
    lightColorScheme(
        primary = light_brand_primary,
        onPrimary = white,
        primaryContainer = colorAlphasAccentAlpha40,
        onPrimaryContainer = green_800,
        secondary = light_brand_accent,
        error = red_800,
        onError = white,
        errorContainer = red_100,
        onErrorContainer = grey800,
        surface = white,
        onSurface = grey850,
        onSurfaceVariant = grey700,
        background = grey50,
        onBackground = grey900,
        outline = grey400,
        outlineVariant = grey200,
        tertiaryContainer = grey100,
    )

@Composable
fun OkCreditTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val okCreditColorScheme =
        when {
            darkTheme -> okCreditDarkColorScheme
            else -> okCreditLightColorScheme
        }

    MaterialTheme(
        colorScheme = okCreditColorScheme,
        shapes = shapes,
        typography = typography,
        content = content,
    )
}
