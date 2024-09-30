package app.okcredit.ledger.ui.supplier.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.okcredit.ui.icon_call
import app.okcredit.ui.icon_chevron_right
import app.okcredit.ui.icon_collections
import app.okcredit.ui.icon_credit_up
import app.okcredit.ui.icon_payment_down
import app.okcredit.ui.icon_statement
import okcredit.base.units.Paisa
import okcredit.base.units.formatPaisa
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun SupplierBottomUi(
    modifier: Modifier,
    ledgerNotEmpty: Boolean,
    closingBalance: Paisa,
    canShowPayOnline: Boolean,
    onPayOnlineClicked: () -> Unit,
    onShareReportClicked: () -> Unit,
    onCallClicked: () -> Unit,
    onGivenClicked: () -> Unit,
    onReceivedClicked: () -> Unit,
) {
    Column(
        modifier = modifier.background(color = MaterialTheme.colorScheme.surface),
    ) {
        if (ledgerNotEmpty) {
            BalanceDueContainer(
                closingBalance = closingBalance,
                shareReportClicked = onShareReportClicked,
                canShowPayOnline = canShowPayOnline,
                onPayOnlineClicked = onPayOnlineClicked,
                onCallClicked = onCallClicked,
            )
        }
        HorizontalDivider(
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.surface,
        )
        PaymentAndCreditCta(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp),
            onReceivedClicked = onReceivedClicked,
            onGivenClicked = onGivenClicked,
        )
    }
}

@Composable
fun BalanceDueContainer(
    closingBalance: Paisa,
    canShowPayOnline: Boolean,
    shareReportClicked: () -> Unit,
    onPayOnlineClicked: () -> Unit,
    onCallClicked: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color.Transparent),
    ) {
        ShareReportUi(
            modifier = Modifier,
            onCallClicked = onCallClicked,
            onPayOnlineClicked = onPayOnlineClicked,
            shareReportClicked = shareReportClicked,
            canShowPayOnline = canShowPayOnline,
        )
        BalanceUi(
            modifier = Modifier,
            isBalanceAdvance = closingBalance > Paisa.ZERO,
            balance = formatPaisa(closingBalance.value, true),
            onSupplierReportClicked = shareReportClicked,
        )
    }
}

@Composable
fun ShareReportUi(
    modifier: Modifier,
    onCallClicked: () -> Unit,
    onPayOnlineClicked: () -> Unit,
    shareReportClicked: () -> Unit,
    canShowPayOnline: Boolean,
) {
    Column(
        modifier = Modifier
            .background(
                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
                shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp),
            ),
    ) {
        Spacer(Modifier.height(4.dp))
        Row(
            modifier = modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Row(
                modifier = modifier
                    .weight(0.6f)
                    .clickable { shareReportClicked() },
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Spacer(modifier = Modifier.width(12.dp))
                Icon(
                    painter = painterResource(app.okcredit.ui.Res.drawable.icon_statement),
                    contentDescription = "Date icon",
                    modifier = Modifier
                        .width(16.dp)
                        .height(16.dp),
                    tint = MaterialTheme.colorScheme.primary,
                )
                Spacer(modifier = Modifier.width(2.dp))
                Text(
                    modifier = Modifier.padding(vertical = 8.dp),
                    text = "Share Report",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                    ),
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.primary,
                )
                Icon(
                    modifier = Modifier
                        .size(18.dp)
                        .padding(top = 2.dp),
                    painter = painterResource(app.okcredit.ui.Res.drawable.icon_chevron_right),
                    contentDescription = "arrow_right",
                    tint = MaterialTheme.colorScheme.primary,
                )
            }
            Row(
                modifier = Modifier.weight(1f),
            ) {
                Button(
                    onClick = { onCallClicked() },
                    shape = CircleShape,
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    modifier = Modifier
                        .weight(if (canShowPayOnline) 0.65f else 1f)
                        .padding(end = 8.dp),
                    elevation = ButtonDefaults.elevatedButtonElevation(defaultElevation = 0.dp),
                ) {
                    Icon(
                        painter = painterResource(app.okcredit.ui.Res.drawable.icon_call),
                        contentDescription = "Smart collect icon",
                        modifier = Modifier.height(20.dp),
                        tint = MaterialTheme.colorScheme.surface,
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = "Call",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                        ),
                        color = MaterialTheme.colorScheme.surface,
                        fontSize = 12.sp,
                        maxLines = 1,
                    )
                }
                if (canShowPayOnline) {
                    Button(
                        onClick = { onPayOnlineClicked() },
                        shape = CircleShape,
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 8.dp),
                        elevation = ButtonDefaults.elevatedButtonElevation(defaultElevation = 0.dp),
                    ) {
                        Icon(
                            painter = painterResource(app.okcredit.ui.Res.drawable.icon_collections),
                            contentDescription = "Smart collect icon",
                            modifier = Modifier.height(20.dp),
                            tint = MaterialTheme.colorScheme.surface,
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = "Pay Online",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.SemiBold,
                            ),
                            color = MaterialTheme.colorScheme.primary,
                            maxLines = 1,
                            fontSize = 12.sp,
                        )
                    }
                }
            }
        }
        Spacer(Modifier.height(4.dp))
    }
}

@Composable
fun BalanceUi(
    modifier: Modifier,
    isBalanceAdvance: Boolean,
    balance: String,
    onSupplierReportClicked: () -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .clickable { onSupplierReportClicked() },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Spacer(Modifier.width(16.dp))

        Text(
            text = if (!isBalanceAdvance) "Balance Due" else "Balance Advance",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 13.sp,
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.padding(vertical = 10.dp),
        )

        Spacer(Modifier.weight(1f))
        Row(
            modifier = Modifier,
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = balance,
                fontSize = 16.sp,
                color = if (isBalanceAdvance) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.titleMedium,
            )
            Icon(
                painter = painterResource(app.okcredit.ui.Res.drawable.icon_chevron_right),
                contentDescription = "arrow_right",
                tint = if (isBalanceAdvance) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
            )
        }
        Spacer(
            Modifier
                .width(16.dp),
        )
    }
}

@Composable
fun PaymentAndCreditCta(
    modifier: Modifier,
    onReceivedClicked: () -> Unit,
    onGivenClicked: () -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .semantics { contentDescription = "payment_buttons" }
            .testTag("payment_buttons"),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Button(
            modifier = Modifier
                .weight(1f)
                .semantics { contentDescription = "received_button" }
                .testTag("received_button"),
            onClick = { onReceivedClicked() },
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surface),
            shape = CircleShape,
            elevation = ButtonDefaults.elevatedButtonElevation(defaultElevation = 0.dp),
        ) {
            Icon(
                modifier = Modifier.padding(vertical = 4.dp),
                painter = painterResource(app.okcredit.ui.Res.drawable.icon_payment_down),
                contentDescription = "Received icon",
                tint = MaterialTheme.colorScheme.error,
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "Received",
                color = MaterialTheme.colorScheme.error,
                fontSize = 14.sp,
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                ),
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Button(
            modifier = Modifier
                .weight(1f)
                .semantics { contentDescription = "given_button" }
                .testTag("given_button"),
            onClick = { onGivenClicked() },
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surface),
            shape = CircleShape,
            elevation = ButtonDefaults.elevatedButtonElevation(defaultElevation = 0.dp),
        ) {
            Icon(
                modifier = Modifier.padding(vertical = 4.dp),
                painter = painterResource(app.okcredit.ui.Res.drawable.icon_credit_up),
                contentDescription = "Given icon",
                tint = MaterialTheme.colorScheme.primary,
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "Given",
                color = MaterialTheme.colorScheme.primary,
                fontSize = 14.sp,
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                ),
            )
        }
    }
}

@Preview
@Composable
fun PaymentAndCreditCtaPreview() {
    PaymentAndCreditCta(
        onReceivedClicked = {},
        onGivenClicked = {},
        modifier = Modifier.padding(16.dp),
    )
}
