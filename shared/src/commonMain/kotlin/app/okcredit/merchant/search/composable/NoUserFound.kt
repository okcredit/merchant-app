package app.okcredit.merchant.search.composable

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.okcredit.ui.components.PrimaryButton
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun NoUserFound(
    searchQuery: String,
    addCustomerInProgress: Boolean,
    onAddToAccountClicked: (String) -> Unit,
) {
    val focusManager = LocalFocusManager.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = buildAnnotatedString {
                append("No contact found with ")
                withStyle(
                    style = SpanStyle(
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                    ),
                ) {
                    append(searchQuery)
                }
            },
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(1.0f),
        )
        Spacer(modifier = Modifier.width(8.dp))
        PrimaryButton(
            onClick = {
                focusManager.clearFocus()
                onAddToAccountClicked(searchQuery)
            },
            text = "Add",
            enabled = !addCustomerInProgress,
        )
    }
}

@Composable
@Preview
fun NoUserFoundPreview() {
    NoUserFound(
        searchQuery = "Ramesh",
        addCustomerInProgress = false,
        onAddToAccountClicked = {},
    )
}
