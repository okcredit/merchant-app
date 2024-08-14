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
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import app.okcredit.merchant.ledger.HomeContract
import app.okcredit.merchant.ledger.SubtitleType
import app.okcredit.ui.advance
import app.okcredit.ui.due
import app.okcredit.ui.icon_date
import app.okcredit.ui.icon_done
import app.okcredit.ui.icon_error
import app.okcredit.ui.icon_name
import app.okcredit.ui.theme.OkCreditTheme
import okcredit.base.units.paisa
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun CustomerRow(
    customerItem: HomeContract.HomeItem.CustomerItem,
    onItemClicked: (String) -> Unit,
    onProfileClicked: (String) -> Unit
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
                    .align(Alignment.CenterVertically)
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
                    subtitle = customerItem.subtitle,
                    type = customerItem.type,
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
                    }
                )
                Text(
                    text = if (customerItem.balance > 0.paisa) {
                        stringResource(resource = app.okcredit.ui.Res.string.advance)
                    } else {
                        stringResource(resource = app.okcredit.ui.Res.string.due)
                    },
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier.align(Alignment.End),
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
        }
        HorizontalDivider(modifier = Modifier.padding(start = 72.dp, end = 16.dp))
    }
}

@Composable
fun SubtitleText(subtitle: AnnotatedString, type: SubtitleType?) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        val image = findSubtitleImageForType(type ?: SubtitleType.NONE)
        image?.let {
            Image(
                painter = painterResource(resource = image),
                contentDescription = subtitle.text,
                colorFilter = ColorFilter.tint(findSubtitleColorForType(type ?: SubtitleType.NONE)),
                modifier = Modifier
                    .padding(end = 4.dp)
                    .align(Alignment.CenterVertically)
                    .size(16.dp)
            )
        }
        Text(
            text = subtitle,
            style = MaterialTheme.typography.labelMedium,
            maxLines = 1,
            modifier = Modifier.weight(1.0f),
            overflow = TextOverflow.Ellipsis,
            color = findSubtitleColorForType(type ?: SubtitleType.NONE),
        )
    }
}

@Composable
fun findSubtitleColorForType(subtitleType: SubtitleType): Color {
    return when (subtitleType) {
        SubtitleType.DUE_TODAY -> MaterialTheme.colorScheme.primary
        SubtitleType.DUE_DATE_PASSED -> MaterialTheme.colorScheme.error
        else -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
    }
}

fun findSubtitleImageForType(type: SubtitleType): DrawableResource? {
    return when (type) {
        SubtitleType.CUSTOMER_ADDED -> app.okcredit.ui.Res.drawable.icon_name
        SubtitleType.DUE_TODAY -> app.okcredit.ui.Res.drawable.icon_date
        SubtitleType.DUE_DATE_PASSED -> app.okcredit.ui.Res.drawable.icon_date
        SubtitleType.DUE_DATE_INCOMING ->app.okcredit.ui.Res.drawable.icon_date
        SubtitleType.TRANSACTION_SYNC_DONE -> app.okcredit.ui.Res.drawable.icon_done
        SubtitleType.TRANSACTION_SYNC_PENDING -> app.okcredit.ui.Res.drawable.icon_date
        SubtitleType.ERROR -> app.okcredit.ui.Res.drawable.icon_error
        SubtitleType.NONE -> null
    }
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
                subtitle = buildAnnotatedString { append("Last activity on 12/12/2020") },
                commonLedger = true,
                isDefaulter = false,
                type = SubtitleType.TRANSACTION_SYNC_DONE
            ),
            onItemClicked = {},
            onProfileClicked = { }
        )
    }
}
