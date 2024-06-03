@file:OptIn(ExperimentalMaterial3Api::class)

package app.okcredit.onboarding.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import app.okcredit.ui.icon_phone
import merchant_app.features.auth_ui.generated.resources.Res
import merchant_app.features.auth_ui.generated.resources.ic_applogo_name
import org.jetbrains.compose.resources.painterResource

@Composable
fun EnterMobile(onSubmitClick: (String) -> Unit) {
    Column(modifier = Modifier.background(color = MaterialTheme.colorScheme.background)) {
        Image(
            painter = painterResource(Res.drawable.ic_applogo_name),
            contentDescription = null,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp),
        )
        Spacer(Modifier.weight(1.0f))
        Image(
            painter = painterResource(app.okcredit.ui.Res.drawable.icon_phone),
            contentDescription = null,
            modifier = Modifier.padding(horizontal = 16.dp),
        )
        Spacer(Modifier.height(24.dp))
        val inputValue = remember { mutableStateOf(TextFieldValue("")) }
        Text(
            text = "Enter Your Mobile Number",
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(horizontal = 16.dp),
        )
        Spacer(Modifier.height(16.dp))
        EnterMobileTextField(inputValue) {
            onSubmitClick(inputValue.value.text)
        }
        TermsAndConditions({}, columnScope = this)
    }
}

@Composable
fun TermsAndConditions(onTncClicked: () -> Unit, columnScope: ColumnScope) {
    val tncText = buildAnnotatedString {
        append("By continuing, you agree to our ")
        withStyle(
            style = SpanStyle(
                color = MaterialTheme.colorScheme.primary,
                textDecoration = TextDecoration.Underline,
            ),
        ) {
            append("Terms of Service")
        }
        append(" and ")
        withStyle(
            style = SpanStyle(
                color = MaterialTheme.colorScheme.primary,
                textDecoration = TextDecoration.Underline,
            ),
        ) {
            append("Privacy Policy")
        }
    }

    columnScope.apply {
        ClickableText(
            text = tncText,
            onClick = { onTncClicked.invoke() },
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp)
                .clickable {
                    onTncClicked.invoke()
                },
        )
    }
}

@Composable
fun EnterMobileTextField(inputValue: MutableState<TextFieldValue>, onSubmitClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        OutlinedTextField(
            value = inputValue.value,
            onValueChange = { inputValue.value = it },
            modifier = Modifier
                .padding(end = 16.dp)
                .weight(1.0f),
            label = {
                Text(
                    text = "Mobile Number",
                    style = MaterialTheme.typography.bodyMedium,
                )
            },
            leadingIcon = {
                Icon(
                    painter = painterResource(app.okcredit.ui.Res.drawable.icon_phone),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    tint = MaterialTheme.colorScheme.primary,
                )
            },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Phone,
            ),
            textStyle = MaterialTheme.typography.bodyLarge,
        )

        FloatingActionButton(onClick = onSubmitClick, modifier = Modifier.size(48.dp)) {
            Icon(imageVector = Icons.Default.Check, contentDescription = "Submit!")
        }
    }
}
