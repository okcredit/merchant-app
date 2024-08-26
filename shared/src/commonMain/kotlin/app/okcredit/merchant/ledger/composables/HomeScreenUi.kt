@file:OptIn(ExperimentalMaterial3Api::class)

package app.okcredit.merchant.ledger.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonElevation
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import app.okcredit.merchant.ledger.HomeContract
import app.okcredit.merchant.ledger.HomeTab
import app.okcredit.merchant.ledger.ReminderFilterOption
import app.okcredit.merchant.ledger.isCustomerTab
import app.okcredit.merchant.ledger.isSupplierTab
import app.okcredit.ui.ic_add_first_supplier
import app.okcredit.ui.ic_ledger_tutorial
import app.okcredit.ui.icon_add
import app.okcredit.ui.icon_add_photo
import app.okcredit.ui.icon_error_fill
import app.okcredit.ui.icon_filter_list
import app.okcredit.ui.icon_search
import app.okcredit.ui.theme.OkCreditTheme
import app.okcredit.ui.theme.grey900
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import merchant_app.shared.generated.resources.Res
import merchant_app.shared.generated.resources.add_customer
import merchant_app.shared.generated.resources.add_supplier
import merchant_app.shared.generated.resources.clear_filter
import merchant_app.shared.generated.resources.no_results_found
import merchant_app.shared.generated.resources.supplier_learn_more_title
import merchant_app.shared.generated.resources.t_001_addrel_first_time_txt
import okcredit.base.units.paisa
import okcredit.base.units.timestamp
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

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
    val customerListState = rememberLazyListState()
    val supplierListState = rememberLazyListState()
    val extendedFabInCustomer by remember {
        derivedStateOf { customerListState.firstVisibleItemIndex == 0 }
    }
    val extendFabInSupplier by remember {
        derivedStateOf { supplierListState.firstVisibleItemIndex == 0 }
    }

    Scaffold(
        topBar = {
            Column {
                HomeTabBar(
                    toolbarAction = state.toolbarAction,
                    activeBusiness = state.activeBusiness,
                    primaryVpa = state.primaryVpa,
                    onAvatarClicked = onAvatarClicked,
                    onToolbarActionClicked = onToolbarActionClicked,
                    onPrimaryVpaClicked = onPrimaryVpaClicked,
                )
                DynamicComponent(
                    dynamicItems = state.dynamicItems,
                    userAlert = state.userAlert,
                    onDynamicItemClicked = onDynamicItemClicked,
                    onUserAlertClicked = onUserAlertClicked,
                )
            }
        },
        floatingActionButton = {
            BottomActions(
                selectedTab = state.selectedTab,
                modifier = Modifier,
                extendFab = if (state.selectedTab.isCustomerTab()) extendedFabInCustomer else extendFabInSupplier,
                showAddRelationship = state.showAddRelationship,
                onAddRelationshipClicked = onAddRelationshipClicked,
            )
        },
        floatingActionButtonPosition = FabPosition.End,
    ) {
        HomeContent(
            modifier = Modifier.padding(it),
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
        modifier = modifier,
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
                        style = MaterialTheme.typography.titleSmall,
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
                modifier = Modifier.semantics { contentDescription = "Add Relationship" }
                    .testTag("Add Relationship")
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
    background: Color = MaterialTheme.colorScheme.secondary,
    onClick: () -> Unit,
) {
    FloatingActionButton(
        modifier = modifier.sizeIn(minWidth = minSize, minHeight = minSize),
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        containerColor = background,
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

@Composable
fun HomeContent(
    modifier: Modifier,
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
    Column(
        modifier = modifier.background(
            color = MaterialTheme.colorScheme.surface,
            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
        )
    ) {
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
                                            painter = painterResource(resource = app.okcredit.ui.Res.drawable.ic_ledger_tutorial),
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
                                            color = MaterialTheme.colorScheme.secondary,
                                            text = stringResource(resource = Res.string.add_customer),
                                            drawableTint = MaterialTheme.colorScheme.onSurface,
                                            textStyle = MaterialTheme.typography.titleSmall.copy(
                                                color = MaterialTheme.colorScheme.onSurface
                                            ),
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
                                            painter = painterResource(resource = app.okcredit.ui.Res.drawable.ic_add_first_supplier),
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
                                            color = MaterialTheme.colorScheme.secondary,
                                            text = stringResource(resource = Res.string.add_supplier),
                                            drawableTint = MaterialTheme.colorScheme.onSurface,
                                            textStyle = MaterialTheme.typography.labelLarge.copy(
                                                color = MaterialTheme.colorScheme.onSurface
                                            ),
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
    color: Color = MaterialTheme.colorScheme.primary,
    elevation: ButtonElevation? = ButtonDefaults.buttonElevation(),
    horizontalContentPadding: Dp = 16.dp,
    verticalContentPadding: Dp = 8.dp,
    textStyle: TextStyle = MaterialTheme.typography.titleSmall,
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
        colors = ButtonDefaults.buttonColors(
            containerColor = color,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
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
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.primary
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
            modifier = Modifier.weight(1.0f),
            onTabChanged = onTabChanged
        )
        if (showSortAndFilter) {
            Box {
                Surface(
                    shape = CircleShape,
                    color = if (sortOrFilterAppliedCount > 0) {
                        MaterialTheme.colorScheme.secondary
                    } else {
                        MaterialTheme.colorScheme.background
                    },
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
                                MaterialTheme.colorScheme.onSecondary
                            } else {
                                MaterialTheme.colorScheme.onBackground
                            }
                        )
                    }
                }
                if (sortOrFilterAppliedCount > 0) {
                    Text(
                        text = sortOrFilterAppliedCount.toString(),
                        color = MaterialTheme.colorScheme.onError,
                        style = MaterialTheme.typography.labelMedium,
                        modifier = Modifier
                            .size(16.dp)
                            .align(Alignment.TopEnd)
                            .background(MaterialTheme.colorScheme.error, CircleShape),
                        textAlign = TextAlign.Center,
                    )
                }
            }
        }
        if (showSearch) {
            Surface(
                shape = CircleShape,
                color = MaterialTheme.colorScheme.background,
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
                        tint = MaterialTheme.colorScheme.onBackground
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
                        lastActivity = Clock.System.now().toEpochMilliseconds().timestamp,
                        lastActivityMetaInfo = 1
                    ),
                    HomeContract.HomeItem.CustomerItem(
                        customerId = "3",
                        profileImage = "",
                        name = "Aditya",
                        balance = 1000L.paisa,
                        commonLedger = true,
                        lastActivity = Clock.System.now().toEpochMilliseconds().timestamp,
                        lastActivityMetaInfo = 1
                    ),
                    HomeContract.HomeItem.CustomerItem(
                        isDefaulter = false,
                        customerId = "2",
                        profileImage = "",
                        name = "Gaurav",
                        balance = (-1000L).paisa,
                        lastActivity = Clock.System.now().toEpochMilliseconds().timestamp,
                        lastActivityMetaInfo = 1
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
