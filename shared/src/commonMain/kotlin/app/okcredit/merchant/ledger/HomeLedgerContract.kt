package app.okcredit.merchant.ledger

import androidx.compose.runtime.Immutable
import app.okcredit.merchant.ledger.usecase.CustomerForHomeResponse
import okcredit.base.ui.BaseViewEvent
import okcredit.base.ui.UiState
import okcredit.base.ui.UserIntent
import okcredit.base.units.Paisa
import app.okcredit.merchant.ledger.usecase.SupplierForHomeResponse
import okcredit.base.units.Timestamp
import tech.okcredit.identity.contract.model.Business

interface HomeContract {

    @Immutable
    data class State(
        val loading: Boolean = false,
        val homeSyncLoading: Boolean = false,
        val loadingCustomers: Boolean = true,
        val loadingSuppliers: Boolean = true,
        val activeBusiness: Business? = null,
        val toolbarAction: ToolbarAction? = null,
        val userAlert: UserAlert? = null,
        val dynamicItems: List<DynamicItem> = emptyList(),
        val showSortAndFilter: Boolean = true,
        val showSearch: Boolean = true,
        val primaryVpa: String? = null,
        val selectedCustomerSortOption: SortOption = SortOption.LAST_ACTIVITY,
        val selectedSupplierSortOption: SortOption = SortOption.LAST_ACTIVITY,
        val selectedCustomerReminderFilterOptions: Set<ReminderFilterOption> = emptySet(),
        val selectedTab: HomeTab = HomeTab.CUSTOMER_TAB,
        val showKachaNote: Boolean = false,
        val customers: List<HomeItem> = emptyList(),
        val suppliers: List<HomeItem> = emptyList(),
        val scrollListToTop: Boolean = false,
    ) : UiState {
        val sortOrFilterAppliedCount: Int
            get() = if (selectedTab.isCustomerTab()) {
                selectedCustomerReminderFilterOptions.size + if (selectedCustomerSortOption != SortOption.LAST_ACTIVITY) 1 else 0
            } else {
                if (selectedCustomerSortOption != SortOption.LAST_ACTIVITY) 1 else 0
            }

        val showAddRelationship: Boolean
            get() = if (selectedTab.isCustomerTab()) {
                if (customers.isEmpty()) {
                    sortOrFilterAppliedCount > 0
                } else {
                    true
                }
            } else {
                if (suppliers.isEmpty()) {
                    sortOrFilterAppliedCount > 0
                } else {
                    true
                }
            }
    }

    sealed class HomeItem {
        data class CustomerItem(
            val customerId: String,
            val profileImage: String?,
            val name: String,
            val balance: Paisa,
            val lastActivityMetaInfo: Int,
            val lastActivity: Timestamp,
            val dueDate: Timestamp? = null,
            val lastAmount: Paisa? = null,
            val commonLedger: Boolean = false,
            val isDefaulter: Boolean = false,
        ) : HomeItem()

        data class SummaryItem(
            val homeTab: HomeTab,
            val netBalance: Paisa,
            val totalAccounts: Int,
            val showDefaulterReminders: Boolean = false,
            val statusPending: Boolean = false,
            val defaulterCount: Int = 0,
        ) : HomeItem()

        data class SupplierItem(
            val supplierId: String,
            val name: String,
            val profileImage: String?,
            val subtitle: String?,
            val subtitleIconType: SubtitleIconType?,
            val balance: Paisa,
            val commonLedger: Boolean = false,
        ) : HomeItem()
    }

    enum class ToolbarAction {
        ACTIVATE_UPI,
        NEED_HELP,
        SHARE,
    }

    sealed class UserAlert {
        data object UnSyncedTransactions : UserAlert()
    }

    data class DynamicItem(
        val id: String,
        val icon: String,
        val title: String,
        val subtitle: String?,
        val deeplink: String,
        val trackOnItemClicked: () -> Unit = {},
    )

    sealed class PartialState : UiState.Partial {
        data class SetLoading(val loading: Boolean) : PartialState()
        data object NoChange : PartialState()
        data class SetScrollToTop(val shouldScroll: Boolean) : PartialState()
        data class SetActiveBusiness(val activeBusiness: Business) : PartialState()
        data class SetCustomersForHome(val customers: CustomerForHomeResponse) : PartialState()
        data class SetSuppliersForHome(val suppliers: SupplierForHomeResponse) : PartialState()
        data class SetDynamicComponentsForHome(val dynamicItems: List<DynamicItem>) : PartialState()
        data class SetToolbarAction(val toolbarAction: ToolbarAction?) : PartialState()
        data class SetUserAlert(val userAlert: UserAlert?) : PartialState()
        data class SetKachhaChittaEnabled(val enabled: Boolean) : PartialState()
        data class SetSelectedTab(val selectedTab: HomeTab) : PartialState()
        data class SetPrimaryVpa(val primaryVpa: String?) : PartialState()
        data class SetHomeSyncLoading(val loading: Boolean) : PartialState()
    }

    sealed class Intent : UserIntent {
        data object OnPullToRefresh : Intent()

        data object OnScrollToTop : Intent()

        data class OnTabChanged(val tab: HomeTab) : Intent()

        data class LoadCustomersWithFilter(
            val sortBy: SortOption? = null,
            val reminderFilters: Set<ReminderFilterOption> = emptySet()
        ) : Intent()

        data class LoadSuppliersWithFilter(val sortBy: SortOption) : Intent()

        data class OnCustomerClicked(val customerId: String) : Intent()

        data class OnSupplierClicked(val supplierId: String) : Intent()

        data object LoadAutoReminderSummary : Intent()

        data object OnAutoReminderSummaryShown : Intent()

        data object OnRetrySyncTransactionsClicked : Intent()
    }

    sealed class ViewEvent : BaseViewEvent {
        data object LaunchAskSmsPermissionForAutoReminder : ViewEvent()

        data class ShowError(val errorRes: String) : ViewEvent()
        data class ShowAutoReminderSummarySnackBar(
            val message: String,
            val customerCount: Int
        ) : ViewEvent()

        data class GoToCustomerLedgerScreen(val customerId: String) : ViewEvent()

        data class GoToSupplierLedgerScreen(val supplierId: String) : ViewEvent()
    }
}

enum class HomeTab {
    CUSTOMER_TAB,
    SUPPLIER_TAB
}

fun HomeTab.isCustomerTab() = this == HomeTab.CUSTOMER_TAB

fun HomeTab.isSupplierTab() = this == HomeTab.SUPPLIER_TAB


enum class CategoryOption {
    SORT_BY,
    REMINDER_DATE
}

enum class SortOption {
    LAST_ACTIVITY,
    LAST_PAYMENT,
    AMOUNT_DUE,
    NAME
}

enum class ReminderFilterOption {
    TODAY,
    OVERDUE,
    UPCOMING
}

enum class SubtitleType {
    CUSTOMER_ADDED,
    DUE_TODAY,
    DUE_DATE_PASSED,
    DUE_DATE_INCOMING,
    TRANSACTION_SYNC_DONE,
    TRANSACTION_SYNC_PENDING,
    NONE,
    ERROR,
}

enum class SubtitleIconType {
    USER,
    NONE,
    ERROR,
    TRANSACTION_SYNC_PENDING,
    TRANSACTION_SYNC_DONE,
    TRANSACTION_DELIVERED,
    TRANSACTION_SEEN,
}
