package app.okcredit.merchant.ledger.usecase

import app.okcredit.ledger.contract.model.AccountType
import app.okcredit.ledger.contract.model.Supplier
import app.okcredit.ledger.contract.usecase.GetAccounts
import app.okcredit.ledger.contract.usecase.SortBy
import app.okcredit.merchant.ledger.HomeContract
import app.okcredit.merchant.ledger.HomeTab
import app.okcredit.merchant.ledger.SortOption
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import me.tatarka.inject.annotations.Inject
import okcredit.base.units.paisa

@Inject
class GetSuppliersForHome(
    private val getAccountsLazy: Lazy<GetAccounts>,
) {

    fun execute(sortOption: SortOption): Flow<SupplierForHomeResponse> {
        return getAccountsLazy.value.execute(sortBy = SortBy.LAST_ACTIVITY, accountType = AccountType.SUPPLIER)
            .map { suppliers ->
                SupplierForHomeResponse(
                    items = buildSupplierList(
                        suppliers = suppliers as List<Supplier>,
                        sortOption = sortOption,
                    ),
                    sortOption = sortOption,
                )
            }
    }

    private fun buildSupplierList(
        suppliers: List<Supplier>,
        sortOption: SortOption,
    ): List<HomeContract.HomeItem> {
        val list = mutableListOf<HomeContract.HomeItem>()
        var netBalance = 0L.paisa
        var counter = 0
        // apply sort option and build list
        applySortOption(suppliers, sortOption).forEach { supplier ->
            val item = HomeContract.HomeItem.SupplierItem(
                supplierId = supplier.id,
                profileImage = supplier.profileImage,
                name = supplier.name,
                balance = supplier.balance,
                lastActivity = supplier.summary.lastActivity,
                lastAmount = supplier.summary.lastAmount,
                lastActivityMetaInfo = supplier.summary.lastActivityMetaInfo,
                commonLedger = supplier.registered,
            )
            list.add(item)
            netBalance += supplier.balance
            counter++
        }

        if (list.size > 0) {
            // add summary item only if there are suppliers
            list.add(
                0,
                HomeContract.HomeItem.SummaryItem(
                    homeTab = HomeTab.SUPPLIER_TAB,
                    netBalance = netBalance,
                    totalAccounts = counter,
                ),
            )
        }
        return list
    }

    private fun applySortOption(suppliers: List<Supplier>, sortOption: SortOption): List<Supplier> {
        return suppliers
    }
}

data class SupplierForHomeResponse(
    val items: List<HomeContract.HomeItem>,
    val sortOption: SortOption,
)
