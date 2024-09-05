package app.okcredit.merchant.ledger.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import app.okcredit.merchant.ledger.HomeContract
import app.okcredit.ui.advance
import app.okcredit.ui.due
import app.okcredit.ui.icon_done
import app.okcredit.ui.icon_name
import app.okcredit.ui.icon_schedule
import app.okcredit.ui.theme.OkCreditTheme
import app.okcredit.ui.theme.grey900
import kotlinx.datetime.Clock
import okcredit.base.units.Paisa
import okcredit.base.units.Timestamp
import okcredit.base.units.paisa
import okcredit.base.units.timestamp
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun SupplierRow(
    supplierItem: HomeContract.HomeItem.SupplierItem,
    onClick: (String) -> Unit,
    onProfileClicked: (String) -> Unit,
) {
    Column {
        Row(Modifier.clickable { onClick(supplierItem.supplierId) }) {
            Spacer(modifier = Modifier.width(16.dp))
            AvatarWithName(
                customerName = supplierItem.name.takeIf { it.isNotEmpty() } ?: "O",
                profileImage = supplierItem.profileImage,
                commonLedger = supplierItem.commonLedger,
                modifier = Modifier
                    .clickable { onProfileClicked(supplierItem.supplierId) }
                    .padding(vertical = 16.dp)
                    .size(44.dp)
                    .align(Alignment.CenterVertically),
            )
            Column(
                modifier = Modifier
                    .weight(1.0f)
                    .padding(start = 12.dp)
                    .align(Alignment.CenterVertically),
                verticalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = supplierItem.name,
                    style = MaterialTheme.typography.titleSmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                SubtitleTextForSupplier(
                    lastActivityMetaInfo = supplierItem.lastActivityMetaInfo,
                    lastAmount = supplierItem.lastAmount,
                    lastActivity = supplierItem.lastActivity,
                )
            }

            Column(
                modifier = Modifier
                    .padding(start = 12.dp, end = 16.dp)
                    .align(Alignment.CenterVertically),
                verticalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = supplierItem.balance.toString(),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.align(Alignment.End),
                    color = if (supplierItem.balance >= 0L.paisa) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.error
                    },
                )
                Text(
                    text = if (supplierItem.balance > 0.paisa) {
                        stringResource(resource = app.okcredit.ui.Res.string.advance)
                    } else {
                        stringResource(resource = app.okcredit.ui.Res.string.due)
                    },
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier.align(Alignment.End),
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                )
            }
        }
        Divider(modifier = Modifier.padding(start = 72.dp, end = 16.dp))
    }
}

@Composable
fun SubtitleTextForSupplier(
    lastActivityMetaInfo: Int,
    lastAmount: Paisa?,
    lastActivity: Timestamp,
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        val image =
            findSupplierSubtitleImageForType(lastActivityMetaInfo)
        val subtitle = findSubtitleForSupplier(lastActivityMetaInfo, lastAmount, lastActivity)
        Image(
            painter = painterResource(resource = image),
            contentDescription = subtitle,
            colorFilter = ColorFilter.tint(grey900),
            modifier = Modifier
                .padding(end = 4.dp)
                .align(Alignment.CenterVertically)
                .size(16.dp),
        )
        if (!subtitle.isNullOrEmpty()) {
            Text(
                text = subtitle,
                style = MaterialTheme.typography.labelMedium,
                maxLines = 1,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
            )
        }
    }
}

fun findSubtitleForSupplier(
    lastActivityMetaInfo: Int,
    lastAmount: Paisa?,
    lastActivity: Timestamp,
): String {
    return when (lastActivityMetaInfo) {
        0 -> "$lastAmount credit deleted ${lastActivity.relativeDate()}"
        1 -> "$lastAmount payment deleted ${lastActivity.relativeDate()}"
        2 -> "$lastAmount credit added ${lastActivity.relativeDate()}"
        3 -> "$lastAmount payment added ${lastActivity.relativeDate()}"
        5 -> "Processing"
        8 -> "$lastAmount credit updated ${lastActivity.relativeDate()}"
        9 -> "$lastAmount payment updated ${lastActivity.relativeDate()}"
        else -> "Supplier added ${lastActivity.relativeDate()}"
    }
}

fun findSupplierSubtitleImageForType(lastActivityMetaInfo: Int): DrawableResource {
    return when (lastActivityMetaInfo) {
        4 -> app.okcredit.ui.Res.drawable.icon_name
        5 -> app.okcredit.ui.Res.drawable.icon_schedule
        else -> app.okcredit.ui.Res.drawable.icon_done
    }
}

@Preview
@Composable
fun SupplierRowPreview() {
    OkCreditTheme {
        SupplierRow(
            supplierItem = HomeContract.HomeItem.SupplierItem(
                supplierId = "some_id",
                name = "Shubham",
                balance = 1000L.paisa,
                profileImage = null,
                commonLedger = false,
                lastActivity = Clock.System.now().timestamp,
                lastActivityMetaInfo = 0,
                lastAmount = 1000L.paisa,
            ),
            onClick = {},
            onProfileClicked = {},
        )
    }
}
