package app.okcredit.ledger.ui.delete.composable

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.okcredit.ledger.ui.Res
import app.okcredit.ledger.ui.delete_customer
import app.okcredit.ledger.ui.delete_supplier
import app.okcredit.ui.composable.ArrowBack
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun DeleteRelationshipScreen(
    name: String,
    balance: Long,
    mobile: String,
    isSupplier: Boolean,
    onDeleteClicked: () -> Unit,
    onSettlementClicked: () -> Unit,
    isLoading: Boolean,
    shouldSettle: Boolean,
    onBackClicked: () -> Unit,
    loadDetails: () -> Unit
) {
    LaunchedEffect(true) {
        loadDetails()
    }
    Scaffold(

        topBar = {
            DeleteRelationshipToolbar(
                isSupplier = isSupplier,
                onBackClicked = onBackClicked
            )
        }
    ) { contentPadding ->
        DeleteRelationshipContent(
            isLoading = isLoading,
            contentPadding = contentPadding,
            name = name,
            mobile = mobile,
            balance = balance,
            shouldSettle = shouldSettle,
            onDeleteClicked = onDeleteClicked,
            onSettlementClicked = onSettlementClicked,
            isSupplier = isSupplier
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeleteRelationshipToolbar(
    isSupplier: Boolean,
    onBackClicked: () -> Unit,
) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        title = {
            Row {
                ArrowBack(onBackClicked)
                Spacer(modifier = Modifier.padding(6.dp))
                Text(
                    text = if (isSupplier) {
                        stringResource(Res.string.delete_supplier)
                    } else {
                        stringResource(Res.string.delete_customer)
                    }
                )
            }
        }
    )
}

@Preview
@Composable
fun DeleteCustomerScreenPreview() {
    DeleteRelationshipScreen(
        name = "John Doe",
        balance = 1000,
        onDeleteClicked = {},
        onSettlementClicked = {},
        isLoading = false,
        shouldSettle = true,
        mobile = "1234567890",
        isSupplier = false,
        onBackClicked = {},
        loadDetails = {}
    )
}
