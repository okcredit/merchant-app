package app.okcredit.ledger.ui.profile.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import app.okcredit.ledger.ui.err_cyclic_conflict_customer
import app.okcredit.ledger.ui.err_cyclic_conflict_supplier
import app.okcredit.ledger.ui.view_customer
import app.okcredit.ledger.ui.view_supplier
import app.okcredit.ui.Res
import app.okcredit.ui.icon_account
import app.okcredit.ui.icon_close_fill
import app.okcredit.ui.icon_remove
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun CyclicAccountDialog(
    modifier: Modifier,
    mobile: String?,
    name: String?,
    active: Boolean,
    isCustomer: Boolean,
    onDismiss: () -> Unit,
    onViewClicked: (Boolean) -> Unit
) {
    Card(
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        modifier = modifier
            .padding(34.dp)
            .fillMaxWidth()
    ) {
        Box {
            Column(
                modifier = Modifier.wrapContentSize().padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(Res.drawable.icon_account),
                    contentDescription = null,
                    modifier = Modifier
                        .size(64.dp)
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(8.dp)
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = if (isCustomer) {
                        stringResource(
                            app.okcredit.ledger.ui.Res.string.err_cyclic_conflict_customer,
                            mobile ?: "",
                            name ?: ""
                        )
                    } else {
                        stringResource(
                            app.okcredit.ledger.ui.Res.string.err_cyclic_conflict_supplier,
                            mobile ?: "",
                            name ?: ""
                        )
                    },
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(32.dp))
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                        .clickable { onViewClicked(active) }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 20.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Image(
                            painter = painterResource(Res.drawable.icon_remove),
                            contentDescription = null,
                            modifier = Modifier.size(24.dp),
                            colorFilter = ColorFilter.tint(Color.White)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = stringResource(if (isCustomer) app.okcredit.ledger.ui.Res.string.view_customer else app.okcredit.ledger.ui.Res.string.view_supplier),
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.White
                        )
                    }
                }
            }
            Box(
                modifier = Modifier
                    .padding(top = 8.dp, end = 8.dp)
                    .align(Alignment.TopEnd)
                    .clickable { onDismiss() }
            ) {
                Icon(
                    painter = painterResource(Res.drawable.icon_close_fill),
                    contentDescription = "close_icon",
                    modifier = Modifier
                        .padding(4.dp)
                        .align(Alignment.CenterEnd),
                    tint = MaterialTheme.colorScheme.outlineVariant,
                )
            }
        }
    }
}

@Preview
@Composable
fun CyclicAccountDialogPreview() {
    CyclicAccountDialog(
        mobile = "1234567890",
        name = "John Doe",
        isCustomer = true,
        onDismiss = {},
        onViewClicked = {},
        modifier = Modifier.fillMaxSize(),
        active = true
    )
}
