package app.okcredit.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val okCreditDarkColorScheme =
    darkColorScheme(
        primary = dark_indigo_primary,
        onPrimary = dark_onindigo_primary,
        primaryContainer = dark_indigo_primaryContainer,
        onPrimaryContainer = dark_onindigo_primaryContainer,
        secondary = dark_indigo_primary,
        onSecondary = dark_onindigo_primary,
        secondaryContainer = dark_indigo_primaryContainer,
        onSecondaryContainer = dark_onindigo_primaryContainer,
        tertiary = dark_indigo_primary,
        onTertiary = dark_onindigo_primary,
        tertiaryContainer = dark_indigo_primaryContainer,
        onTertiaryContainer = dark_onindigo_primaryContainer,
        error = dark_red_primary,
        onError = dark_onred_primary,
        errorContainer = dark_red_primaryContainer,
        onErrorContainer = dark_onred_primaryContainer,
    )

private val okCreditLightColorScheme =
    lightColorScheme(
        primary = green_primary,
        onPrimary = white,
        primaryContainer = green_lite_1,
        onPrimaryContainer = green_primary,
        secondary = green_lite,
        onSecondary = green_primary,
        secondaryContainer = green_lite_1,
        onSecondaryContainer = green_primary,
        tertiary = indigo_primary,
        onTertiary = white,
        tertiaryContainer = indigo_lite_1,
        onTertiaryContainer = indigo_primary,
        error = red_primary,
        onError = white,
        errorContainer = red_lite_1,
        onErrorContainer = red_primary,
        surface = white,
        onSurface = grey900,
        onSurfaceVariant = grey700,
        background = grey50,
        onBackground = grey900,
        outline = grey400,
        outlineVariant = grey200,
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
