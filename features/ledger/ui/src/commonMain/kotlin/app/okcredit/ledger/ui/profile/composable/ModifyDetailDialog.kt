package app.okcredit.ledger.ui.profile.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import app.okcredit.ledger.ui.Res
import app.okcredit.ledger.ui.name
import app.okcredit.ledger.ui.submit_text
import app.okcredit.ui.icon_close
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ModifyDetailDialog(
    title: String,
    prefillText: String,
    onSubmitClicked: (String) -> Unit,
    onCloseClicked: () -> Unit,
) {
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalFocusManager.current

    val text = remember { mutableStateOf(TextFieldValue(prefillText, selection = TextRange(prefillText.length))) }
    LaunchedEffect(true) {
        focusRequester.requestFocus()
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
                .align(Alignment.TopStart),
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = title,
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp),
                style = MaterialTheme.typography.titleSmall,
            )
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester)
                    .padding(horizontal = 16.dp),
                value = text.value,
                onValueChange = {
                    text.value = it
                },
                label = {
                    Text(
                        text = stringResource(Res.string.name),
                        style = MaterialTheme.typography.titleSmall,
                    )
                },
                keyboardActions = KeyboardActions(onDone = { keyboardController.clearFocus() }),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done,
                ),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.primary,
                    errorBorderColor = MaterialTheme.colorScheme.error,
                    errorLabelColor = MaterialTheme.colorScheme.error,
                    errorLeadingIconColor = MaterialTheme.colorScheme.error,
                ),
            )
            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant,
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
                    .clickable {
                        onSubmitClicked(text.value.text)
                    }
                    .background(MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(20.dp)),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = stringResource(Res.string.submit_text),
                    modifier = Modifier.padding(vertical = 10.dp),
                    color = MaterialTheme.colorScheme.surface,
                    style = MaterialTheme.typography.titleSmall,
                )
            }
        }
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .clickable { onCloseClicked() },
        ) {
            androidx.compose.material3.Icon(
                painterResource(app.okcredit.ui.Res.drawable.icon_close),
                contentDescription = "close",
                modifier = Modifier.padding(8.dp),
            )
        }
    }
}

@Preview
@Composable
fun ModifyNameDialogPreview() {
    ModifyDetailDialog(prefillText = "Karan", onSubmitClicked = {}, onCloseClicked = {}, title = "Enter Name")
}
