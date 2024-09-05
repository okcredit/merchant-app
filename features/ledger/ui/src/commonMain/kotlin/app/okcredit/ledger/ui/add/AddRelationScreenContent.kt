@file:OptIn(ExperimentalMaterial3Api::class)

package app.okcredit.ledger.ui.add

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.okcredit.ledger.contract.model.AccountType
import app.okcredit.ui.Res
import app.okcredit.ui.components.BoxInputField
import app.okcredit.ui.components.PrimaryButton
import app.okcredit.ui.components.TwinBottomButtons
import app.okcredit.ui.icon_account
import app.okcredit.ui.icon_phone
import org.jetbrains.compose.resources.painterResource

@Composable
fun AddRelationScreenUi(
    state: AddRelationContract.State,
    accountType: AccountType,
    onBackClicked: () -> Unit,
    onSubmitted: (String, String) -> Unit,
    onViewLedgerClicked: (String, AccountType) -> Unit,
    onDismissDialog: () -> Unit,
) {
    Scaffold(
        topBar = { AddRelationTopBar(onBackClicked) },
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        AddRelationContent(modifier = Modifier.padding(it), state, onSubmitted)
    }

    if (state.errorDialog != null) {
        when (state.errorDialog) {
            is AddRelationContract.ErrorDialog.CyclicAccountError -> {
                CyclicAccountErrorDialog(
                    accountId = state.errorDialog.accountId,
                    accountType = accountType,
                    onDismissRequest = onDismissDialog,
                    onViewLedgerClicked = onViewLedgerClicked
                )
            }

            is AddRelationContract.ErrorDialog.MobileConflictError -> {
                MobileConflictErrorDialog(
                    accountId = state.errorDialog.accountId,
                    accountType = accountType,
                    onDismissRequest = onDismissDialog,
                    onViewLedgerClicked = onViewLedgerClicked
                )
            }
        }
    }
}

@Composable
fun MobileConflictErrorDialog(
    accountId: String,
    accountType: AccountType,
    onDismissRequest: () -> Unit,
    onViewLedgerClicked: (String, AccountType) -> Unit
) {
    ModalBottomSheet(onDismissRequest = onDismissRequest) {
        Column {
            Text(
                text = if (accountType == AccountType.CUSTOMER) {
                    "Cannot add customer"
                } else {
                    "Cannot add supplier"
                },
                style = MaterialTheme.typography.titleMedium,
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "This mobile number is already added as a ${if (accountType == AccountType.CUSTOMER) "customer" else "supplier"}",
                style = MaterialTheme.typography.bodyMedium,
            )
            Spacer(modifier = Modifier.height(16.dp))
            TwinBottomButtons(
                primaryText = "View Ledger",
                secondaryText = "Cancel",
                onPrimaryClick = { onViewLedgerClicked(accountId, accountType) },
                onSecondaryClick = onDismissRequest,
            )
        }
    }
}

@Composable
fun CyclicAccountErrorDialog(
    accountId: String,
    accountType: AccountType,
    onDismissRequest: () -> Unit,
    onViewLedgerClicked: (String, AccountType) -> Unit
) {
    ModalBottomSheet(onDismissRequest = onDismissRequest) {
        Column {
            Text(
                text = if (accountType == AccountType.CUSTOMER) {
                    "Cannot add customer"
                } else {
                    "Cannot add supplier"
                },
                style = MaterialTheme.typography.titleMedium,
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "This account is already added as a ${if (accountType == AccountType.CUSTOMER) "supplier" else "customer"}",
                style = MaterialTheme.typography.bodyMedium,
            )
            Spacer(modifier = Modifier.height(16.dp))
            TwinBottomButtons(
                primaryText = "View Ledger",
                secondaryText = "Cancel",
                onPrimaryClick = {
                    onViewLedgerClicked(
                        accountId,
                        if (accountType == AccountType.CUSTOMER) AccountType.SUPPLIER else AccountType.CUSTOMER
                    )
                },
                onSecondaryClick = onDismissRequest,
            )
        }
    }
}

@Composable
fun AddRelationTopBar(onBackClicked: () -> Unit) {
    TopAppBar(
        title = { Text(text = "Add Account") },
        navigationIcon = {
            IconButton(onClick = onBackClicked) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
            }
        }
    )
}

@Composable
fun AddRelationContent(
    modifier: Modifier,
    state: AddRelationContract.State,
    onSubmitted: (String, String) -> Unit,
) {
    var name by remember { mutableStateOf("") }
    var mobile by remember { mutableStateOf<String?>(null) }
    Column(modifier = modifier.fillMaxSize()) {
        BoxInputField(
            value = name,
            onValueChange = {
                name = it
            },
            label = "Name",
            leadingIcon = painterResource(Res.drawable.icon_account),
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
        )

        BoxInputField(
            value = mobile ?: "",
            onValueChange = {
                mobile = it
            },
            label = "Mobile (Optional)",
            leadingIcon = painterResource(Res.drawable.icon_phone),
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
        )
        Spacer(modifier = Modifier.weight(1f))
        HorizontalDivider()
        PrimaryButton(
            text = if (name.isEmpty()) {
                "Enter Name"
            } else if (state.loading) {
                "Please wait.."
            } else {
                "Confirm"
            },
            enabled = name.isNotBlank() && (mobile.isNullOrBlank() || mobile!!.length == 10) && !state.loading,
            onClick = { onSubmitted(name, mobile ?: "") },
            modifier = Modifier.fillMaxWidth().padding(all = 16.dp),
        )
    }
}
