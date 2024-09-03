package app.okcredit.merchant.search.composable

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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import app.okcredit.merchant.ledger.composables.AvatarWithName
import app.okcredit.merchant.search.HomeSearchItem
import app.okcredit.ui.advance
import app.okcredit.ui.due
import merchant_app.shared.generated.resources.Res
import merchant_app.shared.generated.resources.balance
import okcredit.base.units.paisa
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun HomeSearchSupplierItem(
    supplierItem: HomeSearchItem.SupplierItem,
    onItemClicked: (String) -> Unit,
    onProfileClicked: (String) -> Unit,
) {
    Column {
        Row(Modifier.clickable { onItemClicked(supplierItem.supplierId) }) {
            Spacer(modifier = Modifier.width(16.dp))
            AvatarWithName(
                customerName = supplierItem.name.first().toString(),
                profileImage = supplierItem.profileImage,
                commonLedger = supplierItem.commonLedger,
                modifier = Modifier
                    .padding(vertical = 16.dp)
                    .clickable { onProfileClicked(supplierItem.supplierId) }
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
                                color = if (supplierItem.balance.value > 0L) {
                                    MaterialTheme.colorScheme.error
                                } else {
                                    MaterialTheme.colorScheme.primary
                                },
                            ),
                        ) {
                            append(
                                supplierItem.balance.toString(),
                            )
                        }
                        append(" ")
                        append(
                            if (supplierItem.balance.value > 0) {
                                stringResource(resource = app.okcredit.ui.Res.string.due)
                            } else {
                                stringResource(resource = app.okcredit.ui.Res.string.advance)
                            },
                        )
                    },
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
        }
        Divider(modifier = Modifier.padding(start = 72.dp, end = 16.dp))
    }
}

@Composable
@Preview
fun HomeSearchSupplierItemPreview() {
    HomeSearchSupplierItem(
        supplierItem = HomeSearchItem.SupplierItem(
            supplierId = "supplierId",
            name = "Supplier Name",
            profileImage = null,
            balance = 1000L.paisa,
            commonLedger = false,
        ),
        onItemClicked = {},
        onProfileClicked = {},
    )
}
