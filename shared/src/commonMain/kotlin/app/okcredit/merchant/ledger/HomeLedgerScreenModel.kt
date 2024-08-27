package app.okcredit.merchant.ledger

import app.okcredit.merchant.ledger.HomeContract.Intent
import app.okcredit.merchant.ledger.HomeContract.PartialState
import app.okcredit.merchant.ledger.HomeContract.State
import app.okcredit.merchant.ledger.HomeContract.ViewEvent
import app.okcredit.merchant.ledger.usecase.GetCustomersForHome
import app.okcredit.merchant.usecase.HomeDataSyncer
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import me.tatarka.inject.annotations.Inject
import okcredit.base.ui.BaseCoroutineScreenModel
import okcredit.base.ui.Result
import app.okcredit.merchant.ledger.usecase.GetDynamicComponentsForHome
import app.okcredit.merchant.ledger.usecase.GetSuppliersForHome
import app.okcredit.merchant.ledger.usecase.GetToolbarActionForHome
import app.okcredit.merchant.ledger.usecase.GetUserAlertForHome
import tech.okcredit.identity.contract.usecase.GetActiveBusiness

@Inject
class HomeLedgerScreenModel(
    private val homeDataSyncer: HomeDataSyncer,
    private val getCustomersForHome: GetCustomersForHome,
    private val getSuppliersForHome: GetSuppliersForHome,
    private val getDynamicComponentsForHome: GetDynamicComponentsForHome,
    private val getToolbarActionForHome: GetToolbarActionForHome,
    private val getUserAlertForHome: GetUserAlertForHome,
    private val getActiveBusiness: GetActiveBusiness,
) : BaseCoroutineScreenModel<State, PartialState, ViewEvent, Intent>(initialState = State()) {

    override fun partialStates(): Flow<PartialState> {
        return merge(
            syncHomeData(),
            loadInitialData(),
            loadActiveBusiness(),
            loadCustomersForHome(),
            loadSuppliersForHome(),
            loadDynamicComponentsForHome(),
            loadToolbarAction(),
            loadUserAlert(),
            observeOnTabChanged(),
            observeOnScrollToTop(),
            observeOnCustomerClicked(),
            observeOnSupplierClicked(),
        )
    }

    private fun observeOnSupplierClicked() = intent<Intent.OnSupplierClicked>()
        .map {
            emitViewEvent(ViewEvent.GoToSupplierLedgerScreen(it.supplierId))
            PartialState.NoChange
        }

    private fun observeOnCustomerClicked() = intent<Intent.OnCustomerClicked>()
        .map {
            emitViewEvent(ViewEvent.GoToCustomerLedgerScreen(it.customerId))
            PartialState.NoChange
        }

    private fun loadInitialData() = drip {
        pushIntent(Intent.LoadCustomersWithFilter())
        pushIntent(Intent.LoadSuppliersWithFilter(SortOption.LAST_ACTIVITY))
    }.dropAll()

    private fun loadUserAlert() = wrap(getUserAlertForHome.execute())
        .map {
            when (it) {
                is Result.Failure -> PartialState.NoChange
                is Result.Progress -> PartialState.NoChange
                is Result.Success -> PartialState.SetUserAlert(it.value)
            }
        }

    private fun loadToolbarAction() = wrap {
        getToolbarActionForHome.execute()
    }.map {
        when (it) {
            is Result.Failure -> PartialState.NoChange
            is Result.Progress -> PartialState.NoChange
            is Result.Success -> PartialState.SetToolbarAction(it.value)
        }
    }

    private fun loadActiveBusiness() = wrap(getActiveBusiness.execute())
        .map {
            when (it) {
                is Result.Failure -> PartialState.NoChange
                is Result.Progress -> PartialState.NoChange
                is Result.Success -> PartialState.SetActiveBusiness(it.value)
            }
        }

    private fun loadCustomersForHome() = intent<Intent.LoadCustomersWithFilter>()
        .flatMapLatest {
            wrap(
                getCustomersForHome.execute(
                    reminderFilters = it.reminderFilters,
                    sortBy = it.sortBy
                )
            )
        }
        .map {
            when (it) {
                is Result.Failure -> PartialState.NoChange
                is Result.Progress -> PartialState.NoChange
                is Result.Success -> {
                    if (currentState.customers.isEmpty() && it.value.items.isNotEmpty()) {
                        pushIntent(Intent.LoadAutoReminderSummary)
                    }
                    PartialState.SetCustomersForHome(it.value)
                }
            }
        }

    private fun loadSuppliersForHome() = intent<Intent.LoadSuppliersWithFilter>()
        .flatMapLatest { wrap(getSuppliersForHome.execute(it.sortBy)) }
        .map {
            when (it) {
                is Result.Failure -> PartialState.NoChange
                is Result.Progress -> PartialState.NoChange
                is Result.Success -> PartialState.SetSuppliersForHome(it.value)
            }
        }

    private fun loadDynamicComponentsForHome() = wrap(getDynamicComponentsForHome.execute())
        .map {
            when (it) {
                is Result.Failure -> PartialState.NoChange
                is Result.Progress -> PartialState.NoChange
                is Result.Success -> PartialState.SetDynamicComponentsForHome(it.value)
            }
        }

    private fun observeOnTabChanged() = intent<Intent.OnTabChanged>()
        .map {
            PartialState.SetSelectedTab(it.tab)
        }

    private fun observeOnScrollToTop() = intent<Intent.OnScrollToTop>()
        .map {
            PartialState.SetScrollToTop(true)
        }

    private fun syncHomeData() = wrap {
        homeDataSyncer.execute()
    }.dropAll()

    override fun reduce(currentState: State, partialState: PartialState): State {
        return when (partialState) {
            PartialState.NoChange -> currentState
            is PartialState.SetActiveBusiness -> currentState.copy(activeBusiness = partialState.activeBusiness)
            is PartialState.NoChange -> currentState
            is PartialState.SetLoading -> currentState.copy(loading = partialState.loading)
            is PartialState.SetCustomersForHome -> currentState.copy(
                customers = partialState.customers.items,
                selectedCustomerReminderFilterOptions = partialState.customers.reminderFilters,
                selectedCustomerSortOption = partialState.customers.sortOption,
                loadingCustomers = false
            )

            is PartialState.SetSuppliersForHome -> currentState.copy(
                suppliers = partialState.suppliers.items,
                selectedSupplierSortOption = partialState.suppliers.sortOption,
                loadingSuppliers = false
            )

            is PartialState.SetDynamicComponentsForHome -> currentState.copy(
                dynamicItems = partialState.dynamicItems,
                loading = false,
            )

            is PartialState.SetSelectedTab -> currentState.copy(
                selectedTab = partialState.selectedTab
            )

            is PartialState.SetPrimaryVpa -> currentState.copy(
                primaryVpa = partialState.primaryVpa
            )

            is PartialState.SetToolbarAction -> currentState.copy(
                toolbarAction = partialState.toolbarAction
            )

            is PartialState.SetUserAlert -> currentState.copy(
                userAlert = partialState.userAlert
            )

            is PartialState.SetKachhaChittaEnabled -> currentState.copy(
                showKachaNote = partialState.enabled
            )

            is PartialState.SetHomeSyncLoading -> currentState.copy(
                homeSyncLoading = partialState.loading
            )

            is PartialState.SetScrollToTop -> currentState.copy(
                scrollListToTop = partialState.shouldScroll
            )
        }
    }
}
