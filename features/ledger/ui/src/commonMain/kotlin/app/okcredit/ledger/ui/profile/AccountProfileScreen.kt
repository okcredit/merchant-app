package app.okcredit.ledger.ui.profile

import androidx.compose.runtime.Composable
import app.okcredit.ledger.ui.model.AccountType

data class AccountProfileScreen(
    val accountId: String,
    val accountType: AccountType
) {

    @Composable
    fun Content() {

    }
}