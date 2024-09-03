package app.okcredit.ui.components

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.okcredit.ui.theme.dark_primary_button_background
import app.okcredit.ui.theme.dark_primary_button_content
import app.okcredit.ui.theme.dark_primary_button_disabledBackground
import app.okcredit.ui.theme.dark_primary_button_disabledContent
import app.okcredit.ui.theme.dark_secondary_button_background
import app.okcredit.ui.theme.dark_secondary_button_content
import app.okcredit.ui.theme.dark_secondary_button_disabledBackground
import app.okcredit.ui.theme.dark_secondary_button_disabledContent
import app.okcredit.ui.theme.light_primary_button_background
import app.okcredit.ui.theme.light_primary_button_content
import app.okcredit.ui.theme.light_primary_button_disabledBackground
import app.okcredit.ui.theme.light_primary_button_disabledContent
import app.okcredit.ui.theme.light_secondary_button_background
import app.okcredit.ui.theme.light_secondary_button_content
import app.okcredit.ui.theme.light_secondary_button_disabledBackground
import app.okcredit.ui.theme.light_secondary_button_disabledContent
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun PrimaryButton(
    text: String,
    icon: DrawableResource? = null,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: () -> Unit,
) {
    val darkMode = isSystemInDarkTheme()

    Button(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(50),
        enabled = enabled,
        colors = if (darkMode) {
            ButtonDefaults.buttonColors(
                containerColor = dark_primary_button_background,
                contentColor = dark_primary_button_content,
                disabledContainerColor = dark_primary_button_disabledBackground,
                disabledContentColor = dark_primary_button_disabledContent,
            )
        } else {
            ButtonDefaults.buttonColors(
                containerColor = light_primary_button_background,
                contentColor = light_primary_button_content,
                disabledContainerColor = light_primary_button_disabledBackground,
                disabledContentColor = light_primary_button_disabledContent,
            )
        },
    ) {
        if (icon != null) {
            Icon(
                painter = painterResource(icon),
                contentDescription = null,
            )
            Spacer(modifier = Modifier.width(8.dp))
        }
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

@Composable
fun SecondaryButton(
    text: String,
    icon: DrawableResource? = null,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: () -> Unit,
) {
    val darkMode = isSystemInDarkTheme()

    Button(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(50),
        enabled = enabled,
        colors = if (darkMode) {
            ButtonDefaults.buttonColors(
                containerColor = dark_secondary_button_background,
                contentColor = dark_secondary_button_content,
                disabledContainerColor = dark_secondary_button_disabledBackground,
                disabledContentColor = dark_secondary_button_disabledContent,
            )
        } else {
            ButtonDefaults.buttonColors(
                containerColor = light_secondary_button_background,
                contentColor = light_secondary_button_content,
                disabledContainerColor = light_secondary_button_disabledBackground,
                disabledContentColor = light_secondary_button_disabledContent,
            )
        },
    ) {
        if (icon != null) {
            Icon(
                painter = painterResource(icon),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimaryContainer,
            )
            Spacer(modifier = Modifier.width(8.dp))
        }
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
        )
    }
}

@Composable
fun TwinBottomButtons(
    primaryText: String,
    secondaryText: String,
    primaryIcon: DrawableResource? = null,
    secondaryIcon: DrawableResource? = null,
    modifier: Modifier = Modifier,
    primaryEnabled: Boolean = true,
    secondaryEnabled: Boolean = true,
    onPrimaryClick: () -> Unit,
    onSecondaryClick: () -> Unit,
) {
    Row(modifier = modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
        SecondaryButton(
            text = secondaryText,
            icon = secondaryIcon,
            modifier = Modifier.weight(1.0f),
            enabled = secondaryEnabled,
            onClick = onSecondaryClick,
        )
        Spacer(modifier = Modifier.width(16.dp))
        PrimaryButton(
            text = primaryText,
            icon = primaryIcon,
            modifier = Modifier.weight(1.0f),
            enabled = primaryEnabled,
            onClick = onPrimaryClick,
        )
    }
}

@Preview
@Composable
fun PrimaryButtonPreview() {
    PrimaryButton(text = "Primary Button", onClick = {})
}

@Preview
@Composable
fun SecondaryButtonPreview() {
    SecondaryButton(text = "Secondary Button", onClick = {})
}

@Preview
@Composable
fun TwinBottomButtonsPreview() {
    TwinBottomButtons(
        primaryText = "Primary Button",
        secondaryText = "Secondary Button",
        onPrimaryClick = {},
        onSecondaryClick = {},
    )
}
