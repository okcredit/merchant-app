package app.okcredit.ledger.ui.customer.usecase

import app.okcredit.ledger.core.usecase.GetAccountStatementImpl
import app.okcredit.ledger.ui.model.MenuOptions
import app.okcredit.ledger.ui.model.ToolbarData
import app.okcredit.ledger.ui.utils.DateTimeUtils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import me.tatarka.inject.annotations.Inject
import okcredit.base.units.Timestamp
import okcredit.base.units.ZERO_TIMESTAMP

@Inject
class GetCustomerToolbarData(
    getAccountStatement: Lazy<GetAccountStatementImpl>
) {
    private val getAccountStatement by lazy { getAccountStatement.value }


    fun execute(customerId: String): Flow<ToolbarData> {
        return getToolbarOptions(customerId)
            .map { toolbarOptions ->
                ToolbarData(
                    moreMenuOptions = getMenuOptions(),
                    toolbarOptions = toolbarOptions
                )
            }

    }

    private fun getMenuOptions(): List<MenuOptions> {
        return listOf(
            MenuOptions.Call,
            MenuOptions.RelationshipStatements,
            MenuOptions.Help,
            MenuOptions.RemindWithSms,
            MenuOptions.RemindWithWhatsapp
        )
    }

    private fun getToolbarOptions(customerId: String): Flow<List<MenuOptions>> {
        val toolbarOptions = mutableListOf<MenuOptions>()
        return checkForEmptyLedger(customerId)
            .map { emptyLedger ->
                if (emptyLedger) {
                    toolbarOptions.add(MenuOptions.Help)
                    return@map toolbarOptions
                }
                toolbarOptions.add(MenuOptions.Call)
                toolbarOptions.add(MenuOptions.RelationshipStatements)
                return@map toolbarOptions
            }

    }

    private fun checkForEmptyLedger(customerId: String): Flow<Boolean> {
        return getAccountStatement.execute(
            accountId = customerId,
            startTime = ZERO_TIMESTAMP,
            endTime = Timestamp(DateTimeUtils.getCurrentTime().toEpochMilliseconds())
        ).map { it.isEmpty() }
    }
}