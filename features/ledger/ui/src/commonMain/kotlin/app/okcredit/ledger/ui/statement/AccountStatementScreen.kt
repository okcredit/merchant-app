package app.okcredit.ledger.ui.statement

import androidx.compose.runtime.Composable
import app.okcredit.ledger.contract.model.AccountType
import cafe.adriel.voyager.core.screen.Screen

data class AccountStatementScreen(
    val accountId: String,
    val accountType: AccountType,
) : Screen {

    @Composable
    override fun Content() {

    }
}