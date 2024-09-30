package app.okcredit.ledger.ui.customer.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.okcredit.ui.icon_call
import app.okcredit.ui.icon_chevron_right
import app.okcredit.ui.icon_credit_up
import app.okcredit.ui.icon_date
import app.okcredit.ui.icon_more_horiz
import app.okcredit.ui.icon_payment_down
import app.okcredit.ui.icon_sms_outline
import app.okcredit.ui.icon_statement_2
import app.okcredit.ui.icon_whatsapp
import app.okcredit.ui.theme.grey400
import okcredit.base.units.Paisa
import okcredit.base.units.formatPaisa
import org.jetbrains.compose.resources.painterResource

@Composable
fun CustomerBottomUi(
    dueDate: String?,
    balance: Paisa?,
    modifier: Modifier,
    onMoreClicked: () -> Unit,
    onReceivedClicked: () -> Unit,
    onGivenClicked: () -> Unit,
    onBalanceClicked: () -> Unit,
    onCallClicked: () -> Unit,
    onWhatsappClicked: () -> Unit,
) {
    Column(
        modifier = modifier
            .wrapContentHeight(align = Alignment.Bottom)
            .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)),
    ) {
        TopBar(
            onMoreClicked = onMoreClicked,
        )
        DueDateUi(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.tertiaryContainer),
            dueDate = dueDate,
            onCallClicked = onCallClicked,
            onWhatsappClicked = onWhatsappClicked,
        )
        HorizontalDivider(
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.surface,
        )
        BalanceUi(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background),
            balance = balance ?: Paisa.ZERO,
            onBalanceClicked = onBalanceClicked,
        )
        HorizontalDivider(
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.surface,
        )
        PaymentAndCreditCta(
            modifier = Modifier.background(MaterialTheme.colorScheme.background).padding(16.dp),
            onReceivedClicked = onReceivedClicked,
            onGivenClicked = onGivenClicked,
        )
    }
}

@Composable
fun DueDateUi(
    modifier: Modifier,
    dueDate: String?,
    onCallClicked: () -> Unit,
    onWhatsappClicked: () -> Unit,
) {
    Row(
        modifier = modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(
            modifier = Modifier
                .weight(0.5f),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (dueDate.isNullOrEmpty()) {
                DueDateWhenNotSet()
            } else {
                DueDateWhenSet(dueDate)
            }
        }
        Row(
            modifier = Modifier
                .weight(1f),
            horizontalArrangement = Arrangement.End,
        ) {
            CTAs(
                modifier = Modifier,
                onCallClicked = onCallClicked,
                onWhatsappClicked = onWhatsappClicked,
            )
        }
    }
}

@Composable
fun DueDateWhenSet(dueDate: String) {
    Icon(
        painter = painterResource(app.okcredit.ui.Res.drawable.icon_date),
        contentDescription = "Date icon",
        modifier = Modifier
            .width(16.dp)
            .height(16.dp),
    )
    Spacer(modifier = Modifier.height(5.dp))
    Row(
        modifier = Modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            modifier = Modifier,
            text = dueDate,
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.SemiBold,
            ),
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.primary,
        )
        Icon(
            modifier = Modifier,
            painter = painterResource(app.okcredit.ui.Res.drawable.icon_chevron_right),
            contentDescription = "chevron_right",
            tint = MaterialTheme.colorScheme.onSurface,
        )
    }
}

@Composable
fun DueDateWhenNotSet() {
    Icon(
        painter = painterResource(app.okcredit.ui.Res.drawable.icon_date),
        contentDescription = "Date icon",
        modifier = Modifier
            .width(16.dp)
            .height(16.dp),
    )
    Spacer(modifier = Modifier.width(2.dp))
    Text(
        text = "Set Due Date",
        style = MaterialTheme.typography.labelMedium.copy(
            fontWeight = FontWeight.SemiBold,
        ),
        fontSize = 12.sp,
        color = MaterialTheme.colorScheme.onSurface,
    )
}

@Composable
fun CTAs(
    modifier: Modifier,
    onCallClicked: () -> Unit,
    onWhatsappClicked: () -> Unit,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(
            modifier = Modifier
                .clickable { onCallClicked() }
                .padding(vertical = 8.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Spacer(modifier = Modifier.width(12.dp))
            Icon(
                painter = painterResource(app.okcredit.ui.Res.drawable.icon_call),
                contentDescription = "Call icon",
                modifier = Modifier.height(20.dp),
                tint = MaterialTheme.colorScheme.surface,
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "Call",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.surface,
                fontSize = 12.sp,
                maxLines = 1,
                modifier = Modifier.padding(vertical = 10.dp),
                overflow = TextOverflow.Ellipsis,
            )
            Spacer(modifier = Modifier.width(12.dp))
        }
        Spacer(modifier = Modifier.width(12.dp))
        Row(
            modifier = Modifier
                .clickable { onWhatsappClicked() }
                .padding(vertical = 8.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Spacer(modifier = Modifier.width(12.dp))
            Icon(
                painter = painterResource(app.okcredit.ui.Res.drawable.icon_whatsapp),
                contentDescription = "WhatsApp icon",
                modifier = Modifier.height(20.dp),
                tint = MaterialTheme.colorScheme.surface,
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "Remind",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.surface,
                fontSize = 12.sp,
                maxLines = 1,
                modifier = Modifier.padding(vertical = 10.dp),
                overflow = TextOverflow.Ellipsis,
            )
            Spacer(modifier = Modifier.width(12.dp))
        }
    }
}

@Composable
fun BalanceUi(
    modifier: Modifier,
    balance: Paisa,
    onBalanceClicked: () -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { onBalanceClicked() },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = if (balance < Paisa.ZERO) "Balance Due" else "Balance Advance",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.labelMedium,
        )

        Spacer(Modifier.height(2.dp))
        Row(
            modifier = Modifier,
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = formatPaisa(amount = balance.value, withRupeePrefix = true),
                color = if (balance < Paisa.ZERO) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.titleMedium,
            )
            Icon(
                painter = painterResource(app.okcredit.ui.Res.drawable.icon_chevron_right),
                contentDescription = "arrow_right",
                tint = if (balance < Paisa.ZERO) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
            )
        }
    }
}

@Composable
fun PaymentAndCreditCta(
    modifier: Modifier,
    onReceivedClicked: () -> Unit,
    onGivenClicked: () -> Unit,
) {
    Row(
        modifier = modifier.fillMaxWidth().semantics { contentDescription = "payment_buttons" }
            .testTag("payment_buttons"),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Button(
            modifier = Modifier.weight(1f).semantics { contentDescription = "received_button" }
                .testTag("received_button"),
            onClick = { onReceivedClicked() },
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surface),
            shape = CircleShape,
            elevation = ButtonDefaults.elevatedButtonElevation(defaultElevation = 0.dp),
        ) {
            Icon(
                painter = painterResource(app.okcredit.ui.Res.drawable.icon_payment_down),
                contentDescription = "Received icon",
                tint = MaterialTheme.colorScheme.primary,
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "Received",
                color = MaterialTheme.colorScheme.primary,
                fontSize = 14.sp,
                style = MaterialTheme.typography.bodyMedium,
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Button(
            modifier = Modifier.weight(1f).semantics { contentDescription = "given_button" }
                .testTag("given_button"),
            onClick = { onGivenClicked() },
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surface),
            shape = CircleShape,
            elevation = ButtonDefaults.elevatedButtonElevation(defaultElevation = 0.dp),
        ) {
            Icon(
                painter = painterResource(app.okcredit.ui.Res.drawable.icon_credit_up),
                contentDescription = "Given icon",
                tint = MaterialTheme.colorScheme.error,
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "Given",
                color = MaterialTheme.colorScheme.error,
                fontSize = 14.sp,
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}

@Composable
fun TopBar(
    onMoreClicked: () -> Unit,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    val customIconModifier = Modifier
        .background(grey400, CircleShape)
        .padding(4.dp)
        .width(16.dp)
        .height(16.dp)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.outlineVariant)
            .padding(horizontal = 10.dp, vertical = 0.dp)
            .minimumInteractiveComponentSize()
            .clickable(
                onClick = { onMoreClicked() },
                enabled = true,
                role = Role.Button,
                interactionSource = interactionSource,
                indication = ripple(bounded = false),
            ),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(
            modifier = Modifier,
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            Icon(
                painter = painterResource(app.okcredit.ui.Res.drawable.icon_statement_2),
                contentDescription = "Statement icon",
                modifier = customIconModifier,
                tint = MaterialTheme.colorScheme.surface,
            )
            Spacer(modifier = Modifier.width(10.dp))
            Icon(
                painter = painterResource(app.okcredit.ui.Res.drawable.icon_sms_outline),
                contentDescription = "Statement icon",
                modifier = customIconModifier,
                tint = MaterialTheme.colorScheme.surface,
            )
            Spacer(modifier = Modifier.width(10.dp))
            Icon(
                painter = painterResource(app.okcredit.ui.Res.drawable.icon_call),
                contentDescription = "Statement icon",
                modifier = customIconModifier,
                tint = MaterialTheme.colorScheme.surface,
            )
            Spacer(modifier = Modifier.width(10.dp))
            Icon(
                painter = painterResource(app.okcredit.ui.Res.drawable.icon_whatsapp),
                contentDescription = "Statement icon",
                modifier = customIconModifier,
                tint = MaterialTheme.colorScheme.surface,
            )
            Spacer(modifier = Modifier.width(10.dp))
        }
        Row(
            modifier = Modifier,
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                modifier = Modifier,
                text = "More",
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                ),
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 12.sp,
            )
            Spacer(modifier = Modifier.width(5.dp))

            Icon(
                painter = painterResource(app.okcredit.ui.Res.drawable.icon_more_horiz),
                contentDescription = "More icon",
                modifier = Modifier,
            )
        }
    }
}
