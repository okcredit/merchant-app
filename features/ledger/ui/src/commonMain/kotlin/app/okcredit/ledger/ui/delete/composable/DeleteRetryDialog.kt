package app.okcredit.ledger.ui.delete.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import app.okcredit.ledger.ui.cancel
import app.okcredit.ledger.ui.retry
import app.okcredit.ledger.ui.unable_to_delete_customer_balance_not_zero
import app.okcredit.ui.Res
import app.okcredit.ui.icon_warning
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun DeleteRetryDialog(
    onCancel: () -> Unit,
    onRetry: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Icon(
            painter = painterResource(Res.drawable.icon_warning),
            contentDescription = null,
            tint = Color.Red,
            modifier = Modifier
                .size(24.dp)
                .align(Alignment.CenterHorizontally),
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = stringResource(app.okcredit.ledger.ui.Res.string.unable_to_delete_customer_balance_not_zero),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(horizontal = 16.dp),
        )

        Spacer(modifier = Modifier.height(24.dp))

        HorizontalDivider(
            color = Color.Gray,
            thickness = 1.dp,
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Row(
                modifier = Modifier
                    .weight(1f)
                    .clickable { onCancel() }
                    .border(width = 1.dp, shape = RoundedCornerShape(20.dp), color = MaterialTheme.colorScheme.primary)
                    .padding(end = 8.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = stringResource(app.okcredit.ledger.ui.Res.string.cancel),
                    modifier = Modifier.padding(vertical = 10.dp),
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.titleMedium,
                )
            }
            Spacer(modifier = Modifier.width(16.dp))

            Row(
                modifier = Modifier
                    .weight(1f)
                    .clickable { onRetry() }
                    .background(MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(20.dp))
                    .border(width = 1.dp, shape = RoundedCornerShape(20.dp), color = MaterialTheme.colorScheme.primary)
                    .padding(end = 8.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = stringResource(app.okcredit.ledger.ui.Res.string.retry),
                    modifier = Modifier.padding(vertical = 10.dp),
                    color = MaterialTheme.colorScheme.surface,
                    style = MaterialTheme.typography.titleMedium,
                )
            }
        }
    }
}

@Preview
@Composable
fun DeleteRetryDialogPreview() {
    DeleteRetryDialog({}, {})
}
