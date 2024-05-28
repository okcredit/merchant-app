package app.okcredit.ledger.contract.usecase

import app.okcredit.ledger.contract.model.Supplier
import kotlinx.coroutines.flow.Flow

interface GetAllSuppliers {

    fun execute(
        sortBy: SortBy,
        limit: Int,
        offset: Int,
    ): Flow<List<Supplier>>
}
