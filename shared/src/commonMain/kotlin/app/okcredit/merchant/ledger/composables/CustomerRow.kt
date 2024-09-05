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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import app.okcredit.merchant.ledger.HomeContract
import app.okcredit.ui.advance
import app.okcredit.ui.due
import app.okcredit.ui.icon_date
import app.okcredit.ui.icon_done
import app.okcredit.ui.icon_name
import app.okcredit.ui.theme.OkCreditTheme
import kotlinx.datetime.Clock
import okcredit.base.units.Paisa
import okcredit.base.units.Timestamp
import okcredit.base.units.differenceInDays
import okcredit.base.units.formattedDaysDifference
import okcredit.base.units.isZero
import okcredit.base.units.paisa
import okcredit.base.units.timestamp
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun CustomerRow(
    customerItem: HomeContract.HomeItem.CustomerItem,
    onItemClicked: (String) -> Unit,
    onProfileClicked: (String) -> Unit,
) {
    Column {
        Row(Modifier.clickable { onItemClicked(customerItem.customerId) }) {
            Spacer(modifier = Modifier.width(16.dp))
            AvatarWithName(
                customerName = customerItem.name,
                profileImage = customerItem.profileImage,
                defaulter = customerItem.isDefaulter,
                commonLedger = customerItem.commonLedger,
                modifier = Modifier
                    .padding(vertical = 16.dp)
                    .size(44.dp)
                    .clickable { onProfileClicked(customerItem.customerId) }
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
                    text = customerItem.name,
                    style = MaterialTheme.typography.titleSmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                SubtitleText(
                    lastActivityMetaInfo = customerItem.lastActivityMetaInfo,
                    lastActivity = customerItem.lastActivity,
                    lastAmount = customerItem.lastAmount,
                    dueDate = customerItem.dueDate,
                )
            }

            Column(
                modifier = Modifier
                    .padding(start = 12.dp, end = 16.dp)
                    .align(Alignment.CenterVertically),
                verticalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = customerItem.balance.toString(),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.align(Alignment.End),
                    color = if (customerItem.balance >= 0.paisa) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.error
                    },
                )
                Text(
                    text = if (customerItem.balance > 0.paisa) {
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
        HorizontalDivider(modifier = Modifier.padding(start = 72.dp, end = 16.dp))
    }
}

@Composable
fun SubtitleText(
    dueDate: Timestamp?,
    lastActivityMetaInfo: Int,
    lastActivity: Timestamp,
    lastAmount: Paisa?,
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        val image = findSubtitleImageForType(dueDate, lastActivityMetaInfo)
        val text = findSubtitle(dueDate, lastActivityMetaInfo, lastActivity, lastAmount)
        Image(
            painter = painterResource(resource = image),
            contentDescription = text,
            colorFilter = ColorFilter.tint(
                color = findSubtitleColorForType(
                    dueDate = dueDate,
                ),
            ),
            modifier = Modifier
                .padding(end = 4.dp)
                .align(Alignment.CenterVertically)
                .size(16.dp),
        )
        Text(
            text = text,
            style = MaterialTheme.typography.labelMedium,
            maxLines = 1,
            modifier = Modifier.weight(1.0f),
            overflow = TextOverflow.Ellipsis,
            color = findSubtitleColorForType(dueDate),
        )
    }
}

@Composable
fun findSubtitle(
    dueDate: Timestamp?,
    lastActivityMetaInfo: Int,
    lastActivity: Timestamp,
    lastAmount: Paisa?,
): String {
    if (dueDate != null && dueDate.isZero.not()) {
        val daysDifference = dueDate.differenceInDays()
        return when {
            daysDifference == 0 -> "Collect today"
            daysDifference < 0 -> "Pending collection since ${daysDifference.formattedDaysDifference()}"
            else -> "Collect on ${dueDate.format()}"
        }
    }

    return when (lastActivityMetaInfo) {
        0 -> "$lastAmount credit deleted ${lastActivity.relativeDate()}"
        1 -> "$lastAmount payment deleted ${lastActivity.relativeDate()}"
        2 -> "$lastAmount credit added ${lastActivity.relativeDate()}"
        3 -> "$lastAmount payment added ${lastActivity.relativeDate()}"
        5 -> "Processing"
        6 -> "$lastAmount discount deleted ${lastActivity.relativeDate()}"
        7 -> "$lastAmount discount added ${lastActivity.relativeDate()}"
        8 -> "$lastAmount credit updated ${lastActivity.relativeDate()}"
        9 -> "$lastAmount payment updated ${lastActivity.relativeDate()}"
        else -> "Account added ${lastActivity.relativeDate()}"
    }
}

fun findSubtitleImageForType(
    dueDate: Timestamp?,
    lastActivityMetaInfo: Int,
): DrawableResource {
    if (dueDate != null) {
        return app.okcredit.ui.Res.drawable.icon_date
    }
    return when (lastActivityMetaInfo) {
        4 -> app.okcredit.ui.Res.drawable.icon_name
        else -> app.okcredit.ui.Res.drawable.icon_done
    }
}

@Composable
fun findSubtitleColorForType(dueDate: Timestamp?): Color {
    if (dueDate != null && dueDate.isZero.not()) {
        val daysDifference = dueDate.differenceInDays()
        return when {
            daysDifference == 0 -> MaterialTheme.colorScheme.primary
            daysDifference < 0 -> MaterialTheme.colorScheme.error
            else -> MaterialTheme.colorScheme.primary
        }
    }

    return MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
}

@Preview
@Composable
fun CustomerRowPreview() {
    OkCreditTheme {
        CustomerRow(
            customerItem = HomeContract.HomeItem.CustomerItem(
                customerId = "id",
                name = "Jarvi qowdnqwpdb qwdoqwdnpiqwbd qowdbnp qidwqdo qds",
                balance = 0L.paisa,
                profileImage = null,
                commonLedger = true,
                isDefaulter = false,
                lastActivity = Clock.System.now().timestamp,
                lastActivityMetaInfo = 0,
                dueDate = null,
            ),
            onItemClicked = {},
            onProfileClicked = { },
        )
    }
}
