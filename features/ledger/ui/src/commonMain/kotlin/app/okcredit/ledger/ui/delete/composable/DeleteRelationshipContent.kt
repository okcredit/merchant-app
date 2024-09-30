package app.okcredit.ledger.ui.delete.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import app.okcredit.ledger.ui.balance_advance
import app.okcredit.ledger.ui.balance_due
import app.okcredit.ledger.ui.del_cst_msg
import app.okcredit.ledger.ui.del_cst_msg_no_mobile
import app.okcredit.ledger.ui.del_cst_msg_settlement
import app.okcredit.ledger.ui.del_cst_settlement_label_credit
import app.okcredit.ledger.ui.del_cst_settlement_label_payment
import app.okcredit.ledger.ui.del_cst_supplier_msg
import app.okcredit.ledger.ui.del_cst_supplier_msg_no_mobile
import app.okcredit.ledger.ui.del_cst_supplier_msg_settlement
import app.okcredit.ledger.ui.delete
import app.okcredit.ui.Res
import app.okcredit.ui.icon_account_125dp
import app.okcredit.ui.icon_credit_up
import app.okcredit.ui.icon_delete
import app.okcredit.ui.icon_payments
import okcredit.base.utils.toFormattedAmount
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun DeleteRelationshipContent(
    isLoading: Boolean,
    contentPadding: PaddingValues,
    name: String,
    balance: Long,
    mobile: String,
    isSupplier: Boolean,
    shouldSettle: Boolean,
    onDeleteClicked: () -> Unit,
    onSettlementClicked: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(contentPadding)
            .background(color = MaterialTheme.colorScheme.surface),
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 2.dp,
            ),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface,
            ),
            shape = RoundedCornerShape(0.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Row(
                        modifier = Modifier
                            .weight(1f)
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Image(
                            painter = painterResource(Res.drawable.icon_account_125dp),
                            contentDescription = null,
                            modifier = Modifier.size(24.dp),
                            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface),
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = name,
                            style = MaterialTheme.typography.bodyLarge,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .align(Alignment.CenterVertically),
                        horizontalAlignment = Alignment.End,
                    ) {
                        Text(
                            text = balance.toFormattedAmount(true),
                            style = MaterialTheme.typography.bodyLarge,
                            color = if (balance > 0L) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
                        )
                        Text(
                            text = getBalanceText(balance),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))

                if (shouldSettle) {
                    Row(
                        modifier = Modifier
                            .clickable { onSettlementClicked() }
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .background(MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(20.dp)),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                    ) {
                        if (isLoading) {
                            Column {
                                Spacer(modifier = Modifier.size(8.dp))
                                CircularProgressIndicator(
                                    modifier = Modifier.size(24.dp),
                                    color = MaterialTheme.colorScheme.surface,
                                )
                                Spacer(modifier = Modifier.size(8.dp))
                            }
                        } else {
                            Icon(
                                modifier = Modifier.padding(vertical = 8.dp),
                                painter = painterResource(
                                    if (balance < 0L) {
                                        Res.drawable.icon_payments
                                    } else {
                                        Res.drawable.icon_credit_up
                                    },
                                ),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.surface,
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = getSettlementButtonText(balance = balance),
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.surface,
                            )
                        }
                    }
                } else {
                    Row(
                        modifier = Modifier
                            .clickable { onDeleteClicked() }
                            .fillMaxWidth()
                            .background(
                                MaterialTheme.colorScheme.surface,
                                shape = RoundedCornerShape(20.dp),
                            )
                            .padding(horizontal = 16.dp)
                            .border(
                                width = 1.dp,
                                shape = RoundedCornerShape(20.dp),
                                color = MaterialTheme.colorScheme.error,
                            ),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                    ) {
                        if (isLoading) {
                            Column {
                                Spacer(modifier = Modifier.size(8.dp))
                                CircularProgressIndicator(
                                    modifier = Modifier.size(24.dp),
                                    color = MaterialTheme.colorScheme.error,
                                    strokeWidth = 2.dp,
                                )
                                Spacer(modifier = Modifier.size(8.dp))
                            }
                        } else {
                            Icon(
                                modifier = Modifier.padding(vertical = 8.dp),
                                painter = painterResource(Res.drawable.icon_delete),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.error,
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = stringResource(app.okcredit.ledger.ui.Res.string.delete),
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.error,
                            )
                        }
                    }
                }
            }
        }

        Text(
            text = getMessageText(
                balance = balance,
                mobile = mobile,
                isSupplier = isSupplier,
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
                .align(Alignment.CenterHorizontally),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}

@Composable
private fun getBalanceText(
    balance: Long?,
) = stringResource(if (balance != null && balance < 0L) app.okcredit.ledger.ui.Res.string.balance_due else app.okcredit.ledger.ui.Res.string.balance_advance)

@Composable
private fun getSettlementButtonText(
    balance: Long,
): String {
    return if (balance < 0L) {
        stringResource(
            app.okcredit.ledger.ui.Res.string.del_cst_settlement_label_payment,
            balance.toFormattedAmount(true),
        )
    } else {
        stringResource(
            app.okcredit.ledger.ui.Res.string.del_cst_settlement_label_credit,
            balance.toFormattedAmount(true),
        )
    }
}

@Composable
private fun getMessageText(
    balance: Long,
    mobile: String,
    isSupplier: Boolean,
): String {
    return if (balance == 0L) {
        if (mobile.isEmpty()) {
            stringResource(
                if (isSupplier) {
                    app.okcredit.ledger.ui.Res.string.del_cst_supplier_msg_no_mobile
                } else {
                    app.okcredit.ledger.ui.Res.string.del_cst_msg_no_mobile
                },
            )
        } else {
            stringResource(
                if (isSupplier) {
                    app.okcredit.ledger.ui.Res.string.del_cst_supplier_msg
                } else {
                    app.okcredit.ledger.ui.Res.string.del_cst_msg
                },
            )
        }
    } else {
        stringResource(
            if (isSupplier) {
                app.okcredit.ledger.ui.Res.string.del_cst_supplier_msg_settlement
            } else {
                app.okcredit.ledger.ui.Res.string.del_cst_msg_settlement
            },
        )
    }
}

@Preview
@Composable
fun DeleteRelationshipContentPreview() {
    DeleteRelationshipContent(
        isLoading = false,
        contentPadding = PaddingValues(0.dp),
        name = "John Doe",
        balance = -1000,
        shouldSettle = true,
        onDeleteClicked = {},
        onSettlementClicked = {},
        mobile = "13124241",
        isSupplier = true,
    )
}
