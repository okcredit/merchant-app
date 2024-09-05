package app.okcredit.merchant.search.usecase

import app.okcredit.ledger.contract.model.AccountType
import app.okcredit.ledger.core.usecase.AddAccount
import me.tatarka.inject.annotations.Inject

@Inject
class AddCustomerOrSupplierFromSearch(
    private val addAccount: AddAccount,
) {

    suspend fun execute(
        name: String,
        mobile: String?,
        accountType: AccountType,
    ): Pair<String, Boolean> {
        val account = addAccount.execute(
            name = name,
            mobile = mobile,
            accountType = accountType,
        )
        return account.id to true
    }
}
