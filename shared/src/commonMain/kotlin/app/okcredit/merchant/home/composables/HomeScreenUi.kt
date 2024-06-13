@file:OptIn(ExperimentalMaterialApi::class, ExperimentalMaterialApi::class,
    ExperimentalMaterialApi::class
)

package app.okcredit.merchant.home.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BackdropScaffold
import androidx.compose.material.BackdropValue
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.ButtonElevation
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material.rememberBackdropScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import app.okcredit.merchant.home.HomeContract
import app.okcredit.merchant.home.HomeTab
import app.okcredit.merchant.home.ReminderFilterOption
import app.okcredit.merchant.home.isCustomerTab
import app.okcredit.merchant.home.isSupplierTab
import app.okcredit.ui.icon_add
import app.okcredit.ui.icon_add_photo
import app.okcredit.ui.icon_error_fill
import app.okcredit.ui.icon_filter_list
import app.okcredit.ui.icon_search
import app.okcredit.ui.theme.OkCreditTheme
import app.okcredit.ui.theme.green_lite_1
import app.okcredit.ui.theme.grey50
import app.okcredit.ui.theme.grey900
import kotlinx.coroutines.launch
import merchant_app.shared.generated.resources.Res
import merchant_app.shared.generated.resources.add_customer
import merchant_app.shared.generated.resources.add_supplier
import merchant_app.shared.generated.resources.clear_filter
import merchant_app.shared.generated.resources.ic_add_first_supplier
import merchant_app.shared.generated.resources.ic_ledger_tutorial
import merchant_app.shared.generated.resources.no_results_found
import merchant_app.shared.generated.resources.supplier_learn_more_title
import merchant_app.shared.generated.resources.t_001_addrel_first_time_txt
import okcredit.base.units.paisa
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeScreenUi(
    state: HomeContract.State,
    onAvatarClicked: () -> Unit,
    onToolbarActionClicked: (HomeContract.ToolbarAction) -> Unit,
    onPrimaryVpaClicked: () -> Unit,
    onTabChanged: (Boolean) -> Unit,
    onSearchClicked: () -> Unit,
    onSortAndFilterClicked: () -> Unit,
    onCustomerClicked: (String) -> Unit,
    onCustomerProfileClicked: (String) -> Unit,
    onSupplierClicked: (String) -> Unit,
    onSupplierProfileClicked: (String) -> Unit,
    onAddRelationshipClicked: () -> Unit,
    onDynamicItemClicked: (String, String) -> Unit,
    onSummaryCardClicked: (HomeTab) -> Unit,
    onPullToRefresh: () -> Unit,
    onClearFilterClicked: () -> Unit,
    onUserAlertClicked: (HomeContract.UserAlert) -> Unit,
) {
    val pullRefreshState = rememberPullRefreshState(
        refreshing = state.homeSyncLoading,
        onRefresh = onPullToRefresh
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pullRefresh(pullRefreshState)
    ) {
        val customerListState = rememberLazyListState()
        val supplierListState = rememberLazyListState()
        val extendedFabInCustomer by remember {
            derivedStateOf { customerListState.firstVisibleItemIndex == 0 }
        }
        val extendFabInSupplier by remember {
            derivedStateOf { supplierListState.firstVisibleItemIndex == 0 }
        }
        BackdropScaffold(
            scaffoldState = rememberBackdropScaffoldState(BackdropValue.Revealed),
            frontLayerShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
            frontLayerScrimColor = Color.Unspecified,
            backLayerBackgroundColor = grey50,
            modifier = Modifier.fillMaxSize(),
            appBar = {
                HomeTabBar(
                    toolbarAction = state.toolbarAction,
                    activeBusiness = state.activeBusiness,
                    primaryVpa = state.primaryVpa,
                    onAvatarClicked = onAvatarClicked,
                    onToolbarActionClicked = onToolbarActionClicked,
                    onPrimaryVpaClicked = onPrimaryVpaClicked,
                )
            },
            backLayerContent = {
                DynamicComponent(
                    dynamicItems = state.dynamicItems,
                    userAlert = state.userAlert,
                    onDynamicItemClicked = onDynamicItemClicked,
                    onUserAlertClicked = onUserAlertClicked,
                )
            },
            frontLayerContent = {
                HomeContent(
                    state = state,
                    customerListState = customerListState,
                    supplierListState = supplierListState,
                    onTabChanged = onTabChanged,
                    onSearchClicked = onSearchClicked,
                    onSortAndFilterClicked = onSortAndFilterClicked,
                    onCustomerClicked = onCustomerClicked,
                    onCustomerProfileClicked = onCustomerProfileClicked,
                    onSupplierClicked = onSupplierClicked,
                    onSupplierProfileClicked = onSupplierProfileClicked,
                    onSummaryCardClicked = onSummaryCardClicked,
                    onAddRelationshipClicked = onAddRelationshipClicked,
                    onClearFilterClicked = onClearFilterClicked
                )
            }
        )

        PullRefreshIndicator(
            refreshing = state.homeSyncLoading,
            state = pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter)
        )

        BottomActions(
            selectedTab = state.selectedTab,
            modifier = Modifier.align(Alignment.BottomEnd),
            extendFab = if (state.selectedTab.isCustomerTab()) extendedFabInCustomer else extendFabInSupplier,
            showAddRelationship = state.showAddRelationship,
            onAddRelationshipClicked = onAddRelationshipClicked,
        )
    }
}

@Composable
fun BottomActions(
    selectedTab: HomeTab,
    modifier: Modifier,
    extendFab: Boolean,
    showAddRelationship: Boolean,
    onAddRelationshipClicked: () -> Unit,
) {
    Column(
        modifier = modifier.padding(all = 16.dp),
        horizontalAlignment = Alignment.End,
    ) {
        if (showAddRelationship) {
            Spacer(Modifier.height(16.dp))
            AnimatedExtendedFloatingActionButton(
                extendFab = extendFab,
                label = {
                    Text(
                        text = stringResource(
                            resource = if (selectedTab.isSupplierTab()) {
                                Res.string.add_supplier
                            } else {
                                Res.string.add_customer
                            }
                        ),
                        style = MaterialTheme.typography.subtitle2,
                        color = grey900
                    )
                },
                icon = {
                    Icon(
                        painterResource(resource = app.okcredit.ui.Res.drawable.icon_add),
                        contentDescription = "",
                        tint = grey900,
                        modifier = Modifier.size(24.dp)
                    )
                },
                minSize = 56.dp,
                onClick = onAddRelationshipClicked,
                modifier = Modifier.semantics { contentDescription = "Add Relationship" }.testTag("Add Relationship")
            )
        }
    }
}

@Composable
fun AnimatedExtendedFloatingActionButton(
    extendFab: Boolean,
    modifier: Modifier = Modifier,
    icon: @Composable () -> Unit,
    label: @Composable () -> Unit,
    minSize: Dp = 56.dp,
    background: Color = green_lite_1,
    onClick: () -> Unit,
) {
    FloatingActionButton(
        modifier = modifier.sizeIn(minWidth = minSize, minHeight = minSize),
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        backgroundColor = background,
    ) {
        Row(
            modifier = Modifier.padding(start = 16.dp, end = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            icon()
            AnimatedVisibility(visible = extendFab) {
                Spacer(Modifier.width(6.dp))
            }
            AnimatedVisibility(visible = extendFab) {
                label()
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeContent(
    state: HomeContract.State,
    customerListState: LazyListState,
    supplierListState: LazyListState,
    onTabChanged: (Boolean) -> Unit,
    onSearchClicked: () -> Unit,
    onSortAndFilterClicked: () -> Unit,
    onCustomerClicked: (String) -> Unit,
    onCustomerProfileClicked: (String) -> Unit,
    onSupplierClicked: (String) -> Unit,
    onSupplierProfileClicked: (String) -> Unit,
    onSummaryCardClicked: (HomeTab) -> Unit,
    onAddRelationshipClicked: () -> Unit,
    onClearFilterClicked: () -> Unit,
) {
    Column(modifier = Modifier) {
        Spacer(modifier = Modifier.size(12.dp))
        val pagerState = rememberPagerState { 2 }
        val scope = rememberCoroutineScope()
        HomeHeader(
            selectedTab = if (pagerState.currentPage == 1) HomeTab.SUPPLIER_TAB else HomeTab.CUSTOMER_TAB,
            showSortAndFilter = state.showSortAndFilter,
            showSearch = state.showSearch,
            sortOrFilterAppliedCount = state.sortOrFilterAppliedCount,
            onTabChanged = {
                scope.launch {
                    if (it) {
                        pagerState.animateScrollToPage(1)
                    } else {
                        pagerState.animateScrollToPage(0)
                    }
                }
            },
            onSearchClicked = onSearchClicked,
            onSortAndFilterClicked = onSortAndFilterClicked
        )
        HorizontalPager(
            modifier = Modifier,
            state = pagerState,
            pageContent = { page ->
                when (page) {
                    0 -> {
                        HomeList(
                            listState = customerListState,
                            loadingItems = state.loadingCustomers,
                            homeItemList = state.customers,
                            scrollToTop = state.scrollListToTop,
                            sortOfFilterAppliedCount = state.sortOrFilterAppliedCount,
                            emptyPlaceHolder = {
                                EmptyPlaceholder(
                                    emptyImage = {
                                        Image(
                                            painter = painterResource(resource = Res.drawable.ic_ledger_tutorial),
                                            contentDescription = stringResource(resource = Res.string.t_001_addrel_first_time_txt),
                                            modifier = Modifier.size(160.dp)
                                        )
                                    },
                                    message = stringResource(resource = Res.string.t_001_addrel_first_time_txt),
                                    emptyButton = {
                                        DarkSolidButton(
                                            onClick = onAddRelationshipClicked,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(horizontal = 20.dp),
                                            drawableId = app.okcredit.ui.Res.drawable.icon_add_photo,
                                            color = green_lite_1,
                                            text = stringResource(resource = Res.string.add_customer),
                                            drawableTint = MaterialTheme.colors.onSurface,
                                            textStyle = MaterialTheme.typography.button.copy(color = MaterialTheme.colors.onSurface),
                                        )
                                    }
                                )
                            },
                            onItemClicked = onCustomerClicked,
                            onProfileClicked = onCustomerProfileClicked,
                            onSummaryCardClicked = onSummaryCardClicked,
                            onClearFilterClicked = onClearFilterClicked,
                        )
                    }

                    1 -> {
                        HomeList(
                            listState = supplierListState,
                            loadingItems = state.loadingSuppliers,
                            homeItemList = state.suppliers,
                            scrollToTop = state.scrollListToTop,
                            sortOfFilterAppliedCount = state.sortOrFilterAppliedCount,
                            emptyPlaceHolder = {
                                EmptyPlaceholder(
                                    emptyImage = {
                                        Image(
                                            painter = painterResource(resource = Res.drawable.ic_add_first_supplier),
                                            contentDescription = stringResource(resource = Res.string.supplier_learn_more_title),
                                            modifier = Modifier.size(160.dp)
                                        )
                                    },
                                    message = stringResource(resource = Res.string.supplier_learn_more_title),
                                    emptyButton = {
                                        DarkSolidButton(
                                            onClick = onAddRelationshipClicked,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(horizontal = 20.dp),
                                            drawableId = app.okcredit.ui.Res.drawable.icon_add_photo,
                                            color = green_lite_1,
                                            text = stringResource(resource = Res.string.add_supplier),
                                            drawableTint = MaterialTheme.colors.onSurface,
                                            textStyle = MaterialTheme.typography.button.copy(color = MaterialTheme.colors.onSurface),
                                        )
                                    }
                                )
                            },
                            onItemClicked = onSupplierClicked,
                            onProfileClicked = onSupplierProfileClicked,
                            onSummaryCardClicked = onSummaryCardClicked,
                            onClearFilterClicked = onClearFilterClicked,
                        )
                    }
                }
            }
        )
        LaunchedEffect(pagerState.currentPage) {
            onTabChanged(pagerState.currentPage == 1)
        }
    }
}

@Composable
fun DarkSolidButton(
    onClick: () -> Unit,
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    color: Color = MaterialTheme.colors.primary,
    elevation: ButtonElevation? = ButtonDefaults.elevation(),
    horizontalContentPadding: Dp = 16.dp,
    verticalContentPadding: Dp = 8.dp,
    textStyle: TextStyle = MaterialTheme.typography.subtitle2,
    drawableId: DrawableResource? = null,
    drawableHeight: Dp = 32.dp,
    drawableWidth: Dp = 32.dp,
    drawablePadding: Dp = 8.dp,
    drawableTint: Color = Color.Unspecified
) {
    Button(
        onClick = onClick,
        contentPadding = PaddingValues(
            horizontal = horizontalContentPadding,
            vertical = verticalContentPadding
        ),
        shape = RoundedCornerShape(percent = 50),
        colors = ButtonDefaults.buttonColors(backgroundColor = color),
        elevation = elevation,
        enabled = enabled,
        modifier = modifier
    ) {
        if (drawableId != null) {
            Icon(
                painter = painterResource(resource = drawableId),
                contentDescription = null,
                modifier = Modifier
                    .height(drawableHeight)
                    .width(drawableWidth)
                    .padding(end = drawablePadding),
                tint = drawableTint
            )
        }

        Text(
            text = text,
            style = textStyle,
        )
    }
}

@Composable
fun HomeList(
    listState: LazyListState,
    homeItemList: List<HomeContract.HomeItem>,
    scrollToTop: Boolean,
    loadingItems: Boolean,
    sortOfFilterAppliedCount: Int,
    emptyPlaceHolder: @Composable () -> Unit,
    onClearFilterClicked: () -> Unit,
    onItemClicked: (String) -> Unit,
    onProfileClicked: (String) -> Unit,
    onSummaryCardClicked: (HomeTab) -> Unit,
) {
    if (loadingItems) {
        LoadingShimmer()
    } else if (homeItemList.isEmpty()) {
        if (sortOfFilterAppliedCount == 0) {
            emptyPlaceHolder()
        } else {
            EmptyFilterPlaceHolder(onClearFilterClicked)
        }
    } else {
        LazyColumn(state = listState) {
            items(
                items = homeItemList,
                key = {
                    when (it) {
                        is HomeContract.HomeItem.CustomerItem -> it.customerId
                        is HomeContract.HomeItem.SummaryItem -> "summaryCard"
                        is HomeContract.HomeItem.SupplierItem -> it.supplierId
                    }
                }
            ) {
                when (it) {
                    is HomeContract.HomeItem.CustomerItem -> {
                        CustomerRow(
                            customerItem = it,
                            onItemClicked = onItemClicked,
                            onProfileClicked = onProfileClicked,
                        )
                    }

                    is HomeContract.HomeItem.SummaryItem -> {
                        SummaryCard(
                            summaryItem = it,
                            onSummaryCardClicked = onSummaryCardClicked,
                        )
                    }

                    is HomeContract.HomeItem.SupplierItem -> {
                        SupplierRow(
                            supplierItem = it,
                            onClick = onItemClicked,
                            onProfileClicked = onProfileClicked,
                        )
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(128.dp))
            }
        }

        LaunchedEffect(key1 = scrollToTop) {
            if (scrollToTop) {
                listState.animateScrollToItem(0)
            }
        }
    }
}

@Composable
fun EmptyFilterPlaceHolder(onClearFilterClicked: () -> Unit) {
    EmptyPlaceholder(
        emptyImage = {
            Image(
                painter = painterResource(resource = app.okcredit.ui.Res.drawable.icon_error_fill),
                contentDescription = stringResource(resource = Res.string.supplier_learn_more_title),
            )
        },
        message = stringResource(resource = Res.string.no_results_found),
        emptyButton = {
            TextButton(onClick = onClearFilterClicked) {
                Text(
                    text = stringResource(resource = Res.string.clear_filter),
                    style = MaterialTheme.typography.subtitle2,
                    color = MaterialTheme.colors.primary
                )
            }
        }
    )
}

@Composable
fun HomeHeader(
    selectedTab: HomeTab,
    showSortAndFilter: Boolean,
    showSearch: Boolean,
    sortOrFilterAppliedCount: Int,
    onTabChanged: (Boolean) -> Unit,
    onSearchClicked: () -> Unit,
    onSortAndFilterClicked: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        CustomerSupplierTab(
            selectedTab = selectedTab,
            width = 360.dp - 136.dp,
            onTabChanged = onTabChanged
        )
        if (showSortAndFilter) {
            Box {
                Surface(
                    shape = CircleShape,
                    color = if (sortOrFilterAppliedCount > 0) green_lite_1 else grey50,
                    onClick = onSortAndFilterClicked,
                ) {
                    Box(
                        modifier = Modifier.size(40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(resource = app.okcredit.ui.Res.drawable.icon_filter_list),
                            contentDescription = "",
                            modifier = Modifier.size(24.dp),
                            tint = if (sortOrFilterAppliedCount > 0) {
                                MaterialTheme.colors.primary
                            } else {
                                MaterialTheme.colors.onSurface
                            }
                        )
                    }
                }
                if (sortOrFilterAppliedCount > 0) {
                    Text(
                        text = sortOrFilterAppliedCount.toString(),
                        color = MaterialTheme.colors.onError,
                        style = MaterialTheme.typography.caption,
                        modifier = Modifier
                            .size(16.dp)
                            .align(Alignment.TopEnd)
                            .background(MaterialTheme.colors.error, CircleShape),
                        textAlign = TextAlign.Center,
                    )
                }
            }
        }
        if (showSearch) {
            Surface(
                shape = CircleShape,
                color = grey50,
                onClick = onSearchClicked,
            ) {
                Box(
                    modifier = Modifier.size(40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(resource = app.okcredit.ui.Res.drawable.icon_search),
                        contentDescription = "",
                        modifier = Modifier.size(24.dp),
                        tint = MaterialTheme.colors.onSurface
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun HomeScreenUiPreview() {
    OkCreditTheme {
        HomeScreenUi(
            state = HomeContract.State(
                loading = true,
                dynamicItems = listOf(
                    HomeContract.DynamicItem(
                        id = "1",
                        icon = "",
                        title = "Stock Entry",
                        subtitle = "subtitle",
                        deeplink = ""
                    ),
                    HomeContract.DynamicItem(
                        id = "1",
                        icon = "",
                        title = "GST Bills",
                        subtitle = "subtitle",
                        deeplink = ""
                    ),
                    HomeContract.DynamicItem(
                        id = "1",
                        icon = "",
                        title = "Earn Interest Daily",
                        subtitle = "subtitle",
                        deeplink = ""
                    )
                ),
                userAlert = HomeContract.UserAlert.UnSyncedTransactions,
                primaryVpa = "okcredit@upi",
                selectedCustomerReminderFilterOptions = setOf(
                    ReminderFilterOption.OVERDUE,
                    ReminderFilterOption.TODAY,
                    ReminderFilterOption.UPCOMING
                ),
                customers = listOf(
                    HomeContract.HomeItem.CustomerItem(
                        isDefaulter = true,
                        customerId = "1",
                        profileImage = "",
                        name = "Harsh",
                        balance = 1000L.paisa,
                        subtitle = buildAnnotatedString { append("Last payment 2 days ago") },
                    ),
                    HomeContract.HomeItem.CustomerItem(
                        customerId = "3",
                        profileImage = "",
                        name = "Aditya",
                        balance = 1000L.paisa,
                        commonLedger = true,
                        subtitle = buildAnnotatedString { append("Last payment 2 days ago") },
                    ),
                    HomeContract.HomeItem.CustomerItem(
                        isDefaulter = false,
                        customerId = "2",
                        profileImage = "",
                        name = "Gaurav",
                        balance = (-1000L).paisa,
                        subtitle = buildAnnotatedString { append("Last payment 2 days ago") },
                    )
                ),
            ),
            onAvatarClicked = {},
            onToolbarActionClicked = {},
            onPrimaryVpaClicked = {},
            onTabChanged = {},
            onSearchClicked = {},
            onSortAndFilterClicked = {},
            onCustomerClicked = {},
            onSupplierClicked = {},
            onCustomerProfileClicked = {},
            onSupplierProfileClicked = {},
            onAddRelationshipClicked = {},
            onDynamicItemClicked = { _, _ -> },
            onSummaryCardClicked = {},
            onPullToRefresh = {},
            onClearFilterClicked = {},
            onUserAlertClicked = {},
        )
    }
}
