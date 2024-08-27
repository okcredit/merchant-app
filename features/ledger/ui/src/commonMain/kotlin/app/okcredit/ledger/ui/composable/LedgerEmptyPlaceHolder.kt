package app.okcredit.ledger.ui.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import app.okcredit.ledger.ui.empty_placeholder_customer_ledger
import app.okcredit.ledger.ui.empty_placeholder_supplier_ledger
import app.okcredit.ledger.ui.learn_more
import app.okcredit.ledger.ui.model.AccountType
import app.okcredit.ledger.ui.placeholder_empty_customer_ledger
import app.okcredit.ledger.ui.placeholder_empty_supplier_ledger
import app.okcredit.ui.icon_help_outline
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import org.jetbrains.compose.ui.tooling.preview.Preview


@Composable
fun LedgerEmptyPlaceHolder(
    accountType: AccountType,
    onLearnMoreClicked: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.padding(top = 24.dp))
        Image(
            imageVector = if (accountType.isSupplier()) {
                vectorResource(app.okcredit.ledger.ui.Res.drawable.placeholder_empty_supplier_ledger)
            } else {
                vectorResource(app.okcredit.ledger.ui.Res.drawable.placeholder_empty_customer_ledger)
            },
            contentDescription = null,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Text(
            text = if (accountType.isSupplier()) {
                stringResource(app.okcredit.ledger.ui.Res.string.empty_placeholder_supplier_ledger)
            } else {
                stringResource(app.okcredit.ledger.ui.Res.string.empty_placeholder_customer_ledger)
            },
            style = MaterialTheme.typography.titleSmall,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        if (accountType.isSupplier()) {
            Spacer(modifier = Modifier.padding(top = 20.dp))
            Row(
                modifier = Modifier.clickable {
                    onLearnMoreClicked()
                }
            ) {
                Icon(
                    painter = painterResource(app.okcredit.ui.Res.drawable.icon_help_outline),
                    contentDescription = "help",
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.padding(4.dp))
                Text(
                    text = stringResource(app.okcredit.ledger.ui.Res.string.learn_more),
                    style = MaterialTheme.typography.titleSmall,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Preview
@Composable
fun LedgerEmptyPlaceHolderCustomer() {
    LedgerEmptyPlaceHolder(
        accountType = AccountType.Customer,
        onLearnMoreClicked = {}
    )
}

@Preview
@Composable
fun LedgerEmptyPlaceHolderSupplier() {
    LedgerEmptyPlaceHolder(
        accountType = AccountType.Customer,
        onLearnMoreClicked = {}
    )
}
