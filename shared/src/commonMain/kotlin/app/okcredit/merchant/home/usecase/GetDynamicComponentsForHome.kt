package app.okcredit.merchant.home.usecase

import app.okcredit.merchant.home.HomeContract
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import me.tatarka.inject.annotations.Inject
import tech.okcredit.analytics.AnalyticsProvider
import tech.okcredit.customization.local.CustomizationLocalSource

@Inject
class GetDynamicComponentsForHome(
    private val getCustomization: Lazy<CustomizationLocalSource>,
    private val analyticsProvider: Lazy<AnalyticsProvider>,
) {

    fun execute(): Flow<List<HomeContract.DynamicItem>> {
        return getCustomization.value.listComponentsForTarget("HOME_BANNER")
            .map { optional ->
                buildDynamicList()
            }
    }

    private fun buildDynamicList(): List<HomeContract.DynamicItem> {
        val list = mutableListOf<HomeContract.DynamicItem>()
        return list
    }
}
