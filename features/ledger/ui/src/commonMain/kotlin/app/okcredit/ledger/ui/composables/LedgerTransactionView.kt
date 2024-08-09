package app.okcredit.ledger.ui.composables

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Brush.Companion.verticalGradient
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.okcredit.ledger.ui.advance
import app.okcredit.ledger.ui.credit_deleted
import app.okcredit.ledger.ui.credit_failed
import app.okcredit.ledger.ui.due
import app.okcredit.ledger.ui.model.AccountType
import app.okcredit.ledger.ui.payment_deleted
import app.okcredit.ledger.ui.payment_failed
import app.okcredit.ledger.ui.placeholder_bill_images
import app.okcredit.ledger.ui.transaction_share
import app.okcredit.ui.Res
import app.okcredit.ui.icon_chevron_right
import app.okcredit.ui.icon_credit_up
import app.okcredit.ui.icon_delete
import app.okcredit.ui.icon_payment_down
import app.okcredit.ui.icon_pdf
import app.okcredit.ui.icon_refresh_outline
import app.okcredit.ui.icon_share
import app.okcredit.ui.icon_single_check
import app.okcredit.ui.icon_sync
import app.okcredit.ui.icon_sync_problem
import coil3.compose.LocalPlatformContext
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import io.ktor.client.network.sockets.SocketTimeoutException
import okcredit.base.utils.toFormattedAmount
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

data class TransactionViewState(
    val txnId: String,
    val relationshipId: String,
    val createdBySelf: Boolean = true,
    val imageCount: Int = 0,
    val image: String? = "",
    val isDiscountTransaction: Boolean = false,
    val txnGravity: TxnGravity,
    val closingBalance: Long,
    val amount: Long,
    val date: String,
    val isDirty: Boolean,
    val txnTag: String?,
    val note: String?,
    val txnType: UiTxnStatus = UiTxnStatus.Transaction,
    val collectionId: String?,
    val billId: String? = null,
    val accountType: AccountType = AccountType.Customer,
)

enum class TxnGravity {
    LEFT, RIGHT
}

sealed class UiTxnStatus {
    data object Transaction : UiTxnStatus()

    data class DeletedTransaction(
        val isDeletedByCustomer: Boolean,
    ) : UiTxnStatus()

    data class ProcessingTransaction(
        val action: ProcessingTransactionAction,
    ) : UiTxnStatus()

    enum class ProcessingTransactionAction {
        NONE,
        ADD_BANK,
        HELP,
        KYC,
    }
}


@Composable
fun LedgerTransactionView(
    modifier: Modifier,
    item: TransactionViewState,
    isLastItem: Boolean,
    openBill: (String) -> Unit,
    onTransactionClicked: (String, Long, Boolean) -> Unit,
    trackOnRetryClicked: (String, String) -> Unit,
    trackReceiptLoadFailed: (String, String) -> Unit,
    trackNoInternetError: (String, String) -> Unit,
    onOnlineTransactionClicked: (String) -> Unit,
    onTransactionShareButtonClicked: (String) -> Unit,
) {
    TransactionView(
        modifier = modifier,
        item = item,
        isLastItem = isLastItem,
        onTransactionClicked = onTransactionClicked,
        trackOnRetryClicked = trackOnRetryClicked,
        trackReceiptLoadFailed = trackReceiptLoadFailed,
        trackNoInternetError = trackNoInternetError,
        onOnlineTransactionClicked = onOnlineTransactionClicked,
        onTransactionShareButtonClicked = onTransactionShareButtonClicked,
        openBill = openBill,
    )
}

@Composable
fun TransactionView(
    modifier: Modifier,
    item: TransactionViewState,
    isLastItem: Boolean,
    openBill: (String) -> Unit,
    onTransactionClicked: (String, Long, Boolean) -> Unit,
    trackOnRetryClicked: (String, String) -> Unit,
    trackReceiptLoadFailed: (String, String) -> Unit,
    trackNoInternetError: (String, String) -> Unit,
    onOnlineTransactionClicked: (String) -> Unit,
    onTransactionShareButtonClicked: (String) -> Unit
) {
    val isPayment = item.txnGravity == TxnGravity.LEFT

    Box(
        modifier = modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier
                .wrapContentWidth()
                .align(if (isPayment) Alignment.CenterStart else Alignment.CenterEnd),
        ) {
            Card(
                modifier = Modifier
                    .wrapContentWidth()
                    .border(
                        1.dp,
                        MaterialTheme.colorScheme.outlineVariant,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .clickable {
                        val collectionId = item.collectionId
                        if (!collectionId.isNullOrEmpty()) {
                            onOnlineTransactionClicked(item.collectionId)
                        } else {
                            onTransactionClicked(
                                item.txnId,
                                item.closingBalance,
                                item.isDiscountTransaction
                            )
                        }
                    },
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.elevatedCardElevation(defaultElevation = 0.dp),
            ) {
                Column(
                    modifier = Modifier
                        .widthIn(max = 240.dp)
                        .background(
                            brush = if (!item.txnTag.isNullOrEmpty()) {
                                verticalGradient(
                                    getHeightOfGreyBackground(item) to MaterialTheme.colorScheme.outlineVariant,
                                    0.1f to Color.White,
                                    1.0f to Color.White
                                )
                            } else {
                                Brush.linearGradient(
                                    1.0f to Color.White,
                                    1f to Color.White
                                )
                            }
                        )
                ) {
                    TransactionTag(
                        transactionTag = item.txnTag,
                        modifier = Modifier,
                    )
                    TransactionAmountStrip(
                        transactionAmount = item.amount.toFormattedAmount(true),
                        transactionTextColor = getTransactionTextColor(
                            item.amount,
                            isPayment,
                            item.accountType
                        ),
                        transactionDate = item.date,
                        arrowIcon = {
                            GetTransactionArrows(
                                item.amount,
                                isPayment,
                                item.accountType,
                                item.txnType is UiTxnStatus.DeletedTransaction
                            )
                        },
                        collectionId = item.collectionId ?: "",
                        isDirty = item.isDirty,
                        createdBySelf = item.createdBySelf,
                        isDiscountTransaction = item.isDiscountTransaction,
                        isDeletedTransaction = item.txnType is UiTxnStatus.DeletedTransaction,
                        isOnlineTxn = !item.collectionId.isNullOrEmpty(),
                        isPayment = isPayment,
                        isTransactionImageAdded = item.imageCount > 0,
                        supplierLedger = item.accountType.isSupplier()
                    )
                    TransactionBillDetails(
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        billNumber = item.note ?: "",
                        billId = item.billId ?: "",
                        openBill = openBill
                    )
                    if (item.txnType !is UiTxnStatus.DeletedTransaction) {
                        TransactionBillImages(
                            image = item.image,
                            imageCount = item.imageCount,
                            trackOnRetryClicked = trackOnRetryClicked,
                            modifier = Modifier.align(Alignment.CenterHorizontally),
                            trackNoInternetError = trackNoInternetError,
                            trackReceiptLoadFailed = trackReceiptLoadFailed,
                            txnId = item.txnId,
                        )
                    }
                    if (item.txnType !is UiTxnStatus.DeletedTransaction && item.txnType !is UiTxnStatus.ProcessingTransaction) {
                        TransactionNote(
                            note = item.note,
                            billId = item.billId ?: ""
                        )
                    }
                }
            }
            if (item.txnType !is UiTxnStatus.DeletedTransaction && item.txnType !is UiTxnStatus.ProcessingTransaction) {
                TransactionBottomStrip(
                    totalAmount = item.closingBalance,
                    modifier = Modifier.align(if (isPayment) Alignment.Start else Alignment.End)
                )
                TransactionShareButton(
                    gravity = item.txnGravity,
                    onShareClicked = { onTransactionShareButtonClicked(item.txnId) },
                    isLastItem = isLastItem,
                    modifier = Modifier.align(if (isPayment) Alignment.End else Alignment.Start)
                )
            }
        }
    }
}

fun getHeightOfGreyBackground(item: TransactionViewState): Float {
    return when {
        item.imageCount > 0 && !item.note.isNullOrEmpty() -> 0.1f
        item.imageCount > 0 -> 0.1f
        !item.note.isNullOrEmpty() -> 0.25f
        else -> 0.33f
    }
}

@Composable
fun TransactionShareButton(
    onShareClicked: () -> Unit,
    gravity: TxnGravity,
    isLastItem: Boolean,
    modifier: Modifier,
) {
    if (isLastItem.not()) return

    Row(
        modifier = modifier
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(20.dp)
            )
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant,
                shape = RoundedCornerShape(20.dp)
            ),
        horizontalArrangement = when (gravity) {
            TxnGravity.LEFT -> Arrangement.End
            TxnGravity.RIGHT -> Arrangement.Start
        }
    ) {
        Row(
            modifier = Modifier
                .clickable {
                    onShareClicked()
                }
                .padding(vertical = 10.dp, horizontal = 8.dp)
                .defaultMinSize(minWidth = ButtonDefaults.MinWidth),
        ) {
            Image(
                painter = painterResource(app.okcredit.ui.Res.drawable.icon_share),
                contentDescription = "Share",
                modifier = Modifier.padding(end = 6.dp),
                colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.primary)
            )
            Text(
                text = stringResource(app.okcredit.ledger.ui.Res.string.transaction_share),
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                color = MaterialTheme.colorScheme.onSurface,
            )
        }
    }
}

@Composable
fun TransactionBottomStrip(
    totalAmount: Long,
    modifier: Modifier,
) {
    Row(
        modifier = modifier.padding(top = 3.dp, bottom = 4.dp, end = 4.dp, start = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = when {
                (totalAmount * 100).toInt() == 0 -> {
                    "₹0 ${stringResource(app.okcredit.ledger.ui.Res.string.due)}"
                }

                totalAmount > 0 -> {
                    "${
                        totalAmount.toFormattedAmount(true)
                    } ${stringResource(app.okcredit.ledger.ui.Res.string.due)}"
                }

                else -> {
                    "${
                        totalAmount.toFormattedAmount(true)
                    } ${stringResource(app.okcredit.ledger.ui.Res.string.advance)}"
                }
            },
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
fun TransactionNote(
    note: String?,
    billId: String,
) {
    if (!note.isNullOrEmpty() && billId.isBlank()) {
        Text(
            text = note,
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.padding(top = 6.dp, end = 10.dp, start = 12.dp, bottom = 8.dp),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            softWrap = true,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
fun TransactionBillImages(
    image: String?,
    imageCount: Int,
    trackOnRetryClicked: (String, String) -> Unit,
    modifier: Modifier,
    trackNoInternetError: (String, String) -> Unit,
    trackReceiptLoadFailed: (String, String) -> Unit,
    txnId: String
) {
    if (image.isNullOrEmpty()) return
    var showRetry by remember { mutableStateOf(false) }
    var showLoader by remember { mutableStateOf(true) }
    var loadKey by remember { mutableIntStateOf(1) }

    Box(
        modifier = modifier
            .padding(4.dp)
            .wrapContentWidth()
            .wrapContentHeight()
    ) {
        val painter = rememberAsyncImagePainter(
            model = ImageRequest.Builder(LocalPlatformContext.current)
                .data(if (loadKey > 0) image else null).apply(
                    block = fun ImageRequest.Builder.() {
                        // transformations(RoundedCornersTransformation(12f))
                    },
                ).listener(
                    onError = { _, error ->
                        showRetry = true
                        showLoader = false
                        if (error.throwable is SocketTimeoutException) {
                            trackNoInternetError(txnId, image)
                        } else {
                            trackReceiptLoadFailed(txnId, image)
                        }
                    },
                    onSuccess = { _, _ ->
                        showRetry = false
                        showLoader = false
                    },
                ).build(),
            placeholder = painterResource(app.okcredit.ledger.ui.Res.drawable.placeholder_bill_images),
            error = painterResource(app.okcredit.ledger.ui.Res.drawable.placeholder_bill_images)
        )

        Image(
            painter = painter,
            contentDescription = "Transaction Bill",
            modifier = Modifier
                .widthIn(max = 240.dp)
                .heightIn(max = 180.dp),
            contentScale = ContentScale.Crop
        )
        if (imageCount > 1) {
            Card(
                shape = RoundedCornerShape(4.dp),
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
                    .size(32.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                ),
            ) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        textAlign = TextAlign.Center,
                        text = "+$imageCount",
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
                        color = MaterialTheme.colorScheme.surface,
                    )
                }
            }
        }

        if (showRetry) {
            RetryButton(
                onRetryClicked = {
                    showLoader = true
                    loadKey++
                    trackOnRetryClicked(txnId, image)
                },
                modifier = Modifier.align(Alignment.Center),
            )
        } else if (showLoader) {
            TxnBillLoader(modifier = Modifier.align(Alignment.Center))
        }
    }
}

@Composable
fun TxnBillLoader(
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "infinite_txns")
    val angle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 600, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation_animation"
    )

    Image(
        painter = painterResource(Res.drawable.icon_refresh_outline),
        contentDescription = "Loader",
        modifier = modifier
            .rotate(angle)
            .size(40.dp)
    )
}

@Composable
fun RetryButton(
    onRetryClicked: () -> Unit,
    modifier: Modifier
) {
    Button(
        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.onSurface),
        onClick = { onRetryClicked() },
        modifier = modifier.padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(20.dp),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        elevation = ButtonDefaults.elevatedButtonElevation(defaultElevation = 0.dp),
    ) {
        Icon(
            painter = painterResource(app.okcredit.ui.Res.drawable.icon_sync),
            contentDescription = "Retry",
            modifier = Modifier.padding(end = 8.dp),
            tint = Color.White
        )
        Text(
            text = "Retry",
            color = Color.White,
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.SemiBold
            )
        )
    }
}

@Composable
fun TransactionAmountStrip(
    modifier: Modifier = Modifier,
    supplierLedger: Boolean,
    transactionAmount: String,
    transactionTextColor: Color,
    transactionDate: String,
    arrowIcon: @Composable () -> Unit,
    collectionId: String,
    isDirty: Boolean,
    createdBySelf: Boolean,
    isDiscountTransaction: Boolean,
    isDeletedTransaction: Boolean,
    isOnlineTxn: Boolean,
    isPayment: Boolean,
    isTransactionImageAdded: Boolean,
) {
    val findWidth = if (isTransactionImageAdded) {
        modifier.widthIn(min = 240.dp, max = 240.dp)
    } else {
        modifier.widthIn(max = 240.dp)
    }
    Row(
        modifier = findWidth
            .padding(horizontal = 12.dp, vertical = 10.dp)
            .background(color = Color.Transparent),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            if (isDeletedTransaction) {
                DeletedTransactionUi(
                    isOnlineTxn = isOnlineTxn,
                    isPayment = isPayment,
                    supplierLedger = supplierLedger
                )
            }
            TransactionArrow(
                arrowIcon = arrowIcon,
                isDiscountTransaction = isDiscountTransaction,
            )
            TransactionAmount(
                transactionAmount = transactionAmount,
                transactionTextColor = transactionTextColor,
                isDiscountTransaction = isDiscountTransaction,
                isDeletedTransaction = isDeletedTransaction,
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
            TransactionDate(transactionDate, Modifier.align(Alignment.CenterVertically))
            TransactionSyncStatus(
                collectionId = collectionId,
                isDirty = isDirty,
                isCreatedBySelf = createdBySelf
            )
        }
    }
}

@Composable
fun DeletedTransactionUi(
    isOnlineTxn: Boolean,
    isPayment: Boolean,
    supplierLedger: Boolean,
) {
    Icon(
        modifier = Modifier.size(18.dp),
        painter = painterResource(app.okcredit.ui.Res.drawable.icon_delete),
        contentDescription = "icon_delete",
        tint = MaterialTheme.colorScheme.outlineVariant
    )
    Spacer(modifier = Modifier.padding(end = 5.dp))
    Text(
        text = stringResource(
            getDeletedSuffixText(
                isOnlineTxn = isOnlineTxn,
                isPayment = isPayment,
                supplierLedger = supplierLedger
            )
        ),
        style = MaterialTheme.typography.labelSmall,
        modifier = Modifier.padding(end = 2.dp)
    )
}

@Composable
fun TransactionDate(transactionDate: String, modifier: Modifier) {
    Text(
        text = transactionDate,
        style = MaterialTheme.typography.labelMedium,
        modifier = modifier,
        color = MaterialTheme.colorScheme.outlineVariant,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )
}

@Composable
fun TransactionAmount(
    transactionAmount: String,
    transactionTextColor: Color,
    isDiscountTransaction: Boolean,
    isDeletedTransaction: Boolean,
) {
    Text(
        text = transactionAmount,
        style = MaterialTheme.typography.headlineSmall.copy(
            textDecoration = if (isDeletedTransaction) {
                TextDecoration.LineThrough
            } else {
                TextDecoration.None
            },
            fontSize = if (isDeletedTransaction) 13.sp else 20.sp
        ),
        modifier = Modifier.padding(end = 6.dp),
        color = if (isDiscountTransaction) MaterialTheme.colorScheme.onSurface
        else if (isDeletedTransaction) MaterialTheme.colorScheme.outlineVariant
        else transactionTextColor,
    )
}

fun getDeletedSuffixText(
    isOnlineTxn: Boolean,
    isPayment: Boolean,
    supplierLedger: Boolean
): StringResource {
    return if (supplierLedger) {
        if (isOnlineTxn) {
            if (isPayment) app.okcredit.ledger.ui.Res.string.credit_failed else app.okcredit.ledger.ui.Res.string.payment_failed
        } else {
            if (isPayment) app.okcredit.ledger.ui.Res.string.credit_deleted else app.okcredit.ledger.ui.Res.string.payment_deleted
        }
    } else {
        if (isOnlineTxn) {
            if (isPayment) app.okcredit.ledger.ui.Res.string.payment_failed else app.okcredit.ledger.ui.Res.string.credit_failed
        } else {
            if (isPayment) app.okcredit.ledger.ui.Res.string.payment_deleted else app.okcredit.ledger.ui.Res.string.credit_deleted
        }
    }
}

@Composable
fun TransactionArrow(
    arrowIcon: @Composable () -> Unit,
    isDiscountTransaction: Boolean,
) {
    if (isDiscountTransaction.not()) {
        arrowIcon()
    }
}

@Composable
fun TransactionSyncStatus(
    collectionId: String,
    isDirty: Boolean,
    isCreatedBySelf: Boolean,
) {
    val iconRes = when {
        collectionId.isEmpty().not() && isDirty -> Res.drawable.icon_sync

        isCreatedBySelf -> if (isDirty) {
            Res.drawable.icon_sync_problem
        } else {
            Res.drawable.icon_single_check
        }

        else -> null
    }

    iconRes?.let {
        Icon(
            painter = painterResource(it),
            contentDescription = "Sync Status",
            modifier = Modifier
                .padding(start = 4.dp)
                .size(16.dp),
            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = if (collectionId.isNotEmpty()) 0.6f else 0.7f)
        )
    }
}

@Composable
fun getTransactionTextColor(amount: Long, isPayment: Boolean, accountType: AccountType): Color {
    var color = if (accountType.isSupplier()) {
        MaterialTheme.colorScheme.error
    } else {
        MaterialTheme.colorScheme.primary
    }
    if (amount < 0L || !isPayment) {
        color = if (accountType.isSupplier()) {
            MaterialTheme.colorScheme.primary
        } else {
            MaterialTheme.colorScheme.error
        }
    }
    return color
}

@Composable
fun GetTransactionArrows(
    amount: Long,
    isPayment: Boolean,
    accountType: AccountType,
    isDeletedTransaction: Boolean
) {
    if (accountType.isSupplier()) {
        if (amount < 0L || !isPayment) {
            Icon(
                painter = painterResource(Res.drawable.icon_credit_up),
                contentDescription = "transaction_arrow",
                modifier = Modifier.padding(end = if (isDeletedTransaction) 0.dp else 4.dp),
                tint = if (isDeletedTransaction) MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f) else MaterialTheme.colorScheme.primary
            )
        } else {
            Icon(
                painter = painterResource(Res.drawable.icon_payment_down),
                contentDescription = "transaction_arrow",
                modifier = Modifier.padding(end = if (isDeletedTransaction) 0.dp else 4.dp),
                tint = if (isDeletedTransaction) MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f) else MaterialTheme.colorScheme.error
            )
        }
    } else {
        if (amount < 0L || !isPayment) {
            Icon(
                painter = painterResource(Res.drawable.icon_credit_up),
                contentDescription = "transaction_arrow",
                modifier = Modifier.padding(end = if (isDeletedTransaction) 0.dp else 4.dp),
                tint = if (isDeletedTransaction) MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f) else MaterialTheme.colorScheme.error
            )
        } else {
            Icon(
                painter = painterResource(Res.drawable.icon_payment_down),
                contentDescription = "transaction_arrow",
                modifier = Modifier.padding(end = if (isDeletedTransaction) 0.dp else 4.dp),
                tint = if (isDeletedTransaction) MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f) else MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
fun TransactionTag(
    transactionTag: String?,
    modifier: Modifier = Modifier
) {
    if (!transactionTag.isNullOrEmpty()) {
        Row(
            modifier = modifier
        ) {
            Text(
                text = transactionTag,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .wrapContentWidth()
                    .padding(start = 16.dp, top = 4.dp, end = 16.dp, bottom = 4.dp),
                style = MaterialTheme.typography.labelMedium.copy(
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    fontWeight = FontWeight.SemiBold
                ),
            )
        }
    }
}

@Composable
fun TransactionBillDetails(
    modifier: Modifier = Modifier,
    billNumber: String,
    billId: String,
    openBill: (String) -> Unit,
) {
    if (billId.isBlank()) {
        return
    }
    Column(
        modifier = modifier
            .padding(horizontal = 12.dp)
            .clickable { openBill(billId) }
    ) {
        HorizontalDivider()
        Row(
            modifier = Modifier
                .padding(vertical = 12.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier
            ) {
                Icon(
                    painter = painterResource(Res.drawable.icon_pdf),
                    contentDescription = "Pdf icon",
                    modifier = Modifier,
                    tint = Color.Unspecified,
                )
                Column(
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Text(
                        text = "View Bill Details",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                        modifier = Modifier,
                    )
                    Text(
                        text = billNumber,
                        style = MaterialTheme.typography.labelMedium.copy(color = MaterialTheme.colorScheme.outlineVariant),
                        modifier = Modifier,
                    )
                }
            }

            Icon(
                painter = painterResource(app.okcredit.ui.Res.drawable.icon_chevron_right),
                contentDescription = "Chevron icon",
                modifier = Modifier,
            )
        }
    }
}

@Preview
@Composable
fun TransactionItemPreview() {
    val sampleTxnItem = TransactionViewState(
        txnId = "1",
        amount = 1000L,
        date = "16 Jan 2024",
        txnGravity = TxnGravity.LEFT,
        closingBalance = 1000L,
        txnTag = "",
        note = "Note",
        isDirty = false,
        txnType = UiTxnStatus.Transaction,
        relationshipId = "1",
        collectionId = "1",
        billId = "1",
        imageCount = 2,
        image = "https://www.google.com/images/branding/googlelogo/1x/googlelogo_color_272x92dp.png"
    )
    LedgerTransactionView(
        item = sampleTxnItem,
        isLastItem = true,
        onTransactionClicked = { _, _, _ -> },
        trackOnRetryClicked = { _, _ -> },
        trackReceiptLoadFailed = { _, _ -> },
        trackNoInternetError = { _, _ -> },
        onOnlineTransactionClicked = {},
        onTransactionShareButtonClicked = {},
        modifier = Modifier,
        openBill = {},
    )
}

@Preview
@Composable
fun TransactionBillDetailsPreview() {
    TransactionBillDetails(billNumber = "BILL-54", billId = "abcd", openBill = {})
}
