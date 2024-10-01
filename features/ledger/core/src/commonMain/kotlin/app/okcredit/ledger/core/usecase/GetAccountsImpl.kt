package app.okcredit.ledger.core.usecase

import app.okcredit.ledger.contract.model.Account
import app.okcredit.ledger.contract.model.AccountType
import app.okcredit.ledger.contract.usecase.GetAccounts
import app.okcredit.ledger.contract.usecase.SortBy
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding

@Inject
@ContributesBinding(AppScope::class)
class GetAccountsImpl(
    private val getCustomers: GetCustomers,
    private val getSuppliers: GetSuppliers,
) : GetAccounts {
    override fun execute(
        sortBy: SortBy,
        accountType: AccountType?,
        limit: Int,
        offset: Int,
    ): Flow<List<Account>> {
        return when (accountType) {
            AccountType.CUSTOMER -> {
                return getCustomers.execute(sortBy, limit, offset)
            }
            AccountType.SUPPLIER -> {
                return getSuppliers.execute(sortBy, limit, offset)
            }
            else -> {
                combine(
                    getCustomers.execute(sortBy, limit, offset),
                    getSuppliers.execute(sortBy, limit, offset),
                ) { customers, suppliers ->
                    customers + suppliers
                }
            }
        }
    }
}
