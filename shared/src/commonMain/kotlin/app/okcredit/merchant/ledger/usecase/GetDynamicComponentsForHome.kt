package app.okcredit.merchant.ledger.usecase

import app.okcredit.merchant.ledger.HomeContract
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import me.tatarka.inject.annotations.Inject
import tech.okcredit.analytics.AnalyticsProvider
import tech.okcredit.customization.models.Component
import tech.okcredit.customization.models.withDefaultProperties
import tech.okcredit.customization.usecase.GetCustomization

@Inject
class GetDynamicComponentsForHome(
    private val getCustomization: Lazy<GetCustomization>,
    private val analyticsProvider: Lazy<AnalyticsProvider>,
) {

    fun execute(): Flow<List<HomeContract.DynamicItem>> {
        return getCustomization.value.execute("home_banner")
            .map { list ->
                buildDynamicList(list)
            }
    }

    private fun buildDynamicList(components: List<Component>): List<HomeContract.DynamicItem> {
        val list = mutableListOf<HomeContract.DynamicItem>()
        components.forEach { component ->
            component.items?.forEach { item ->
                val action = item.eventHandlers?.click
                    ?.find { it.action == "navigate" }
                val actionTrack = item.eventHandlers?.click
                    ?.find { it.action == "track" }
                val dynamicItem = HomeContract.DynamicItem(
                    id = item.metadata?.name ?: "",
                    icon = item.icon!!,
                    title = item.title!!,
                    deeplink = (action)?.url!!,
                    subtitle = null,
                    trackOnItemClicked = {
                        actionTrack?.withDefaultProperties(
                            targetName = "home_banner",
                            component = item,
                        )?.let {
                            analyticsProvider.value.logProductEvent(
                                it.event!!,
                                it.properties,
                            )
                        }
                    },
                )
                list.add(dynamicItem)
            }
        }
        return list
    }
}
