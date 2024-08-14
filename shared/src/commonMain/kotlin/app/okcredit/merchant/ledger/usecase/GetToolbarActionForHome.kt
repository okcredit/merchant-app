package app.okcredit.merchant.ledger.usecase

import app.okcredit.merchant.ledger.HomeContract
import kotlinx.coroutines.flow.first
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.daysUntil
import me.tatarka.inject.annotations.Inject
import okcredit.base.units.instant
import okcredit.base.units.timestamp
import tech.okcredit.identity.contract.usecase.GetIndividual

@Inject
class GetToolbarActionForHome(
    private val getIndividual: Lazy<GetIndividual>,
) {

    suspend fun execute(): HomeContract.ToolbarAction {
        val userRegisterTime = getIndividual.value.execute().first().registerTime ?: 0L
        val currentDay = Clock.System.now()
        val diffInDays = userRegisterTime.timestamp.instant.daysUntil(currentDay, TimeZone.currentSystemDefault())
        return if (diffInDays <= 15) {
            HomeContract.ToolbarAction.NEED_HELP
        } else {
            HomeContract.ToolbarAction.SHARE
        }
    }
}
