package app.okcredit.ledger.ui.statement.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp
import app.okcredit.ledger.contract.model.Transaction
import app.okcredit.ledger.ui.Res
import app.okcredit.ledger.ui.load_settled_entries
import app.okcredit.ledger.ui.statement.AccountStatementContract
import app.okcredit.ui.icon_download
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.math.roundToInt

@Composable
fun AccountStatementUI(
    state: AccountStatementContract.State,
    onShowDateRangePicker: () -> Unit,
    onScroll: () -> Unit,
    onClickBack: () -> Unit,
    onClickMenu: () -> Unit,
    onClickAllTransactionFromMenu: () -> Unit,
    onClickAllCollectionsFromMenu: () -> Unit,
    onClickDownload: () -> Unit,
    onClickLoadMore: () -> Unit,
    onClickTransaction: (transaction: Transaction) -> Unit,
    onLongClickTransaction: (transaction: Transaction) -> Unit,
) {
    val fabHeightPx = with(LocalDensity.current) { 72.dp.roundToPx().toFloat() }
    val fabOffsetHeightPx = remember { mutableStateOf(0f) }
    val nestedScrollConnection = remember {
        ScrollHandler(
            fabOffsetHeightPx = fabOffsetHeightPx,
            fabHeightPx = fabHeightPx,
            onScroll = onScroll
        )
    }

    Scaffold(
        modifier = Modifier
            .nestedScroll(nestedScrollConnection),
        topBar = {
            Toolbar(
                state = state,
                onBackClick = onClickBack,
                onClickMenu = onClickMenu,
                onClickAllTransactionFromMenu = onClickAllTransactionFromMenu,
                onClickAllCollectionsFromMenu = onClickAllCollectionsFromMenu
            )
        },
        floatingActionButton = {
            DownloadButton(
                state = state,
                fabOffsetHeightPx = fabOffsetHeightPx,
                onClick = onClickDownload
            )
        },
        floatingActionButtonPosition = FabPosition.Center,
    ) {
        AccountStatementList(
            state = state,
            fabOffsetHeightPx = fabOffsetHeightPx,
            onClickTransaction = onClickTransaction,
            onLongClickTransaction = onLongClickTransaction,
            onShowDateRangePicker = {
                onShowDateRangePicker.invoke()
            },
            onClickLoadMore = onClickLoadMore
        )
    }
}

@Composable
private fun AccountStatementList(
    fabOffsetHeightPx: MutableState<Float>,
    onClickTransaction: (transition: Transaction) -> Unit,
    onLongClickTransaction: (transition: Transaction) -> Unit,
    onClickLoadMore: () -> Unit,
    onShowDateRangePicker: () -> Unit,
    state: AccountStatementContract.State,
) {
    val onlineTxns =
        state.transactions.filter { transaction -> transaction.isOnlineTransaction }

    LazyColumn(modifier = Modifier.padding(vertical = 4.dp)) {
        item {
            Spacer(Modifier.height(16.dp))
        }

        if (!state.isOnlineTransactionSelected || onlineTxns.isNotEmpty()) {
            item {
                AccountStatementSummery(state = state)
            }
        }

        items(items = state.transactions) { transaction ->
            if (state.isOnlineTransactionSelected.not() || transaction.isOnlineTransaction) {
                TransactionItem(
                    transaction,
                    onClickTransaction = onClickTransaction,
                    onLongClickTransaction = onLongClickTransaction,
                )
            }
        }

        if (state.isShowOld) {
            item {
                LoadMoreButton(onClickLoadMore)
            }
        }

        if (state.transactions.isEmpty() || (state.isOnlineTransactionSelected && onlineTxns.isEmpty())) {
            item {
                EmptyView()
            }
        }

        item {
            Spacer(modifier = Modifier.height(72.dp))
        }
    }
    DateRangePicker(
        fabOffsetHeightPx = fabOffsetHeightPx,
        onClick = onShowDateRangePicker,
        state = state
    )
    if (state.isLoadingDownload || state.isShowDownloadAlert) {
        DownloadInfoBanner(state)
    }
}

@Composable
private fun EmptyView() {
    Box(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(0.dp, 50.dp, 0.dp, 0.dp)
        ) {
            Image(
                modifier = Modifier
                    .height(136.dp)
                    .width(200.dp),
                painter = painterResource(Res.drawable.account_statement_placeholder),
                contentDescription = ""
            )
            Text(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(16.dp),
                style = MaterialTheme.typography.bodyMedium,
                text = stringResource(Res.string.no_transactions)
            )
        }
    }
}

@Composable
private fun LoadMoreButton(onClickLoadMore: () -> Unit) {
    Column(Modifier.fillMaxWidth()) {
        OutlinedButton(
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = MaterialTheme.colorScheme.primary,
            ),
            onClick = onClickLoadMore,
            modifier = Modifier
                .padding(8.dp)
                .align(Alignment.CenterHorizontally)
        ) {
            Text(text = stringResource(Res.string.load_settled_entries))
        }
    }
}

@Composable
private fun DownloadButton(
    fabOffsetHeightPx: MutableState<Float>,
    onClick: () -> Unit,
    state: AccountStatementContract.State,
) {
    if (state.transactions.isNotEmpty() &&
        state.isLoading.not() &&
        state.isLoadingDownload.not() &&
        state.isOnlineTransactionSelected.not()
    ) {
        FloatingActionButton(
            onClick = onClick,
            modifier = Modifier
                .offset {
                    IntOffset(x = 0, y = -fabOffsetHeightPx.value.roundToInt())
                }
                .fillMaxWidth()
                .height(48.dp)
                .padding(16.dp, 0.dp, 16.dp, 0.dp),
            shape = CircleShape,
            containerColor = MaterialTheme.colorScheme.secondary
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    tint = MaterialTheme.colorScheme.surface,
                    painter = painterResource(app.okcredit.ui.Res.drawable.icon_download),
                    contentDescription = null
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = stringResource(Res.string.download),
                    style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.surface)
                )
            }
        }
    }
}

@Composable
fun DownloadInfoBanner(state: AccountStatementContract.State) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(0.dp, 0.dp, 0.dp, if (state.isLoadingDownload) 0.dp else 72.dp)
            .wrapContentSize(align = Alignment.BottomStart)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(if (state.isLoadingDownload) 72.dp else 36.dp)
                .align(Alignment.BottomCenter)
                .background(MaterialTheme.colorScheme.secondary),
        ) {
            Row(
                modifier = Modifier
                    .align(Alignment.Center)
            ) {
                if (state.isLoadingDownload) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .width(16.dp)
                            .height(16.dp)
                            .align(Alignment.CenterVertically),
                        strokeWidth = 1.dp
                    )
                }

                Text(
                    modifier = Modifier
                        .padding(2.dp, 0.dp, 0.dp, 0.dp)
                        .align(Alignment.CenterVertically),
                    text = if (state.isLoadingDownload) {
                        stringResource(Res.string.downloading_please_wait)
                    } else {
                        stringResource(
                            Res.string.download_complete
                        )
                    },
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        fontWeight = FontWeight.Bold
                    ),
                )
            }
        }
    }
}

class ScrollHandler(
    private val fabOffsetHeightPx: MutableState<Float>,
    private val fabHeightPx: Float,
    private val onScroll: () -> Unit,
) : NestedScrollConnection {
    override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
        fabOffsetHeightPx.value =
            (fabOffsetHeightPx.value + available.y).coerceIn(-fabHeightPx, 0f)

        return Offset.Zero
    }

    override suspend fun onPostFling(consumed: Velocity, available: Velocity): Velocity {
        onScroll.invoke()
        return super.onPostFling(consumed, available)
    }
}

@Preview
@Composable
fun DefaultPreview() {
    AccountStatementUI(
        state = AccountStatementContract.State(),
        onShowDateRangePicker = {},
        onClickBack = {},
        onClickMenu = {},
        onClickAllTransactionFromMenu = {},
        onClickAllCollectionsFromMenu = {},
        onClickDownload = {},
        onClickTransaction = {},
        onClickLoadMore = {},
        onScroll = {},
        onLongClickTransaction = {}
    )
}
