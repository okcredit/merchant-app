package app.okcredit.merchant.search

import app.okcredit.ledger.contract.model.AccountType
import app.okcredit.merchant.ledger.HomeTab
import app.okcredit.merchant.search.HomeSearchContract.Intent
import app.okcredit.merchant.search.HomeSearchContract.PartialState
import app.okcredit.merchant.search.HomeSearchContract.State
import app.okcredit.merchant.search.HomeSearchContract.ViewEvent
import app.okcredit.merchant.search.usecase.AddCustomerOrSupplierFromSearch
import app.okcredit.merchant.search.usecase.GetHomeSearchData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import me.tatarka.inject.annotations.Inject
import okcredit.base.ui.BaseCoroutineScreenModel
import okcredit.base.ui.Result

@Inject
class HomeSearchScreenModel(
    private val getHomeSearchData: Lazy<GetHomeSearchData>,
    private val addCustomerOrSupplierFromSearch: Lazy<AddCustomerOrSupplierFromSearch>,
) : BaseCoroutineScreenModel<State, PartialState, ViewEvent, Intent>(State()) {

    override fun partialStates(): Flow<PartialState> {
        return merge(
            loadInitialData(),
            observeSearchQueryChanged(),
            observeAddAccountFromContact(),
            observeAddAccountFromSearchQuery(),
        )
    }

    private fun loadInitialData() = drip {
        pushIntent(Intent.SearchQueryChanged(""))
    }.dropAll()

    private fun observeSearchQueryChanged() = intent<Intent.SearchQueryChanged>()
        .flatMapLatest {
            wrap(getHomeSearchData.value.execute(it.query, currentState.source))
        }.map { result ->
            when (result) {
                is Result.Failure -> PartialState.SetLoading(false)
                is Result.Progress -> PartialState.SetLoading(false)
                is Result.Success -> PartialState.SetHomeSearchData(result.value)
            }
        }

    private fun observeAddAccountFromContact() = intent<Intent.AddAccountFromContact>()
        .flatMapLatest { intent ->
            wrap {
                val contact = currentState.searchList.find {
                    it is HomeSearchItem.ContactItem && intent.contactId == it.contactId
                } as HomeSearchItem.ContactItem?
                if (contact != null) {
                    addCustomerOrSupplierFromSearch.value.execute(
                        name = contact.name,
                        mobile = contact.phoneNumber,
                        accountType = if (currentState.source == HomeTab.CUSTOMER_TAB) AccountType.CUSTOMER else AccountType.SUPPLIER,
                    )
                } else {
                    throw IllegalArgumentException("Contact not found")
                }
            }
        }.map {
            when (it) {
                is Result.Success -> {
                    val (accountId, isCustomer) = it.value
                    if (isCustomer) {
                        emitViewEvent(ViewEvent.MoveToCustomerLedger(accountId))
                    } else {
                        emitViewEvent(ViewEvent.MoveToSupplierLedger(accountId))
                    }
                    PartialState.SetAddAccountInProgress(false)
                }

                is Result.Failure -> {
                    PartialState.SetAddAccountInProgress(false)
                }

                is Result.Progress -> {
                    PartialState.SetAddAccountInProgress(true)
                }
            }
        }

    private fun observeAddAccountFromSearchQuery() = intent<Intent.AddAccountFromSearchQuery>()
        .flatMapLatest { intent ->
            wrap {
                addCustomerOrSupplierFromSearch.value.execute(
                    name = intent.query,
                    mobile = null,
                    accountType = if (currentState.source == HomeTab.CUSTOMER_TAB) AccountType.CUSTOMER else AccountType.SUPPLIER,
                )
            }
        }.map {
            when (it) {
                is Result.Success -> {
                    val (accountId, isCustomer) = it.value
                    if (isCustomer) {
                        emitViewEvent(ViewEvent.MoveToCustomerLedger(accountId))
                    } else {
                        emitViewEvent(ViewEvent.MoveToSupplierLedger(accountId))
                    }
                    PartialState.SetAddAccountInProgress(false)
                }

                is Result.Failure -> {
                    PartialState.SetAddAccountInProgress(false)
                }

                is Result.Progress -> {
                    PartialState.SetAddAccountInProgress(true)
                }
            }
        }

    override fun reduce(currentState: State, partialState: PartialState): State {
        return when (partialState) {
            is PartialState.SetLoading -> currentState.copy(loading = partialState.loading)

            is PartialState.SetHomeSearchData -> currentState.copy(
                loading = false,
                searchQuery = partialState.searchResponse.query,
                searchList = partialState.searchResponse.list,
            )

            is PartialState.SetAddAccountInProgress -> currentState.copy(
                addAccountInProgress = partialState.progress,
            )
        }
    }
}
