package app.okcredit.merchant.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
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
import okcredit.base.di.rememberScreenModel
import org.jetbrains.compose.resources.painterResource

class HomeScreen : Screen {

    @Composable
    override fun Content() {
        val screenModel = rememberScreenModel<HomeScreenModel>()
        val state by screenModel.states.collectAsState()

        HomeScreenUi(
            state = state,
            onMoreItemClicked = { option, deeplink ->
            },
        )
    }
}

@Composable
fun HomeScreenUi(state: HomeContract.State, onMoreItemClicked: (MoreOption, String?) -> Unit) {
    TabNavigator(HomeLedgerTab) {
        var showHomeMoreOptions by remember { mutableStateOf(false) }
        var selectedItem by remember { mutableStateOf(HOME_LEDGER) }
        Scaffold(
            content = {
                Box(
                    modifier = Modifier.padding(it).fillMaxSize(),
                    contentAlignment = Alignment.BottomCenter,
                ) {
                    CurrentTab()

                    val density = LocalDensity.current
                    AnimatedVisibility(
                        visible = showHomeMoreOptions,
                        enter = slideInVertically { with(density) { 120.dp.roundToPx() } } +
                            fadeIn(initialAlpha = 0.3f),
                        exit = slideOutVertically(targetOffsetY = { with(density) { 120.dp.roundToPx() } }) +
                            fadeOut(),
                    ) {
                        HomeMoreOptions(
                            modifier = Modifier.align(Alignment.BottomCenter),
                            items = state.moreOptions,
                            onMoreItemClicked = onMoreItemClicked,
                        )
                    }
                }
            },
            bottomBar = {
                val tabNavigator = LocalTabNavigator.current
                BottomNavigationUi(
                    onItemClicked = {
                        val tab = when (it) {
                            HOME_LEDGER -> {
                                selectedItem = HOME_LEDGER
                                showHomeMoreOptions = false
                                HomeLedgerTab
                            }
                            HOME_PAYMENT -> {
                                selectedItem = NavItem.HOME_PAYMENT
                                showHomeMoreOptions = false
                                HomePaymentTab
                            }
                            HOME_OKLOAN -> {
                                selectedItem = NavItem.HOME_OKLOAN
                                showHomeMoreOptions = false
                                HomeLoansTab
                            }
                            HOME_OK_FEED -> {
                                selectedItem = NavItem.HOME_OK_FEED
                                showHomeMoreOptions = false
                                HomeOkFeedTab
                            }
                            HOME_MORE_OPTIONS -> {
                                showHomeMoreOptions = !showHomeMoreOptions
                                selectedItem = NavItem.HOME_MORE_OPTIONS
                                tabNavigator.current
                            }
                        }
                        tabNavigator.current = tab
                    },
                    list = listOf(
                        BottomMenuItem(
                            navItem = HOME_LEDGER,
                            drawableId = HomeLedgerTab.options.icon
                                ?: painterResource(Res.drawable.icon_home),
                            label = HomeLedgerTab.options.title,
                        ),
                        BottomMenuItem(
                            navItem = HOME_PAYMENT,
                            drawableId = painterResource(Res.drawable.icon_collections),
                            label = HomePaymentTab.options.title,
                        ),
                        BottomMenuItem(
                            navItem = HOME_OK_FEED,
                            drawableId = painterResource(Res.drawable.icon_feed),
                            label = HomeOkFeedTab.options.title,
                        ),
                        BottomMenuItem(
                            navItem = HOME_MORE_OPTIONS,
                            drawableId = painterResource(Res.drawable.icon_more),
                            label = "More",
                        ),
                    ),
                    selectedItem = selectedItem,
                )
            },
        )
    }
}
