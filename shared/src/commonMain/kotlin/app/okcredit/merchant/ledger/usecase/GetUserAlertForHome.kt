package app.okcredit.merchant.ledger.usecase

import app.okcredit.merchant.ledger.HomeContract
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import me.tatarka.inject.annotations.Inject

@Inject
class GetUserAlertForHome {
    // TODO: Handle unsync transaction case here
    fun execute(): Flow<HomeContract.UserAlert?> {
        return flowOf(null)
    }
}
