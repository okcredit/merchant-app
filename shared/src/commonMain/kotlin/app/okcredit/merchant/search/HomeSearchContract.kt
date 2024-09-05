package app.okcredit.merchant.search

import androidx.compose.runtime.Immutable
import app.okcredit.merchant.ledger.HomeTab
import app.okcredit.merchant.search.usecase.HomeSearchResponse
import okcredit.base.ui.BaseViewEvent
import okcredit.base.ui.UiState
import okcredit.base.ui.UserIntent
import okcredit.base.units.Paisa

interface HomeSearchContract {

    @Immutable
    data class State(
        val loading: Boolean = false,
        val accountSelection: Boolean = false,
        val source: HomeTab = HomeTab.CUSTOMER_TAB,
        val searchQuery: String = "",
        val searchList: List<HomeSearchItem> = emptyList(),
        val addAccountInProgress: Boolean = false,
    ) : UiState

    sealed interface PartialState : UiState.Partial {
        data class SetLoading(val loading: Boolean) : PartialState
        data class SetHomeSearchData(val searchResponse: HomeSearchResponse) : PartialState
        data class SetAddAccountInProgress(val progress: Boolean) : PartialState
    }

    sealed interface Intent : UserIntent {
        data class SearchQueryChanged(val query: String) : Intent
        data class SendReminderToCustomer(val customerId: String) : Intent
        data class AddAccountFromContact(val contactId: String) : Intent
        data class AddAccountFromSearchQuery(val query: String) : Intent
    }

    sealed interface ViewEvent : BaseViewEvent {
        data class MoveToCustomerLedger(val accountId: String) : ViewEvent
        data class MoveToSupplierLedger(val accountId: String) : ViewEvent
    }
}

sealed interface HomeSearchItem {

    data class HeaderItem(
        val type: HeaderType,
    ) : HomeSearchItem

    data class CustomerItem(
        val customerId: String,
        val name: String,
        val balance: Paisa,
        val profileImage: String?,
        val commonLedger: Boolean = false,
        val isDefaulter: Boolean = false,
    ) : HomeSearchItem

    data class SupplierItem(
        val supplierId: String,
        val name: String,
        val balance: Paisa,
        val profileImage: String?,
        val commonLedger: Boolean = false,
    ) : HomeSearchItem

    data class ContactItem(
        val contactId: String,
        val name: String,
        val phoneNumber: String,
        val profileImage: String?,
    ) : HomeSearchItem

    data class NoUserFoundItem(
        val searchQuery: String,
        val addCustomerInProgress: Boolean,
    ) : HomeSearchItem
}

enum class HeaderType {
    CUSTOMER,
    SUPPLIER,
    CONTACT,
    RECENT_SEARCH,
}
