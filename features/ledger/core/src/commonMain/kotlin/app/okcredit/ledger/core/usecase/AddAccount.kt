package app.okcredit.ledger.core.usecase

import app.okcredit.ledger.contract.model.Account
import app.okcredit.ledger.contract.model.AccountType
import app.okcredit.ledger.contract.model.CustomerStatus
import app.okcredit.ledger.contract.usecase.CyclicAccountError
import app.okcredit.ledger.contract.usecase.DeletedCustomerError
import app.okcredit.ledger.contract.usecase.InvalidNameError
import app.okcredit.ledger.contract.usecase.MobileConflictError
import app.okcredit.ledger.core.CustomerRepository
import app.okcredit.ledger.core.SupplierRepository
import kotlinx.coroutines.flow.firstOrNull
import me.tatarka.inject.annotations.Inject
import tech.okcredit.identity.contract.usecase.GetActiveBusinessId

@Inject
class AddAccount(
    getActiveBusinessIdLazy: Lazy<GetActiveBusinessId>,
    customerRepositoryLazy: Lazy<CustomerRepository>,
    supplierRepositoryLazy: Lazy<SupplierRepository>,
) {

    private val getActiveBusinessId by lazy { getActiveBusinessIdLazy.value }
    private val customerRepository by lazy { customerRepositoryLazy.value }
    private val supplierRepository by lazy { supplierRepositoryLazy.value }

    suspend fun execute(
        name: String,
        mobile: String?,
        accountType: AccountType,
    ): Account {
        if (name.isEmpty()) {
            throw InvalidNameError()
        }

        val activeBusinessId = getActiveBusinessId.execute()
        // Check if customer with mobile already exists
        if (!mobile.isNullOrBlank()) {
            if (accountType == AccountType.CUSTOMER) {
                checkForCustomerMobileConflict(mobile, activeBusinessId)
            } else {
                checkForSupplierMobileConflict(mobile, activeBusinessId)
            }
        }

        return if (accountType == AccountType.CUSTOMER) {
            customerRepository.addCustomer(
                businessId = activeBusinessId,
                name = name,
                mobile = mobile,
                reactivate = false,
            )
        } else {
            supplierRepository.addSupplier(
                businessId = activeBusinessId,
                name = name,
                mobile = mobile,
            )
        }
    }

    private suspend fun checkForCustomerMobileConflict(mobile: String, activeBusinessId: String) {
        val customer =
            customerRepository.getCustomerByMobile(mobile, activeBusinessId).firstOrNull()
        if (customer != null) {
            if (customer.status == CustomerStatus.DELETED) {
                throw DeletedCustomerError(customer.id)
            } else {
                throw MobileConflictError(customer.id, customer.name)
            }
        }

        val supplier =
            supplierRepository.getSupplierByMobile(mobile, activeBusinessId).firstOrNull()
        if (supplier != null) {
            throw CyclicAccountError(supplier.id, supplier.name)
        }
    }

    private suspend fun checkForSupplierMobileConflict(mobile: String, activeBusinessId: String) {
        val supplier =
            supplierRepository.getSupplierByMobile(mobile, activeBusinessId).firstOrNull()
        if (supplier != null) {
            throw MobileConflictError(supplier.id, supplier.name)
        }

        val customer = customerRepository.getCustomerByMobile(mobile, activeBusinessId).firstOrNull()
        if (customer != null) {
            throw CyclicAccountError(customer.id, customer.name)
        }
    }
}
