package app.okcredit.merchant.search.usecase

import app.okcredit.ledger.contract.model.Account
import app.okcredit.ledger.contract.model.AccountType
import app.okcredit.ledger.contract.usecase.GetAccounts
import app.okcredit.ledger.contract.usecase.SortBy
import app.okcredit.merchant.ledger.HomeTab
import app.okcredit.merchant.search.HeaderType
import app.okcredit.merchant.search.HomeSearchItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import me.tatarka.inject.annotations.Inject

private fun String.containNameOrMobile(name: String, mobile: String?): Boolean {
    return name.contains(this, ignoreCase = true) || mobile?.contains(this, ignoreCase = true) == true
}

@Inject
class GetHomeSearchData(
    private val getAccounts: Lazy<GetAccounts>,
) {

    fun execute(query: String, source: HomeTab): Flow<HomeSearchResponse> {
        return combine(
            fetchCustomersForQuery(),
            fetchSuppliersForQuery(),
        ) { customers, suppliers ->
            HomeSearchResponse(
                query = query,
                list = buildSearchList(
                    query = query,
                    source = source,
                    customers = customers,
                    suppliers = suppliers,
                ),
            )
        }
    }

    private fun buildSearchList(
        query: String,
        source: HomeTab,
        customers: List<Account>,
        suppliers: List<Account>,
    ): List<HomeSearchItem> {
        val list = mutableListOf<HomeSearchItem>()
        val existingAccountMobileNumbers = mutableSetOf<String>()

        // Add customers and suppliers to search list based on source
        if (source == HomeTab.CUSTOMER_TAB) {
            addCustomersToSearchList(
                query = query,
                customers = customers,
                existingAccountMobiles = existingAccountMobileNumbers,
                list = list,
            )
            addSuppliersToSearchList(
                query = query,
                suppliers = suppliers,
                existingAccountMobileNumbers = existingAccountMobileNumbers,
                list = list,
            )
        } else {
            addSuppliersToSearchList(
                query = query,
                suppliers = suppliers,
                existingAccountMobileNumbers = existingAccountMobileNumbers,
                list = list,
            )
            addCustomersToSearchList(
                query = query,
                customers = customers,
                existingAccountMobiles = existingAccountMobileNumbers,
                list = list,
            )
        }

        if (list.isEmpty()) {
            addNoSearchResultCard(query, list)
        }

        return list
    }

    private fun addNoSearchResultCard(
        query: String,
        list: MutableList<HomeSearchItem>,
    ) {
        list.add(
            HomeSearchItem.NoUserFoundItem(
                searchQuery = query,
                addCustomerInProgress = false,
            ),
        )
    }

    private fun addCustomersToSearchList(
        query: String,
        customers: List<Account>,
        existingAccountMobiles: MutableSet<String>,
        list: MutableList<HomeSearchItem>,
    ) {
        if (customers.isEmpty()) return

        val currentSize = list.size
        customers.forEach {
            if (query.containNameOrMobile(it.name, it.mobile)) {
                it.mobile?.let { mobile ->
                    existingAccountMobiles.contains(mobile)
                }
                list.add(
                    HomeSearchItem.CustomerItem(
                        customerId = it.id,
                        name = it.name,
                        balance = it.balance,
                        profileImage = it.profileImage,
                        commonLedger = it.registered,
                    ),
                )
            }
        }
        // add header if we have added any customer to the list
        if (currentSize != list.size) {
            if (query.isNotEmpty()) {
                list.add(currentSize, HomeSearchItem.HeaderItem(HeaderType.CUSTOMER))
            }
        }
    }

    private fun addSuppliersToSearchList(
        query: String,
        suppliers: List<Account>,
        existingAccountMobileNumbers: MutableSet<String>,
        list: MutableList<HomeSearchItem>,
    ) {
        if (suppliers.isEmpty()) return

        val currentSize = list.size
        suppliers.forEach {
            if (query.containNameOrMobile(it.name, it.mobile)) {
                it.mobile?.let { mobile ->
                    existingAccountMobileNumbers.contains(mobile)
                }
                list.add(
                    HomeSearchItem.SupplierItem(
                        supplierId = it.id,
                        name = it.name,
                        balance = it.balance,
                        profileImage = it.profileImage,
                        commonLedger = it.registered,
                    ),
                )
            }
        }

        // add header if we have added any supplier to the list
        if (currentSize != list.size) {
            if (query.isNotEmpty()) {
                list.add(currentSize, HomeSearchItem.HeaderItem(HeaderType.SUPPLIER))
            }
        }
    }

    private fun fetchSuppliersForQuery(): Flow<List<Account>> {
        return getAccounts.value.execute(sortBy = SortBy.NAME, accountType = AccountType.SUPPLIER)
    }

    private fun fetchCustomersForQuery(): Flow<List<Account>> {
        return getAccounts.value.execute(sortBy = SortBy.NAME, accountType = AccountType.CUSTOMER)
    }
}

data class HomeSearchResponse(
    val query: String,
    val list: List<HomeSearchItem>,
)
