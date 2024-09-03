@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

package app.okcredit.merchant.search.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import app.okcredit.merchant.ledger.composables.LoadingShimmer
import app.okcredit.merchant.search.HeaderType
import app.okcredit.merchant.search.HomeSearchContract
import app.okcredit.merchant.search.HomeSearchItem
import app.okcredit.ui.please_wait
import app.okcredit.ui.theme.OkCreditTheme
import okcredit.base.units.paisa
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun HomeSearchUi(
    state: HomeSearchContract.State,
    onBackPress: () -> Unit,
    onSearchQueryChange: (String) -> Unit,
    onCustomerItemClicked: (String) -> Unit,
    onCustomerProfileClicked: (String) -> Unit,
    onCustomerWhatsAppClicked: (String) -> Unit,
    onSupplierItemClicked: (String) -> Unit,
    onSupplierProfileClicked: (String) -> Unit,
    onContactItemClicked: (String) -> Unit,
    onAddToAccountClicked: (String) -> Unit,
) {
    Scaffold(
        topBar = {
            HomeSearchTopBar(
                searchQuery = state.searchQuery,
                onBackPress = onBackPress,
                onValueChange = onSearchQueryChange,
            )
        },
        containerColor = MaterialTheme.colorScheme.surface,
    ) {
        HomeSearchContent(
            state = state,
            modifier = Modifier.padding(it),
            onCustomerItemClicked = onCustomerItemClicked,
            onCustomerProfileClicked = onCustomerProfileClicked,
            onCustomerWhatsAppClicked = onCustomerWhatsAppClicked,
            onSupplierItemClicked = onSupplierItemClicked,
            onSupplierProfileClicked = onSupplierProfileClicked,
            onContactItemClicked = onContactItemClicked,
            onAddToAccountClicked = onAddToAccountClicked,
        )
    }

    if (state.addAccountInProgress) {
        ModalBottomSheet(onDismissRequest = {}) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Spacer(modifier = Modifier.height(16.dp))
                CircularProgressIndicator()
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = stringResource(resource = app.okcredit.ui.Res.string.please_wait))
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun HomeSearchTopBar(
    searchQuery: String,
    onBackPress: () -> Unit,
    onValueChange: (String) -> Unit,
) {
    val focusRequester = remember { FocusRequester() }
    val text = remember { mutableStateOf(TextFieldValue(searchQuery)) }
    TopAppBar(
        navigationIcon = {
            IconButton(onClick = onBackPress) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                )
            }
        },
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                BasicTextField(
                    value = text.value,
                    onValueChange = { newValue ->
                        text.value = newValue
                        onValueChange(newValue.text)
                    },
                    modifier = Modifier
                        .weight(1f)
                        .focusRequester(focusRequester),
                    textStyle = MaterialTheme.typography.titleMedium.copy(
                        color = MaterialTheme.colorScheme.onSurface,
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    singleLine = true,
                    cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                )

                IconButton(
                    onClick = {
                        text.value = TextFieldValue("")
                        onValueChange("")
                    },
                ) {
                    Icon(
                        imageVector = if (text.value.text.isEmpty()) Icons.Default.Search else Icons.Default.Clear,
                        tint = MaterialTheme.colorScheme.onSurface,
                        contentDescription = "",
                    )
                }
            }
        },
    )
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
}

@Composable
fun HomeSearchContent(
    state: HomeSearchContract.State,
    modifier: Modifier,
    onCustomerItemClicked: (String) -> Unit,
    onCustomerProfileClicked: (String) -> Unit,
    onCustomerWhatsAppClicked: (String) -> Unit,
    onSupplierItemClicked: (String) -> Unit,
    onSupplierProfileClicked: (String) -> Unit,
    onContactItemClicked: (String) -> Unit,
    onAddToAccountClicked: (String) -> Unit,
) {
    if (state.loading) {
        LoadingShimmer()
    } else {
        HomeSearchList(
            searchList = state.searchList,
            modifier = modifier,
            onCustomerItemClicked = onCustomerItemClicked,
            onCustomerProfileClicked = onCustomerProfileClicked,
            onCustomerWhatsAppClicked = onCustomerWhatsAppClicked,
            onSupplierItemClicked = onSupplierItemClicked,
            onSupplierProfileClicked = onSupplierProfileClicked,
            onContactItemClicked = onContactItemClicked,
            onAddToAccountClicked = onAddToAccountClicked,
        )
    }
}

@Composable
fun HomeSearchList(
    searchList: List<HomeSearchItem>,
    modifier: Modifier,
    onCustomerItemClicked: (String) -> Unit,
    onCustomerProfileClicked: (String) -> Unit,
    onCustomerWhatsAppClicked: (String) -> Unit,
    onSupplierItemClicked: (String) -> Unit,
    onSupplierProfileClicked: (String) -> Unit,
    onContactItemClicked: (String) -> Unit,
    onAddToAccountClicked: (String) -> Unit,
) {
    LazyColumn(modifier = modifier) {
        items(
            items = searchList,
            key = {
                when (it) {
                    is HomeSearchItem.ContactItem -> it.contactId
                    is HomeSearchItem.CustomerItem -> it.customerId
                    is HomeSearchItem.HeaderItem -> it.type
                    is HomeSearchItem.NoUserFoundItem -> it.searchQuery
                    is HomeSearchItem.SupplierItem -> it.supplierId
                }
            },
        ) { item ->
            when (item) {
                is HomeSearchItem.HeaderItem -> {
                    HomeSearchHeaderItem(item)
                }

                is HomeSearchItem.CustomerItem -> {
                    HomeSearchCustomerItem(
                        customerItem = item,
                        onItemClicked = onCustomerItemClicked,
                        onProfileClicked = onCustomerProfileClicked,
                        onWhatsAppClicked = onCustomerWhatsAppClicked,
                    )
                }

                is HomeSearchItem.SupplierItem -> {
                    HomeSearchSupplierItem(
                        supplierItem = item,
                        onItemClicked = onSupplierItemClicked,
                        onProfileClicked = onSupplierProfileClicked,
                    )
                }

                is HomeSearchItem.NoUserFoundItem -> {
                    NoUserFound(
                        searchQuery = item.searchQuery,
                        addCustomerInProgress = item.addCustomerInProgress,
                        onAddToAccountClicked = onAddToAccountClicked,
                    )
                }

                else -> {
                }
            }
        }
    }
}

@Composable
@Preview
fun HomeSearchUiPreview() {
    OkCreditTheme {
        HomeSearchUi(
            state = HomeSearchContract.State(
                addAccountInProgress = true,
                searchList = listOf(
                    HomeSearchItem.HeaderItem(HeaderType.CUSTOMER),
                    HomeSearchItem.CustomerItem(
                        customerId = "customerId",
                        name = "Customer Name",
                        balance = 1000L.paisa,
                        profileImage = null,
                        commonLedger = false,
                        isDefaulter = false,
                    ),
                    HomeSearchItem.HeaderItem(HeaderType.SUPPLIER),
                    HomeSearchItem.SupplierItem(
                        supplierId = "supplierId",
                        name = "Supplier Name",
                        balance = 1000L.paisa,
                        profileImage = null,
                        commonLedger = false,
                    ),
                    HomeSearchItem.HeaderItem(HeaderType.CONTACT),
                    HomeSearchItem.ContactItem(
                        contactId = "contactId",
                        name = "Contact Name",
                        phoneNumber = "1234567890",
                        profileImage = null,
                    ),
                    HomeSearchItem.HeaderItem(HeaderType.RECENT_SEARCH),
                    HomeSearchItem.NoUserFoundItem(
                        searchQuery = "dqwdqw",
                        addCustomerInProgress = false,
                    ),
                ),
            ),
            onBackPress = {},
            onCustomerItemClicked = {},
            onCustomerProfileClicked = {},
            onCustomerWhatsAppClicked = {},
            onSupplierItemClicked = {},
            onSupplierProfileClicked = {},
            onContactItemClicked = {},
            onSearchQueryChange = {},
            onAddToAccountClicked = {},
        )
    }
}
