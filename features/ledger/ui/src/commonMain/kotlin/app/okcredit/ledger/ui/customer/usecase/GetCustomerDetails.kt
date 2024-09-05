package app.okcredit.ledger.ui.customer.usecase

import app.okcredit.ledger.core.CustomerRepository
import app.okcredit.ledger.ui.customer.CustomerLedgerContract.CustomerDetails
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import me.tatarka.inject.annotations.Inject

@Inject
class GetCustomerDetails(
    customerRepository: Lazy<CustomerRepository>,
) {

    private val repository by lazy { customerRepository.value }

    fun execute(customerId: String): Flow<CustomerDetails?> {
        return repository.getCustomerDetails(customerId).map {
            if (it == null) {
                return@map null
            }
            CustomerDetails(
                id = it.id,
                name = it.name,
                mobile = it.mobile,
                profileImage = it.profileImage,
                balance = it.balance,
                blockerByCustomer = it.settings.blockedByCustomer,
                blocked = it.settings.addTransactionRestricted,
                reminderMode = it.settings.reminderMode ?: "whatsapp",
                registered = it.registered,
                formattedDueDate = "",
                address = "",
            )
        }
    }
}
