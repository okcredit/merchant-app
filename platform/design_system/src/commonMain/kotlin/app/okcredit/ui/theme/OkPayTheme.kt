package app.okcredit.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val okPayDarkColorScheme =
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
        outline = grey100,
        outlineVariant = grey100,
    )

private val okPayLightColorScheme =
    lightColorScheme(
        primary = indigo_primary,
        onPrimary = white,
        primaryContainer = indigo_lite_1,
        onPrimaryContainer = indigo_primary,
        secondary = indigo_lite,
        onSecondary = indigo_primary,
        secondaryContainer = indigo_lite_1,
        onSecondaryContainer = indigo_primary,
        tertiary = indigo_lite,
        onTertiary = indigo_primary,
        tertiaryContainer = indigo_lite_1,
        onTertiaryContainer = indigo_primary,
        error = red_primary,
        onError = white,
        errorContainer = red_lite_1,
        onErrorContainer = red_primary,
        surface = white,
        surfaceVariant = white,
        outline = grey100,
        outlineVariant = grey100,
    )

@Composable
fun OkPayTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val okPayColorScheme =
        when {
            darkTheme -> okPayDarkColorScheme
            else -> okPayLightColorScheme
        }

    MaterialTheme(
        colorScheme = okPayColorScheme,
        shapes = shapes,
        typography = typography,
        content = content,
    )
}
