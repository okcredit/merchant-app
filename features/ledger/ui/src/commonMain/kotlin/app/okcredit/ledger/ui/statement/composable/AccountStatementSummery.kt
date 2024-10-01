package app.okcredit.ledger.ui.statement.composable

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.okcredit.ledger.ui.credit_plural
import app.okcredit.ledger.ui.discounts_offered
import app.okcredit.ledger.ui.net_balance
import app.okcredit.ledger.ui.payment_plural
import app.okcredit.ledger.ui.statement.AccountStatementContract
import app.okcredit.ui.Res
import app.okcredit.ui.icon_account_balance
import app.okcredit.ui.icon_arrow_down
import app.okcredit.ui.icon_arrow_drop_up
import okcredit.base.utils.toFormattedAmount
import org.jetbrains.compose.resources.getPluralString
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.pluralStringResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun AccountStatementSummery(state: AccountStatementContract.State) {
    Card(
        border = BorderStroke(color = MaterialTheme.colorScheme.onPrimaryContainer, width = 1.dp),
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = 0.dp
        ),
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column {
            val totalAmount =
                state.totalCreditAmount - state.totalPaymentAmount - state.totalDiscountAmount

            Row(
                modifier = Modifier.padding(12.dp, 28.dp, 0.dp, 0.dp)
            ) {
                OutlinedButton(
                    onClick = {},
                    modifier = Modifier
                        .width(40.dp)
                        .height(40.dp)
                        .align(Alignment.CenterVertically),
                    shape = CircleShape,
                    border = BorderStroke(
                        color = MaterialTheme.colorScheme.outlineVariant,
                        width = 1.dp
                    ),
                    contentPadding = PaddingValues(0.dp), // avoid the little icon
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.outlineVariant)
                ) {
                    Icon(
                        modifier = Modifier.then(
                            Modifier
                                .size(24.dp)
                                .height(24.dp)
                                .width(24.dp)
                        ),
                        painter = painterResource(Res.drawable.icon_account_balance),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.surfaceVariant
                    )
                }

                Column(
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(8.dp),
                ) {
                    Text(
                        text = stringResource(app.okcredit.ledger.ui.Res.string.net_balance),
                        style = MaterialTheme.typography.labelMedium
                    )
                    Text(
                        text = totalAmount.toFormattedAmount(true),
                        style = MaterialTheme.typography.headlineSmall.copy(
                            color = if (totalAmount > 0) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
                        )
                    )
                }
            }

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .padding(0.dp, 28.dp, 0.dp, 0.dp)
                    .fillMaxWidth()
            ) {
                Row(
                    horizontalArrangement = Arrangement.Start,
                    modifier = Modifier.padding(12.dp, 0.dp, 0.dp, 0.dp)
                ) {
                    OutlinedButton(
                        onClick = {},
                        modifier = Modifier
                            .width(40.dp)
                            .height(40.dp)
                            .align(Alignment.CenterVertically),
                        shape = CircleShape,
                        border = BorderStroke(
                            color = MaterialTheme.colorScheme.outlineVariant,
                            width = 1.dp
                        ),
                        contentPadding = PaddingValues(0.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.outlineVariant)
                    ) {
                        Icon(
                            modifier = Modifier.then(
                                Modifier
                                    .size(20.dp)
                                    .height(20.dp)
                                    .width(20.dp)
                            ),
                            painter = painterResource(Res.drawable.icon_arrow_down),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }

                    Column(
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .padding(8.dp),
                    ) {
                        Text(
                            text = pluralStringResource(
                                app.okcredit.ledger.ui.Res.plurals.payment_plural,
                                state.totalPaymentCount,
                                state.totalPaymentCount
                            ),
                            style = MaterialTheme.typography.labelMedium
                        )
                        Text(
                            text = state.totalPaymentAmount.toFormattedAmount(true),
                            style = MaterialTheme.typography.headlineSmall.copy(
                                color = MaterialTheme.colorScheme.primary,
                            )
                        )
                    }
                }

                Card(
                    modifier = Modifier
                        .height(40.dp)
                        .width(0.5.dp)
                        .align(Alignment.CenterVertically),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.onPrimaryContainer
                    ),
                ) {}

                Row(
                    modifier = Modifier.padding(0.dp, 0.dp, 12.dp, 0.dp)
                ) {
                    OutlinedButton(
                        onClick = {},
                        modifier = Modifier
                            .width(40.dp)
                            .height(40.dp)
                            .align(Alignment.CenterVertically),
                        shape = CircleShape,
                        border = BorderStroke(
                            color = MaterialTheme.colorScheme.outlineVariant,
                            width = 1.dp
                        ),
                        contentPadding = PaddingValues(0.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.outlineVariant)
                    ) {
                        Icon(
                            modifier = Modifier.then(
                                Modifier
                                    .size(20.dp)
                                    .height(20.dp)
                                    .width(20.dp)
                            ),
                            painter = painterResource(Res.drawable.icon_arrow_drop_up),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.error
                        )
                    }

                    Column(
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .padding(8.dp),
                    ) {
                        Text(
                            text = pluralStringResource(
                                app.okcredit.ledger.ui.Res.plurals.credit_plural,
                                state.totalCreditCount,
                                state.totalCreditCount
                            ),
                            style = MaterialTheme.typography.headlineSmall
                        )
                        Text(
                            text = state.totalCreditAmount.toFormattedAmount(true),
                            style = MaterialTheme.typography.headlineSmall.copy(
                                color = MaterialTheme.colorScheme.error,
                            )
                        )
                    }
                }
            }

            if (state.totalDiscountAmount > 0) {
                Spacer(
                    Modifier
                        .height(1.dp)
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.outlineVariant)
                )

                Row(
                    modifier = Modifier.padding(12.dp),
                ) {
                    Icon(
                        modifier = Modifier.then(
                            Modifier
                                .size(16.dp)
                                .height(16.dp)
                                .width(16.dp)
                        ),
                        imageVector = Icons.Default.DateRange,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onErrorContainer
                    )

                    Text(
                        text = stringResource(app.okcredit.ledger.ui.Res.string.discounts_offered)
                            .plus(": ")
                            .plus(state.totalDiscountAmount.toFormattedAmount(withRupeePrefix = true)),
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(6.dp, 0.dp, 0.dp, 0.dp)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun MenuItemPreview() {
    AccountStatementSummery(state = AccountStatementContract.State())
}
