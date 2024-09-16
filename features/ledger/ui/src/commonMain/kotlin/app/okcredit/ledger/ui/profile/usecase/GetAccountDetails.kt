package app.okcredit.ledger.ui.profile.usecase

import app.okcredit.ledger.contract.model.AccountType
import app.okcredit.ledger.core.CustomerRepository
import app.okcredit.ledger.core.SupplierRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import me.tatarka.inject.annotations.Inject
import okcredit.base.units.Paisa

@OptIn(ExperimentalCoroutinesApi::class)
@Inject
class GetAccountDetails(
    customerRepository: Lazy<CustomerRepository>,
    supplierRepository: Lazy<SupplierRepository>
) {

    private val customerRepository by lazy { customerRepository.value }
    private val supplierRepository by lazy { supplierRepository.value }

    fun execute(
        accountId: String,
        accountType: AccountType
    ): Flow<Response?> {
        return flow {
            emit(accountType == AccountType.CUSTOMER)
        }.flatMapLatest { isCustomer ->
            if (isCustomer) {
                customerRepository.getCustomerDetails(accountId).map { customer ->
                    if (customer == null) return@map null
                    Response(
                        name = customer.name,
                        mobile = customer.mobile ?: "",
                        balance = customer.balance,
                        profileImage = customer.profileImage ?: "",
                        registered = customer.registered,
                        blocked = customer.blockedBySelf,
                        transactionRestricted = customer.settings.addTransactionRestricted,
                        address = customer.address ?: ""
                    )
                }
            } else {
                supplierRepository.getSupplierDetails(accountId).map { supplier ->
                    if (supplier == null) return@map null
                    Response(
                        name = supplier.name,
                        mobile = supplier.mobile ?: "",
                        balance = supplier.balance,
                        profileImage = supplier.profileImage ?: "",
                        registered = supplier.registered,
                        blocked = supplier.settings.blockedBySupplier,
                        transactionRestricted = supplier.settings.addTransactionRestricted,
                        address = supplier.address ?: ""
                    )
                }
            }
        }
    }

    data class Response(
        val name: String,
        val mobile: String,
        val balance: Paisa,
        val profileImage: String,
        val address: String,
        val registered: Boolean,
        val blocked: Boolean,
        val transactionRestricted: Boolean,
    )
}
