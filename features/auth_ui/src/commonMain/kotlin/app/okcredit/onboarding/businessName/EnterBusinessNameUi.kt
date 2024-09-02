@file:OptIn(ExperimentalMaterial3Api::class)

package app.okcredit.onboarding.businessName

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import app.okcredit.ui.components.BoxInputField
import app.okcredit.ui.ic_okcredit_logo
import app.okcredit.ui.icon_phone
import merchant_app.features.auth_ui.generated.resources.Res
import merchant_app.features.auth_ui.generated.resources.ic_business_name
import org.jetbrains.compose.resources.painterResource

@Composable
fun EnterBusinessNameUi(
    loading: Boolean,
    onSkipClicked: () -> Unit,
    onSubmitClick: (String) -> Unit,
) {
    Column(modifier = Modifier.background(color = MaterialTheme.colorScheme.surface)) {
        Row(modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)) {
            Image(
                painter = painterResource(app.okcredit.ui.Res.drawable.ic_okcredit_logo),
                contentDescription = null,
                modifier = Modifier.align(Alignment.CenterVertically),
            )
            Spacer(Modifier.weight(1.0f))
            OutlinedButton(
                onClick = { onSkipClicked.invoke() },
                modifier = Modifier.wrapContentHeight(),
                shape = RoundedCornerShape(50),
                border = BorderStroke(
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                    width = 1.dp,
                ),
            ) {
                Text(
                    text = "Skip",
                    style = MaterialTheme.typography.bodyLarge,
                )
            }
        }
        Spacer(Modifier.weight(1.0f))
        Image(
            painter = painterResource(Res.drawable.ic_business_name),
            contentDescription = null,
            modifier = Modifier.padding(horizontal = 16.dp),
        )
        Spacer(Modifier.height(24.dp))
        val inputValue = remember { mutableStateOf(TextFieldValue("")) }
        Text(
            text = "Add your business/shop name",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(horizontal = 16.dp),
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = "This will help your customers identify your business/ shop.",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(horizontal = 16.dp),
        )
        Spacer(Modifier.height(16.dp))
        EnterBusinessNameTextField(inputValue, loading) {
            onSubmitClick(inputValue.value.text)
        }
        Spacer(Modifier.height(16.dp))
    }
}

@Composable
private fun EnterBusinessNameTextField(
    inputValue: MutableState<TextFieldValue>,
    loading: Boolean,
    onSubmitClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(start = 16.dp, end = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        BoxInputField(
            value = inputValue.value,
            onValueChange = {
                inputValue.value = it
            },
            modifier = Modifier
                .padding(end = 16.dp)
                .weight(1.0f)
                .fillMaxWidth(0.9f),
            label = "Business/ Shop Name",
            leadingIcon = painterResource(app.okcredit.ui.Res.drawable.icon_phone),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Phone),
        )

        FloatingActionButton(
            onClick = {
                if (!loading) {
                    onSubmitClick()
                }
            },
        ) {
            Icon(imageVector = Icons.Default.Check, contentDescription = "Submit!")
        }
    }
}
