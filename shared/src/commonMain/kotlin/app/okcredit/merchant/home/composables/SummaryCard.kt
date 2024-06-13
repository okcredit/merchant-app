package app.okcredit.merchant.home.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.okcredit.merchant.home.HomeContract
import app.okcredit.merchant.home.HomeTab
import app.okcredit.merchant.home.isCustomerTab
import app.okcredit.ui.icon_chevron_right
import app.okcredit.ui.icon_name
import app.okcredit.ui.theme.OkCreditTheme
import app.okcredit.ui.theme.grey50
import merchant_app.shared.generated.resources.Res
import merchant_app.shared.generated.resources.net_balance
import merchant_app.shared.generated.resources.you_get
import merchant_app.shared.generated.resources.you_pay
import okcredit.base.units.Paisa
import okcredit.base.units.paisa
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SummaryCard(
    summaryItem: HomeContract.HomeItem.SummaryItem,
    onSummaryCardClicked: (HomeTab) -> Unit,
) {
    Surface(
        onClick = { onSummaryCardClicked(summaryItem.homeTab) },
        shape = RoundedCornerShape(16.dp),
        color = grey50,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, top = 12.dp, end = 16.dp)
    ) {
        Column {
            NetBalanceUi(
                homeTab = summaryItem.homeTab,
                netBalance = summaryItem.netBalance,
                totalAccounts = summaryItem.totalAccounts
            )
        }
    }
}

@Composable
fun NetBalanceUi(homeTab: HomeTab, netBalance: Paisa, totalAccounts: Int) {
    Row(
        modifier = Modifier.padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1.0f)) {
            Text(
                text = stringResource(Res.string.net_balance),
                style = MaterialTheme.typography.subtitle2,
            )
            Spacer(modifier = Modifier.size(2.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(resource = app.okcredit.ui.Res.drawable.icon_name),
                    contentDescription = "",
                    modifier = Modifier
                        .size(12.dp)
                )
                Text(
                    text = "$totalAccounts Accounts",
                    style = MaterialTheme.typography.body2,
                    modifier = Modifier.padding(start = 4.dp),
                    fontSize = 10.sp,
                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
                )
            }
        }
        Column(
            modifier = Modifier
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = netBalance.toString(),
                    style = MaterialTheme.typography.subtitle1,
                    color = if (netBalance >= 0.paisa) MaterialTheme.colors.primary else MaterialTheme.colors.error
                )
                Image(
                    painter = painterResource(resource = app.okcredit.ui.Res.drawable.icon_chevron_right),
                    contentDescription = "",
                    modifier = Modifier
                        .padding(top = 2.dp)
                        .size(16.dp),
                    colorFilter = ColorFilter.tint(
                        color = if (netBalance >= 0.paisa) MaterialTheme.colors.primary else MaterialTheme.colors.error
                    )
                )
            }
            Spacer(modifier = Modifier.size(2.dp))
            Text(
                text = stringResource(
                    if (homeTab.isCustomerTab()) {
                        (if (netBalance >= 0.paisa) Res.string.you_pay else Res.string.you_get)
                    } else {
                        (if (netBalance <= 0.paisa) Res.string.you_pay else Res.string.you_get)
                    }
                ),
                style = MaterialTheme.typography.body2,
                modifier = Modifier
                    .padding(start = 4.dp)
                    .align(Alignment.End),
                fontSize = 10.sp,
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
            )
        }
    }
}

@Preview
@Composable
fun SummaryCardPreview() {
    OkCreditTheme {
        SummaryCard(
            summaryItem = HomeContract.HomeItem.SummaryItem(
                homeTab = HomeTab.CUSTOMER_TAB,
                netBalance = 1000L.paisa,
                totalAccounts = 10,
                showDefaulterReminders = true,
                statusPending = false,
                defaulterCount = 10,
            ),
            onSummaryCardClicked = {},
        )
    }
}
