package app.okcredit.merchant.ledger.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import app.okcredit.merchant.ledger.HomeContract
import app.okcredit.ui.icon_cloud_off
import app.okcredit.ui.theme.OkCreditTheme
import app.okcredit.ui.theme.orange_lite_1
import app.okcredit.ui.theme.orange_primary
import merchant_app.shared.generated.resources.Res
import merchant_app.shared.generated.resources.alert_transactions_not_synced
import merchant_app.shared.generated.resources.retry
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun UserAlertBanner(userAlert: HomeContract.UserAlert, onUserAlertClicked: (HomeContract.UserAlert) -> Unit) {
    Card(
        colors = CardDefaults.cardColors().copy(containerColor = orange_lite_1),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.padding(horizontal = 16.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 8.dp)
        ) {
            Spacer(modifier = Modifier.width(8.dp))
            Image(
                painter = painterResource(resource = userAlert.getIcon()),
                contentDescription = userAlert.getMessage(),
                colorFilter = ColorFilter.tint(orange_primary),
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = userAlert.getMessage(),
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.weight(1.0f)
            )
            if (userAlert.getCta().isNotEmpty()) {
                TextButton(onClick = {
                    onUserAlertClicked.invoke(userAlert)
                }) {
                    Text(
                        text = userAlert.getCta(),
                        style = MaterialTheme.typography.labelSmall,
                        color = orange_primary
                    )
                }
            }
        }
    }
}

fun HomeContract.UserAlert.getIcon(): DrawableResource {
    return when (this) {
        is HomeContract.UserAlert.UnSyncedTransactions -> app.okcredit.ui.Res.drawable.icon_cloud_off
    }
}

@Composable
fun HomeContract.UserAlert.getMessage(): String {
    return when (this) {
        is HomeContract.UserAlert.UnSyncedTransactions -> stringResource(Res.string.alert_transactions_not_synced)
    }
}

@Composable
fun HomeContract.UserAlert.getCta(): String {
    return when (this) {
        is HomeContract.UserAlert.UnSyncedTransactions -> stringResource(Res.string.retry).uppercase()
    }
}

@Preview
@Composable
fun UserAlertBannerPreview() {
    OkCreditTheme {
        UserAlertBanner(
            userAlert = HomeContract.UserAlert.UnSyncedTransactions,
            onUserAlertClicked = {}
        )
    }
}
