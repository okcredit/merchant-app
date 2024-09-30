package app.okcredit.ledger.core

import app.okcredit.ledger.contract.model.Supplier
import app.okcredit.ledger.contract.usecase.SortBy
import app.okcredit.ledger.core.local.LedgerLocalSource
import app.okcredit.ledger.core.remote.LedgerRemoteSource
import app.okcredit.ledger.core.remote.models.UpdateSupplierRequest
import kotlinx.coroutines.flow.Flow
import me.tatarka.inject.annotations.Inject

@Inject
class SupplierRepository(
    localSourceLazy: Lazy<LedgerLocalSource>,
    remoteSourceLazy: Lazy<LedgerRemoteSource>,
) {

    private val localSource: LedgerLocalSource by lazy { localSourceLazy.value }
    private val remoteSource: LedgerRemoteSource by lazy { remoteSourceLazy.value }

    suspend fun syncSuppliers(businessId: String) {
        val suppliers = remoteSource.listSuppliers(businessId)
        localSource.resetSupplierList(suppliers, businessId)
    }

    fun getSupplierByMobile(mobile: String, businessId: String): Flow<Supplier?> {
        return localSource.getSupplierByMobile(mobile, businessId)
    }

    fun getSupplierDetails(customerId: String): Flow<Supplier?> {
        return localSource.getSupplierById(customerId)
    }

    suspend fun addSupplier(
        businessId: String,
        name: String,
        mobile: String?,
    ): Supplier {
        val supplier = remoteSource.addSupplier(
            businessId = businessId,
            name = name,
            mobile = mobile,
        )

        supplier.let { localSource.addSupplier(it) }
        return supplier
    }

    fun listAllSuppliers(
        businessId: String,
        sortBy: SortBy,
        limit: Int,
        offset: Int,
    ): Flow<List<Supplier>> {
        return localSource.listAllSuppliers(businessId, sortBy, limit, offset)
    }

    suspend fun updateSupplier(
        businessId: String,
        accountId: String,
        request: UpdateSupplierRequest,
    ): Supplier {
        val supplier = remoteSource.updateSupplier(
            supplierId = accountId,
            businessId = businessId,
            request = request,
        ).also {
            localSource.resetSupplier(it)
        }
        return supplier
    }

    suspend fun deleteSupplier(supplierId: String, businessId: String) {
        remoteSource.deleteSupplier(supplierId, businessId)
        localSource.markSupplierAsDeleted(supplierId)
    }
}
