package app.okcredit.ledger.ui.supplier.usecase

import app.okcredit.ledger.core.SupplierRepository
import app.okcredit.ledger.ui.model.MenuOptions
import kotlinx.coroutines.flow.map
import me.tatarka.inject.annotations.Inject
import okcredit.base.units.Paisa


@Inject
class GetSupplierDetails(
    supplierRepository: Lazy<SupplierRepository>
) {

    private val repository by lazy { supplierRepository.value }

    fun execute(supplierId: String) = repository.getSupplierDetails(supplierId)
        .map {
            if (it == null) {
                return@map null
            }
            SupplierModel(
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
                toolbarOptions = getToolbarOptions(it.mobile)
            )
        }

    private fun getToolbarOptions(mobile: String?): List<MenuOptions> {
        return mutableListOf<MenuOptions>().apply {
            if (!mobile.isNullOrEmpty()) {
                add(MenuOptions.Call)
            }
            add(MenuOptions.Help)
        }
    }

    data class SupplierModel(
        val id: String,
        val name: String,
        val mobile: String?,
        val profileImage: String?,
        val balance: Paisa,
        val formattedDueDate: String,
        val blockedBySupplier: Boolean,
        val blocked: Boolean,
        val reminderMode: String,
        val registered: Boolean,
        val toolbarOptions: List<MenuOptions>,
    )
}

