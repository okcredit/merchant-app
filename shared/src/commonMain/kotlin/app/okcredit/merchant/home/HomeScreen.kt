package app.okcredit.merchant.home

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import app.okcredit.merchant.home.NavItem.*
import app.okcredit.merchant.ledger.HomeLedgerTab
import app.okcredit.merchant.loans.HomeLoansTab
import app.okcredit.merchant.okfeed.HomeOkFeedTab
import app.okcredit.merchant.payment.HomePaymentTab
import app.okcredit.ui.Res
import app.okcredit.ui.icon_collections
import app.okcredit.ui.icon_feed
import app.okcredit.ui.icon_home
import app.okcredit.ui.icon_more
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.TabNavigator
import org.jetbrains.compose.resources.painterResource

class HomeScreen : Screen {

    @Composable
    override fun Content() {
        TabNavigator(HomeLedgerTab) {
            Scaffold(
                content = {
                    CurrentTab()
                },
                bottomBar = {
                    val tabNavigator = LocalTabNavigator.current
                    BottomNavigationUi(
                        onItemClicked = {
                            val tab = when (it) {
                                HOME_LEDGER -> HomeLedgerTab
                                HOME_PAYMENT -> HomePaymentTab
                                HOME_OKLOAN -> HomeLoansTab
                                HOME_OK_FEED -> HomeOkFeedTab
                                HOME_MORE_OPTIONS -> tabNavigator.current
                            }
                            tabNavigator.current = tab
                        },
                        list = listOf(
                            BottomMenuItem(
                                navItem = HOME_LEDGER,
                                drawableId = HomeLedgerTab.options.icon ?: painterResource(Res.drawable.icon_home),
                                label = HomeLedgerTab.options.title
                            ),
                            BottomMenuItem(
                                navItem = HOME_PAYMENT,
                                drawableId = painterResource(Res.drawable.icon_collections),
                                label = HomePaymentTab.options.title
                            ),
                            BottomMenuItem(
                                navItem = HOME_OK_FEED,
                                drawableId = painterResource(Res.drawable.icon_feed),
                                label = HomeOkFeedTab.options.title
                            ),
                            BottomMenuItem(
                                navItem = HOME_MORE_OPTIONS,
                                drawableId = painterResource(Res.drawable.icon_more),
                                label = "More"
                            )
                        ),
                        selectedItem = HOME_LEDGER
                    )
                }
            )
        }
    }
}