package app.okcredit.merchant.home.usecase

import app.okcredit.merchant.home.MoreOption
import app.okcredit.merchant.home.MoreOptionItem
import app.okcredit.ui.Res
import app.okcredit.ui.icon_account
import app.okcredit.ui.icon_namsthe
import app.okcredit.ui.icon_settings
import app.okcredit.ui.icon_statement
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import me.tatarka.inject.annotations.Inject
import tech.okcredit.customization.models.Component
import tech.okcredit.customization.usecase.GetCustomization

@Inject
class GetHomeMoreOptionItems(
    private val getCustomization: GetCustomization,
) {

    fun execute() = getCustomization.execute("home_side_navigation")
        .distinctUntilChanged()
        .map {
            getOptionsForBusiness(customization = it)
        }

    private fun getOptionsForBusiness(
        customization: List<Component>?,
    ): List<MoreOptionItem> {
        println("Customization - ${customization?.size}")
        val list = mutableListOf<MoreOptionItem>()
        list.add(
            MoreOptionItem(
                id = MoreOption.STATEMENT,
                icon = Res.drawable.icon_statement,
                title = "Account"
            )
        )
        list.add(
            MoreOptionItem(
                id = MoreOption.PROFILE,
                icon = Res.drawable.icon_account,
                title = "Profile"
            )
        )
        list.add(
            MoreOptionItem(
                id = MoreOption.HELP,
                icon = Res.drawable.icon_namsthe,
                title = "Help"
            )
        )
        list.add(
            MoreOptionItem(
                id = MoreOption.SETTINGS,
                icon = Res.drawable.icon_settings,
                title = "Settings"
            )
        )

        customization?.forEach { component ->
            component.items?.forEach { item: Component ->
                val action = item.eventHandlers?.click
                    ?.find { it.action == "navigate" }
                println("Customization action -  ${item}")
                list.add(
                    MoreOptionItem(
                        id = MoreOption.DYNAMIC,
                        iconUrl = item.icon,
                        title = item.title ?: "",
                        icon = null,
                        deeplink = action?.url
                    )
                )
            }
        }

        println("Home more options = ${list.size}")
        return list
    }
}
