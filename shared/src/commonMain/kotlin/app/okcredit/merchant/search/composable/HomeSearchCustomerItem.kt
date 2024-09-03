package app.okcredit.merchant.search.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import app.okcredit.merchant.ledger.composables.AvatarWithName
import app.okcredit.merchant.search.HomeSearchItem
import app.okcredit.ui.advance
import app.okcredit.ui.due
import app.okcredit.ui.icon_whatsapp
import app.okcredit.ui.theme.OkCreditTheme
import merchant_app.shared.generated.resources.Res
import merchant_app.shared.generated.resources.balance
import okcredit.base.units.formatPaisa
import okcredit.base.units.paisa
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun HomeSearchCustomerItem(
    customerItem: HomeSearchItem.CustomerItem,
    onItemClicked: (String) -> Unit,
    onProfileClicked: (String) -> Unit,
    onWhatsAppClicked: (String) -> Unit,
) {
    Column {
        Row(Modifier.clickable { onItemClicked(customerItem.customerId) }) {
            Spacer(modifier = Modifier.width(16.dp))
            AvatarWithName(
                customerName = customerItem.name.first().toString(),
                profileImage = customerItem.profileImage,
                defaulter = customerItem.isDefaulter,
                commonLedger = customerItem.commonLedger,
                modifier = Modifier
                    .padding(vertical = 16.dp)
                    .clickable { onProfileClicked(customerItem.customerId) }
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
                    text = customerItem.name,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = buildAnnotatedString {
                        append(stringResource(resource = Res.string.balance))
                        append(" ")
                        withStyle(
                            style = SpanStyle(
                                color = if (customerItem.balance.value < 0) {
                                    MaterialTheme.colorScheme.error
                                } else {
                                    MaterialTheme.colorScheme.primary
                                },
                            ),
                        ) {
                            append(
                                formatPaisa(customerItem.balance.value, true),
                            )
                        }
                        append(" ")
                        append(
                            if (customerItem.balance.value < 0L) {
                                stringResource(resource = app.okcredit.ui.Res.string.due)
                            } else {
                                stringResource(resource = app.okcredit.ui.Res.string.advance)
                            },
                        )
                    },
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.primaryContainer, CircleShape)
                    .size(36.dp)
                    .clickable {
                        onWhatsAppClicked(customerItem.customerId)
                    }
                    .align(Alignment.CenterVertically),
            ) {
                Icon(
                    painter = painterResource(resource = app.okcredit.ui.Res.drawable.icon_whatsapp),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
        }
        Divider(modifier = Modifier.padding(start = 72.dp, end = 16.dp))
    }
}

@Composable
@Preview
fun HomeSearchCustomerItemPreview() {
    OkCreditTheme {
        HomeSearchCustomerItem(
            HomeSearchItem.CustomerItem(
                customerId = "1",
                profileImage = null,
                name = "John Doe",
                balance = 1000L.paisa,
                commonLedger = false,
                isDefaulter = false,
            ),
            onItemClicked = {},
            onProfileClicked = {},
            onWhatsAppClicked = {},
        )
    }
}
