package app.okcredit.ledger.core.usecase

import app.okcredit.ledger.contract.model.AccountType
import app.okcredit.ledger.contract.model.isCustomer
import app.okcredit.ledger.core.CustomerRepository
import app.okcredit.ledger.core.SupplierRepository
import app.okcredit.ledger.core.remote.models.UpdateCustomerRequest
import app.okcredit.ledger.core.remote.models.UpdateSupplierRequest
import app.okcredit.ledger.core.remote.models.createUpdateCustomerRequest
import app.okcredit.ledger.core.remote.models.createUpdateSupplierRequest
import kotlinx.coroutines.flow.first
import me.tatarka.inject.annotations.Inject
import tech.okcredit.identity.contract.usecase.GetActiveBusinessId

sealed class RequestUpdateAccount {

    data class UpdateAddress(
        val address: String?
    ) : RequestUpdateAccount()

    data class UpdateMobile(
        val mobile: String,
    ) : RequestUpdateAccount()

    data class UpdateAccountState(
        val blocked: Boolean,
    ) : RequestUpdateAccount()

    data class UpdateReminderMode(
        val reminderMode: String,
    ) : RequestUpdateAccount()

    data class UpdateName(
        val desc: String,
    ) : RequestUpdateAccount()

    data class UpdateProfileImage(
        val profileImage: String,
    ) : RequestUpdateAccount()

    data class UpdateLanguage(
        val lang: String,
    ) : RequestUpdateAccount()

    data class UpdateTransactionAlertStatus(
        val isEnable: Boolean,
    ) : RequestUpdateAccount()

    data class UpdateAddTransactionRestrictedStatus(
        val isAddTransactionRestricted: Boolean,
    ) : RequestUpdateAccount()

    data class UpdateTransactionAlertStatusAndLanguage(
        val isEnable: Boolean,
        val lang: String?,
    ) : RequestUpdateAccount()

    data object DeleteDueCustomDate : RequestUpdateAccount()

    data class UpdateDueCustomDate(val dueCustomDate: Long) : RequestUpdateAccount()
}

@Inject
class UpdateAccount(
    customerRepository: Lazy<CustomerRepository>,
    supplierRepository: Lazy<SupplierRepository>,
    getActiveBusinessId: Lazy<GetActiveBusinessId>
) {

    private val customerRepository by lazy { customerRepository.value }
    private val supplierRepository by lazy { supplierRepository.value }
    private val getActiveBusinessId by lazy { getActiveBusinessId.value }

    suspend fun execute(
        accountId: String,
        accountType: AccountType,
        request: RequestUpdateAccount
    ) {
        val businessId = getActiveBusinessId.execute()
        if (request is RequestUpdateAccount.UpdateProfileImage) {
            //TODO file upload for profile image
        }
        if (accountType.isCustomer()) {
            val updateRequest =
                createUpdateRequestForCustomer(accountId = accountId, request = request) ?: return
            customerRepository.updateCustomer(
                businessId = businessId,
                customerId = accountId,
                request = updateRequest
            )
        } else {
            val updateRequest = createUpdateRequestForSupplier(accountId, request) ?: return
            supplierRepository.updateSupplier(
                businessId = businessId,
                accountId = accountId,
                request = updateRequest
            )
        }
    }

    private suspend fun createUpdateRequestForSupplier(
        accountId: String,
        request: RequestUpdateAccount
    ): UpdateSupplierRequest? {
        val supplier = supplierRepository.getSupplierDetails(accountId).first() ?: return null
        return when (request) {
            is RequestUpdateAccount.UpdateMobile -> {
                supplier.createUpdateSupplierRequest(
                    id = accountId,
                    name = supplier.name,
                    mobile = request.mobile,
                    profileImage = supplier.profileImage,
                    lang = supplier.settings.lang,
                    txnAlertEnabled = supplier.settings.txnAlertEnabled,
                    state = if (supplier.blockedBySelf) 3 else 1,
                    address = supplier.address
                )
            }

            is RequestUpdateAccount.UpdateAccountState -> {
                supplier.createUpdateSupplierRequest(
                    id = accountId,
                    name = supplier.name,
                    mobile = supplier.mobile,
                    profileImage = supplier.profileImage,
                    lang = supplier.settings.lang,
                    txnAlertEnabled = supplier.settings.txnAlertEnabled,
                    state = if (request.blocked) 3 else 1,
                    address = supplier.address
                ).copy(
                    updateState = true
                )
            }

            is RequestUpdateAccount.UpdateLanguage -> {
                supplier.createUpdateSupplierRequest(
                    id = accountId,
                    name = supplier.name,
                    mobile = supplier.mobile,
                    profileImage = supplier.profileImage,
                    lang = request.lang,
                    txnAlertEnabled = supplier.settings.txnAlertEnabled,
                    state = if (supplier.blockedBySelf) 3 else 1,
                    address = supplier.address
                )
            }

            is RequestUpdateAccount.UpdateName -> {
                supplier.createUpdateSupplierRequest(
                    id = accountId,
                    name = request.desc,
                    mobile = supplier.mobile,
                    profileImage = supplier.profileImage,
                    lang = supplier.settings.lang,
                    txnAlertEnabled = supplier.settings.txnAlertEnabled,
                    state = if (supplier.blockedBySelf) 3 else 1,
                    address = supplier.address
                )
            }

            is RequestUpdateAccount.UpdateProfileImage -> {
                supplier.createUpdateSupplierRequest(
                    id = accountId,
                    name = supplier.name,
                    mobile = supplier.mobile,
                    profileImage = request.profileImage,
                    lang = supplier.settings.lang,
                    txnAlertEnabled = supplier.settings.txnAlertEnabled,
                    state = if (supplier.blockedBySelf) 3 else 1,
                    address = supplier.address
                )
            }

            is RequestUpdateAccount.UpdateTransactionAlertStatus -> {
                supplier.createUpdateSupplierRequest(
                    id = accountId,
                    name = supplier.name,
                    mobile = supplier.mobile,
                    profileImage = supplier.profileImage,
                    lang = supplier.settings.lang,
                    txnAlertEnabled = request.isEnable,
                    state = if (supplier.blockedBySelf) 3 else 1,
                    address = supplier.address
                ).copy(
                    updateTxnAlertEnabled = true
                )
            }

            is RequestUpdateAccount.UpdateTransactionAlertStatusAndLanguage -> {
                supplier.createUpdateSupplierRequest(
                    id = accountId,
                    name = supplier.name,
                    mobile = supplier.mobile,
                    profileImage = supplier.profileImage,
                    lang = request.lang,
                    txnAlertEnabled = request.isEnable,
                    state = if (supplier.blockedBySelf) 3 else 1,
                    address = supplier.address
                ).copy(
                    updateTxnAlertEnabled = true
                )
            }

            is RequestUpdateAccount.UpdateAddress -> {
                supplier.createUpdateSupplierRequest(
                    id = accountId,
                    name = supplier.name,
                    mobile = supplier.mobile,
                    profileImage = supplier.profileImage,
                    lang = supplier.settings.lang,
                    txnAlertEnabled = supplier.settings.txnAlertEnabled,
                    state = if (supplier.blockedBySelf) 3 else 1,
                    address = request.address
                )
            }

            else -> null
        }
    }

    private suspend fun createUpdateRequestForCustomer(
        accountId: String,
        request: RequestUpdateAccount
    ): UpdateCustomerRequest? {
        val customer = customerRepository.getCustomerDetails(accountId).first() ?: return null
        return when (request) {
            is RequestUpdateAccount.DeleteDueCustomDate -> {
                customer.createUpdateCustomerRequest().copy(
                    deleteDueCustomDate = true
                )
            }

            is RequestUpdateAccount.UpdateAccountState -> {
                customer.createUpdateCustomerRequest().copy(
                    state = if (request.blocked) 3 else 1,
                    updateState = true
                )
            }

            is RequestUpdateAccount.UpdateAddTransactionRestrictedStatus -> {
                customer.createUpdateCustomerRequest().copy(
                    addTransactionRestricted = request.isAddTransactionRestricted,
                    updateAddTransactionRestricted = true
                )
            }

            is RequestUpdateAccount.UpdateDueCustomDate -> {
                customer.createUpdateCustomerRequest().copy(
                    updateDueCustomDate = true,
                    dueCustomDate = request.dueCustomDate
                )
            }

            is RequestUpdateAccount.UpdateLanguage -> {
                customer.createUpdateCustomerRequest().copy(
                    lang = request.lang
                )
            }

            is RequestUpdateAccount.UpdateMobile -> {
                customer.createUpdateCustomerRequest().copy(mobile = request.mobile)
            }

            is RequestUpdateAccount.UpdateName -> {
                customer.createUpdateCustomerRequest().copy(mobile = request.desc)
            }

            is RequestUpdateAccount.UpdateProfileImage -> {
                customer.createUpdateCustomerRequest().copy(profileImage = request.profileImage)
            }

            is RequestUpdateAccount.UpdateReminderMode -> {
                customer.createUpdateCustomerRequest().copy(
                    reminderMode = request.reminderMode,
                )
            }

            is RequestUpdateAccount.UpdateTransactionAlertStatus -> {
                customer.createUpdateCustomerRequest().copy(
                    txnAlertEnabled = request.isEnable,
                    updateTxnAlertEnabled = true
                )
            }

            is RequestUpdateAccount.UpdateTransactionAlertStatusAndLanguage -> {
                customer.createUpdateCustomerRequest().copy(
                    txnAlertEnabled = request.isEnable,
                    updateTxnAlertEnabled = true,
                    lang = request.lang
                )
            }

            is RequestUpdateAccount.UpdateAddress -> {
                customer.createUpdateCustomerRequest().copy(
                    address = request.address
                )
            }
        }

    }
}