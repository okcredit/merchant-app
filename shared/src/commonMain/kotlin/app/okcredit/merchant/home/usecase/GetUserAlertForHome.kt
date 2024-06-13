package app.okcredit.merchant.home.usecase

import app.okcredit.merchant.home.HomeContract
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import me.tatarka.inject.annotations.Inject

@Inject
class GetUserAlertForHome(
) {
    // TODO: Handle unsync transaction case here
    fun execute(): Flow<HomeContract.UserAlert?> {
        return flowOf(null)
    }
}
