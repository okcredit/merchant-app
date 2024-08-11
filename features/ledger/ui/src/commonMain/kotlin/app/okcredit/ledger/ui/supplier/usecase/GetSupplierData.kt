package app.okcredit.ledger.ui.supplier.usecase

import app.okcredit.ledger.core.SupplierRepository
import app.okcredit.ledger.ui.model.MenuOptions
import app.okcredit.ledger.ui.model.ToolbarData
import app.okcredit.ledger.ui.supplier.SupplierLedgerContract.SupplierDetails
import kotlinx.coroutines.flow.map
import me.tatarka.inject.annotations.Inject


@Inject
class GetSupplierData(
    supplierRepository: Lazy<SupplierRepository>
) {

    private val repository by lazy { supplierRepository.value }

    fun execute(supplierId: String) = repository.getSupplierDetails(supplierId)
        .map {
            if (it == null) {
                return@map null
            }
            Response(
                supplier = SupplierDetails(
                    id = it.id,
                    name = it.name,
                    mobile = it.mobile,
                    profileImage = it.profileImage,
                    balance = it.balance,
                    formattedDueDate = "",
                    blockedBySupplier = it.settings.blockedBySupplier,
                    blocked = it.settings.addTransactionRestricted,
                    reminderMode = "whatsapp",
                    registered = it.registered,
                ),
                toolbarData = getToolbarOptions(it.mobile)
            )
        }

    private fun getToolbarOptions(mobile: String?): ToolbarData {
        val toolbarOptions = mutableListOf<MenuOptions>().apply {
            if (!mobile.isNullOrEmpty()) {
                add(MenuOptions.Call)
            }
            add(MenuOptions.Help)
        }
        return ToolbarData(
            toolbarOptions = toolbarOptions,
            moreMenuOptions = emptyList()
        )
    }

    data class Response(
        val supplier: SupplierDetails,
        val toolbarData: ToolbarData,
    )
}

