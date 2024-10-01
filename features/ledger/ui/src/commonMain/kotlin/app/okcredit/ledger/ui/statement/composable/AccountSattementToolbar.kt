package app.okcredit.ledger.ui.statement.composable

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import app.okcredit.ledger.ui.Res
import app.okcredit.ledger.ui.account_statement
import app.okcredit.ledger.ui.all_transactions
import app.okcredit.ledger.ui.online_transactions
import app.okcredit.ledger.ui.statement.AccountStatementContract
import app.okcredit.ui.composable.ArrowBack
import app.okcredit.ui.icon_collections
import app.okcredit.ui.icon_filter_list
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Toolbar(
    onBackClick: () -> Unit,
    onClickAllTransactionFromMenu: () -> Unit,
    onClickAllCollectionsFromMenu: () -> Unit,
    state: AccountStatementContract.State,
    onClickMenu: () -> Unit,
) {
    val expanded = remember { mutableStateOf(false) }

    TopAppBar(
        title = {
            Text(text = stringResource(Res.string.account_statement))
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        navigationIcon = {
            ArrowBack(
                onBackClicked = onBackClick,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
        },
        actions = {
            IconButton(
                onClick = {
                    onClickMenu.invoke()
                    expanded.value = true
                }
            ) {
                Icon(
                    painter = painterResource(app.okcredit.ui.Res.drawable.icon_filter_list),
                    contentDescription = null
                )
            }
            DropMenu(
                state = state,
                expanded = expanded,
                onClickAllTransactionFromMenu = onClickAllTransactionFromMenu,
                onClickAllCollectionsFromMenu = onClickAllCollectionsFromMenu
            )
        }
    )
}

@Composable
private fun DropMenu(
    expanded: MutableState<Boolean>,
    onClickAllTransactionFromMenu: () -> Unit,
    onClickAllCollectionsFromMenu: () -> Unit,
    state: AccountStatementContract.State,
) {
    DropdownMenu(
        expanded = expanded.value,
        offset = DpOffset((0).dp, (0).dp),
        modifier = Modifier
            .width(IntrinsicSize.Max)
            .padding(0.dp)
            .width(200.dp),
        onDismissRequest = { expanded.value = false }
    ) {
        MenuItem(
            isSelected = !state.isOnlineTransactionSelected,
            title = stringResource(Res.string.all_transactions),
            onClick = {
                expanded.value = false
                onClickAllTransactionFromMenu.invoke()
            },
            drawable = app.okcredit.ui.Res.drawable.icon_collections
        )

        MenuItem(
            isSelected = state.isOnlineTransactionSelected,
            title = stringResource(Res.string.online_transactions),
            onClick = {
                expanded.value = false
                onClickAllCollectionsFromMenu.invoke()
            },
            drawable = app.okcredit.ui.Res.drawable.icon_collections
        )
    }
}

@Composable
private fun MenuItem(
    isSelected: Boolean = false,
    title: String = "",
    onClick: () -> Unit,
    drawable: DrawableResource,
) {
    DropdownMenuItem(
        onClick = onClick,
        text = {
            Row(
                modifier = Modifier
                    .padding(0.dp)
            ) {
                Icon(
                    tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                    painter = painterResource(drawable),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .align(Alignment.CenterVertically)
                )
                Text(
                    text = title,
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth(),
                    style = MaterialTheme.typography.titleSmall.copy(
                        color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
                    )
                )
            }
        }
    )
}

@Preview
@Composable
fun ToolbarPreview() {
    Toolbar(
        onBackClick = {},
        onClickAllTransactionFromMenu = {},
        onClickAllCollectionsFromMenu = {},
        state = AccountStatementContract.State(),
        onClickMenu = {}
    )
}

@Preview
@Composable
fun AccountStatementSummeryPreview() {
    MenuItem(
        isSelected = true,
        title = "All Transactions",
        onClick = {},
        drawable = app.okcredit.ui.Res.drawable.icon_collections,
    )
}
