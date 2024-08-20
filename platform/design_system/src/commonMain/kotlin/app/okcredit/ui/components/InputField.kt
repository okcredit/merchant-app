package app.okcredit.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp

@Composable
fun BoxInputField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    supportingText: String? = null,
    enabled: Boolean = true,
    leadingIcon: Painter? = null,
    trailingIcon: Painter? = null,
) {
    Column(modifier = modifier) {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            value = value,
            onValueChange = onValueChange,
            enabled = enabled,
            label = {
                Text(
                    label,
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            leadingIcon = {
                if (leadingIcon != null) {
                    Icon(
                        painter = leadingIcon,
                        contentDescription = "",
                        modifier = Modifier.size(20.dp)
                    )
                }
            },
            trailingIcon = {
                if (trailingIcon != null) {
                    Icon(
                        painter = trailingIcon,
                        contentDescription = "",
                        modifier = Modifier.size(20.dp)
                    )
                }
            },
            shape = MaterialTheme.shapes.small,
            keyboardOptions = keyboardOptions,
        )
        Spacer(modifier = Modifier.size(4.dp))
        AnimatedVisibility(visible = supportingText != null) {
            Text(
                text = supportingText ?: "",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }
    }
}

@Composable
fun BoxInputField(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    supportingText: String? = null,
    enabled: Boolean = true,
    leadingIcon: Painter? = null,
    trailingIcon: Painter? = null,
) {
    Column(modifier = modifier) {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            value = value,
            onValueChange = onValueChange,
            enabled = enabled,
            label = {
                Text(
                    label,
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            leadingIcon = {
                if (leadingIcon != null) {
                    Icon(
                        painter = leadingIcon,
                        contentDescription = "",
                        modifier = Modifier.size(20.dp)
                    )
                }
            },
            trailingIcon = {
                if (trailingIcon != null) {
                    Icon(
                        painter = trailingIcon,
                        contentDescription = "",
                        modifier = Modifier.size(20.dp)
                    )
                }
            },
            shape = MaterialTheme.shapes.small,
            keyboardOptions = keyboardOptions,
        )
        Spacer(modifier = Modifier.size(4.dp))
        AnimatedVisibility(visible = supportingText != null) {
            Text(
                text = supportingText ?: "",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }
    }
}