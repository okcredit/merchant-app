package app.okcredit.ledger.ui.statement.composable

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import app.okcredit.ledger.contract.model.AccountStatus
import app.okcredit.ledger.contract.model.Customer
import app.okcredit.ledger.contract.model.Transaction
import app.okcredit.ledger.ui.Res
import app.okcredit.ledger.ui.discount_offered
import app.okcredit.ledger.ui.edited
import app.okcredit.ledger.ui.model.TransactionDueInfo
import app.okcredit.ledger.ui.online_payment
import app.okcredit.ledger.ui.statement.AccountStatementContract
import app.okcredit.ledger.ui.subscription
import app.okcredit.ui.theme.dark_stroke
import okcredit.base.units.Paisa
import okcredit.base.units.Timestamp
import okcredit.base.utils.toFormattedAmount
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TransactionItem(
    transaction: TransactionDueInfo,
    onClickTransaction: (transaction: Transaction) -> Unit,
    onLongClickTransaction: (transaction: Transaction) -> Unit,
) {
    Surface(
        modifier = Modifier
            .padding(0.dp, 3.dp, 0.dp, 3.dp)
            .combinedClickable(
                onClick = {
                    onClickTransaction.invoke(transaction.transaction)
                },
                onLongClick = {
                    onLongClickTransaction.invoke(transaction.transaction)
                }
            )
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
        ) {
            Card(
                border = BorderStroke(
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    width = 1.dp
                ),
                elevation = CardDefaults.elevatedCardElevation(defaultElevation = 0.dp),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .padding(vertical = 4.dp, horizontal = 8.dp)
                    .width(220.dp)
                    .align(
                        if (transaction.transaction.type == Transaction.Type.PAYMENT || transaction.transaction.type == Transaction.Type.CREDIT) Alignment.Start else Alignment.End
                    ),
            ) {
                Column(
                    modifier = Modifier
                        .padding(vertical = 3.dp, horizontal = 0.dp)
                ) {
                    if (getTransactionType(transaction.transaction) != null) {
                        Box(
                            Modifier
                                .background(MaterialTheme.colorScheme.tertiaryContainer)
                                .fillMaxWidth()
                        ) {
                            Text(
                                text = stringResource(getTransactionType(transaction.transaction)!!),
                                style = MaterialTheme.typography.labelMedium.copy(
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontWeight = FontWeight.Bold
                                ),
                                modifier = Modifier.padding(10.dp, 2.dp, 10.dp, 2.dp)
                            )
                        }
                    }
                    if (transaction.account != null) {
                        Text(
                            text = transaction.account.name,
                            style = MaterialTheme.typography.labelMedium.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontWeight = FontWeight.Bold
                            ),
                            modifier = Modifier.padding(10.dp, 2.dp, 10.dp, 2.dp)
                        )
                    }

                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = transaction.transaction.amount.value.toFormattedAmount(true),
                            style = MaterialTheme.typography.headlineSmall.copy(
                                color = if (transaction.transaction.type == Transaction.Type.CREDIT || transaction.transaction.amount < Paisa.ZERO)
                                    MaterialTheme.colorScheme.error
                                else MaterialTheme.colorScheme.onErrorContainer
                            ),
                            modifier = Modifier.padding(10.dp, 2.dp, 10.dp, 2.dp)
                        )
                        Text(
                            text = transaction.transaction.billDate.relativeDateAndTime(),
                            style = MaterialTheme.typography.labelSmall.copy(color = dark_stroke),
                            modifier = Modifier
                                .padding(24.dp, 2.dp, 10.dp, 2.dp)
                                .align(Alignment.Bottom)
                        )
                    }
                }
            }
        }
    }
}

fun getTransactionType(transaction: Transaction): StringResource? {
    when {
        transaction.collectionId != null -> {
            return Res.string.online_payment
        }

        transaction.isSubscriptionTransaction -> {
            return Res.string.subscription
        }

        transaction.category == Transaction.Category.DISCOUNT -> {
            return Res.string.discount_offered
        }

        transaction.amountUpdated -> {
            return Res.string.edited
        }

        else -> {
            return null
        }
    }
}

@Preview
@Composable
fun TransactionItemPreview() {
    TransactionItem(
        transaction = AccountStatementContract.State().transactions[0],
        onClickTransaction = {},
        onLongClickTransaction = {},
    )
}
