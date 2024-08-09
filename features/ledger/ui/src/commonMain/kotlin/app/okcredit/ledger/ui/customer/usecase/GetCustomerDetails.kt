package app.okcredit.ledger.ui.customer.usecase

import app.okcredit.ledger.core.CustomerRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import me.tatarka.inject.annotations.Inject
import okcredit.base.units.Paisa

@Inject
class GetCustomerDetails(
    customerRepository: Lazy<CustomerRepository>,
) {

    private val repository by lazy { customerRepository.value }

    fun execute(customerId: String): Flow<CustomerModel?> {
        return repository.getCustomerDetails(customerId).map {
            if (it == null) {
                return@map null
            }
            CustomerModel(
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

    data class CustomerModel(
        val id: String,
        val name: String,
        val mobile: String?,
        val address: String,
        val profileImage: String?,
        val balance: Paisa,
        val formattedDueDate: String,
        val blockerByCustomer: Boolean,
        val blocked: Boolean,
        val reminderMode: String,
        val registered: Boolean
    )
}