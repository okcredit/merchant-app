package app.okcredit.ledger.contract.usecase

import app.okcredit.ledger.contract.model.Customer
import kotlinx.coroutines.flow.Flow

interface GetAllCustomers {

    fun execute(
        sortBy: SortBy,
        limit: Int = 5_000,
        offset: Int = 0,
    ): Flow<List<Customer>>
}

enum class SortBy {
    NAME,
    LAST_ACTIVITY,
    LAST_PAYMENT,
    BALANCE_DUE,
}
