package app.okcredit.merchant.ledger.usecase

import androidx.compose.ui.text.AnnotatedString
import app.okcredit.ledger.contract.model.AccountType
import app.okcredit.ledger.contract.model.Customer
import app.okcredit.ledger.contract.usecase.GetAccounts
import app.okcredit.ledger.contract.usecase.SortBy
import app.okcredit.merchant.ledger.HomeContract
import app.okcredit.merchant.ledger.HomeTab
import app.okcredit.merchant.ledger.ReminderFilterOption
import app.okcredit.merchant.ledger.SortOption
import app.okcredit.merchant.ledger.SortOption.*
import app.okcredit.merchant.ledger.SubtitleType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.TimeZone
import kotlinx.datetime.daysUntil
import kotlinx.datetime.minus
import me.tatarka.inject.annotations.Inject
import merchant_app.shared.generated.resources.Res
import merchant_app.shared.generated.resources.today
import merchant_app.shared.generated.resources.yesterday
import okcredit.base.appDispatchers
import okcredit.base.units.instant
import okcredit.base.units.paisa
import org.jetbrains.compose.resources.getString

@Inject
class GetCustomersForHome constructor(
    private val getAccounts: GetAccounts,
) {

    fun execute(
        reminderFilters: Set<ReminderFilterOption>,
        sortBy: SortOption? = null,
    ): Flow<CustomerForHomeResponse> {
        println("GetCustomersForHome: execute: reminderFilters: $reminderFilters, sortBy: $sortBy")
        return getAccounts.execute(sortBy = SortBy.LAST_ACTIVITY, accountType = AccountType.CUSTOMER)
            .map {
                println("GetCustomersForHome: execute: it: $it")
                val list = buildCustomerList(
                    legacyCustomers = it as List<Customer>,
                    defaulters = emptySet(),
                    sortBy = LAST_ACTIVITY,
                    reminderFilters = reminderFilters,
                )
                CustomerForHomeResponse(list, LAST_ACTIVITY, reminderFilters)
            }
    }

    private fun checkCustomerSortOrder(sortBy: SortOption?): SortBy {
        if (sortBy != null) {
            return when (sortBy) {
                LAST_ACTIVITY -> SortBy.LAST_ACTIVITY
                LAST_PAYMENT -> SortBy.LAST_PAYMENT
                AMOUNT_DUE -> SortBy.BALANCE_DUE
                NAME -> SortBy.NAME
            }
        }

        return sortBy ?: SortBy.LAST_ACTIVITY
    }

    private suspend fun buildCustomerList(
        legacyCustomers: List<Customer>,
        defaulters: Set<String>,
        sortBy: SortOption,
        reminderFilters: Set<ReminderFilterOption>,
    ): List<HomeContract.HomeItem> {
        return withContext(appDispatchers.io) {
            val list = mutableListOf<HomeContract.HomeItem>()
            var netBalance = 0L.paisa
            var counter = 0
            applySortAndFilters(
                originalList = legacyCustomers,
                sortBy = sortBy,
                reminderFilters = reminderFilters
            ).forEach { customer ->
                val subTitlePair = findSubTitleAndType(customer)
                val customerItem = HomeContract.HomeItem.CustomerItem(
                    customerId = customer.id,
                    profileImage = customer.profileImage,
                    name = customer.name,
                    balance = customer.balance,
                    commonLedger = customer.registered,
                    subtitle = subTitlePair.first,
                    type = subTitlePair.second,
                    isDefaulter = defaulters.contains(customer.mobile),
                )
                netBalance += customer.balance
                counter++
                list.add(customerItem)
            }

            if (list.size > 0) {
                list.add(
                    0,
                    HomeContract.HomeItem.SummaryItem(
                        homeTab = HomeTab.CUSTOMER_TAB,
                        netBalance = netBalance,
                        totalAccounts = counter,
                    )
                )
            }

            list
        }
    }

    private fun applySortAndFilters(
        originalList: List<Customer>,
        sortBy: SortOption,
        reminderFilters: Set<ReminderFilterOption>
    ): List<Customer> {
        val sortedAndFilterList = mutableListOf<Customer>()

        if (reminderFilters.isEmpty()) {
            // if no filter is applied and sort by is last activity then show default list
            // which contains customers with due today, customers with due crossed and remaining
            // customers in that order
            if (sortBy == LAST_ACTIVITY) {
                sortedAndFilterList.addAll((originalList))
                return sortedAndFilterList
            } else {
                sortedAndFilterList.addAll(originalList)
            }
        } else {
            val customersWithDueToday = mutableListOf<Customer>()
            val customersWithDueCrossed = mutableListOf<Customer>()
            val customersWithUpcomingDue = mutableListOf<Customer>()
            originalList.forEach { customer ->
                if (customer.dueDate != null) {
                    val diff =
                        Clock.System.now().daysUntil(customer.dueDate!!.instant, TimeZone.UTC)
                    when {
                        diff == 0 -> {
                            customersWithDueToday.add(customer)
                        }

                        diff < 0 -> {
                            customersWithDueCrossed.add(customer)
                        }

                        diff > 0 -> {
                            customersWithUpcomingDue.add(customer)
                        }
                    }
                }
            }

            for (filter in reminderFilters) {
                when (filter) {
                    ReminderFilterOption.TODAY -> {
                        sortedAndFilterList.addAll(customersWithDueToday)
                    }

                    ReminderFilterOption.OVERDUE -> {
                        sortedAndFilterList.addAll(customersWithDueCrossed)
                    }

                    ReminderFilterOption.UPCOMING -> {
                        sortedAndFilterList.addAll(customersWithUpcomingDue)
                    }
                }
            }
        }

        return sortedAndFilterList
    }

    private suspend fun findSubTitleAndType(
        customer: Customer,
    ): Pair<AnnotatedString, SubtitleType> {
        val subtitleType: SubtitleType
        val subTitle = AnnotatedString.Builder("")

        if (customer.summary.lastActivity != customer.createdAt) {
            val currentTime = Clock.System.now()
            currentTime.minus(24, DateTimeUnit.HOUR)
            subTitle.append(
                when {
                    currentTime.compareTo(customer.summary.lastActivity.instant) == 0 -> {
                        getString(Res.string.today)
                    }

                    currentTime.minus(24, DateTimeUnit.HOUR)
                        .compareTo(customer.summary.lastActivity.instant) == 0 -> {
                        getString(Res.string.yesterday)
                    }

                    else -> {
                        ""
                    }
                }
            )

            subtitleType = SubtitleType.TRANSACTION_SYNC_DONE
        } else {
            subTitle.append(
                customer.createdAt.instant.toString()
            )
            subtitleType = SubtitleType.CUSTOMER_ADDED
        }
        return subTitle.toAnnotatedString() to subtitleType
    }
}

data class CustomerForHomeResponse(
    val items: List<HomeContract.HomeItem>,
    val sortOption: SortOption,
    val reminderFilters: Set<ReminderFilterOption>,
)

const val DATE_TYPE_TODAY_OR_YESTERDAY = 3
const val DATE_TYPE_DATE = 1
