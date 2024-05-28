package app.okcredit.ledger.core.usecase

import app.okcredit.ledger.contract.usecase.BalanceNonZeroError
import app.okcredit.ledger.contract.usecase.CustomerNotFoundError
import app.okcredit.ledger.contract.usecase.PermissionDeniedError
import app.okcredit.ledger.core.CustomerRepository
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.flow.firstOrNull
import me.tatarka.inject.annotations.Inject
import okcredit.base.network.ApiError
import okcredit.base.units.paisa

@Inject
class DeleteCustomer(
    private val repository: CustomerRepository,
) {

    suspend fun execute(customerId: String) {
        val customer = repository.getCustomerDetails(customerId).firstOrNull()
            ?: throw CustomerNotFoundError()

        if (customer.summary.balance != 0.paisa) {
            throw BalanceNonZeroError()
        }

        try {
            repository.deleteCustomer(customerId, customer.businessId)
        } catch (error: ApiError) {
            if (error.code == HttpStatusCode.Conflict.value) {
                throw PermissionDeniedError()
            }

            throw error
        } catch (e: Exception) {
            throw e
        }
    }
}
