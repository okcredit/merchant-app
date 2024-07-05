package app.okcredit.ledger.core

import app.okcredit.ledger.contract.model.Customer
import app.okcredit.ledger.contract.usecase.SortBy
import app.okcredit.ledger.core.local.LedgerLocalSource
import app.okcredit.ledger.core.remote.LedgerRemoteSource
import kotlinx.coroutines.flow.Flow
import me.tatarka.inject.annotations.Inject

@Inject
class CustomerRepository(
    localSourceLazy: Lazy<LedgerLocalSource>,
    remoteSourceLazy: Lazy<LedgerRemoteSource>,
) {

    private val localSource by lazy { localSourceLazy.value }
    private val remoteSource by lazy { remoteSourceLazy.value }

    suspend fun syncCustomers(businessId: String) {
        val customers = remoteSource.listCustomers(businessId)
        localSource.resetCustomerList(customers, businessId)
    }

    fun listAllCustomers(
        businessId: String,
        sortBy: SortBy,
        limit: Int,
        offset: Int,
    ): Flow<List<Customer>> {
        return localSource.listAllCustomers(
            businessId = businessId,
            sortBy = sortBy,
            limit = limit,
            offset = offset,
        )
    }

    suspend fun addCustomer(
        businessId: String,
        name: String,
        mobile: String?,
        reactivate: Boolean,
    ): Customer {
        val customer = remoteSource.addCustomer(
            name = name,
            mobile = mobile,
            reactivate = reactivate,
            businessId = businessId,
        )

        customer.let { localSource.addCustomer(it) }
        return customer
    }

    fun getCustomerByMobile(mobile: String, businessId: String): Flow<Customer?> {
        return localSource.getCustomerByMobile(mobile, businessId)
    }

    fun getCustomerDetails(customerId: String): Flow<Customer?> {
        return localSource.getCustomerById(customerId)
    }

    suspend fun deleteCustomer(customerId: String, businessId: String) {
        remoteSource.deleteCustomer(customerId, businessId)
        localSource.markCustomerAsDeleted(customerId)
    }
}
