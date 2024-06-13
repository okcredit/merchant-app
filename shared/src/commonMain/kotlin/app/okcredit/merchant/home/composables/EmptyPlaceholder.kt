package app.okcredit.merchant.home.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import merchant_app.shared.generated.resources.Res
import merchant_app.shared.generated.resources.ic_add_first_supplier
import merchant_app.shared.generated.resources.supplier_learn_more_title
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun EmptyPlaceholder(emptyImage: @Composable () -> Unit, message: String, emptyButton: @Composable () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
        Spacer(modifier = Modifier.height(45.dp))
        emptyImage()
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 20.dp)
        )
        Spacer(modifier = Modifier.height(20.dp))
        emptyButton()
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Preview
@Composable
fun EmptyPlaceholderPreview() {
    EmptyPlaceholder(
        emptyImage = {
            Image(
                painter = painterResource(resource = Res.drawable.ic_add_first_supplier),
                contentDescription = stringResource(resource = Res.string.supplier_learn_more_title),
                modifier = Modifier.size(160.dp)
            )
        },
        message = stringResource(resource = Res.string.supplier_learn_more_title),
        emptyButton = {
        }
    )
}
