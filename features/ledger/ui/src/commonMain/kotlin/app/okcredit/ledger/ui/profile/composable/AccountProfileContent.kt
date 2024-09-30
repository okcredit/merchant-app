package app.okcredit.ledger.ui.profile.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import app.okcredit.ledger.contract.model.AccountType
import app.okcredit.ledger.contract.model.isSupplier
import app.okcredit.ledger.ui.add_address
import app.okcredit.ledger.ui.add_mobile_number
import app.okcredit.ledger.ui.block
import app.okcredit.ledger.ui.communication
import app.okcredit.ledger.ui.contact_information
import app.okcredit.ledger.ui.customer_permission
import app.okcredit.ledger.ui.delete
import app.okcredit.ledger.ui.deny_to_add_transaction
import app.okcredit.ledger.ui.enter_name
import app.okcredit.ledger.ui.icon_add_txn_permission
import app.okcredit.ledger.ui.icon_address
import app.okcredit.ledger.ui.sms_settings
import app.okcredit.ledger.ui.sms_settings_info
import app.okcredit.ledger.ui.unblock
import app.okcredit.ui.Res
import app.okcredit.ui.icon_block
import app.okcredit.ui.icon_chevron_right
import app.okcredit.ui.icon_delete
import app.okcredit.ui.icon_mobile
import app.okcredit.ui.icon_sms_outline
import app.okcredit.ui.icon_user_outline
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import app.okcredit.ledger.ui.Res as ledgerRes

data class ProfileContentState(
    val name: String,
    val profileImage: String,
    val mobile: String,
    val address: String,
    val registered: Boolean,
    val accountType: AccountType,
    val transactionRestricted: Boolean,
    val blocked: Boolean,
)

@Composable
fun AccountProfileContent(
    state: ProfileContentState,
    contentPadding: PaddingValues,
    onProfileClicked: () -> Unit,
    onMobileClicked: () -> Unit,
    onSmsSettingsClicked: () -> Unit,
    onBlockRelationshipClicked: () -> Unit,
    onDeleteRelationshipClicked: () -> Unit,
    onDeniedTransactionSwitchClicked: (Boolean) -> Unit,
    onNameClicked: () -> Unit,
    onAddressClicked: () -> Unit,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.surface)
            .padding(contentPadding),
    ) {
        item {
            Spacer(modifier = Modifier.height(16.dp))
            ProfileHeader(
                profileImgUrl = state.profileImage,
                onProfileImageClicked = onProfileClicked,
            )
            ProfileName(
                modifier = Modifier,
                name = state.name.ifBlank { stringResource(ledgerRes.string.enter_name) },
                onNameClicked = onNameClicked,
            )
            ContactInformation(
                modifier = Modifier,
                mobile = state.mobile,
                address = state.address,
                onMobileClicked = onMobileClicked,
                onAddressClicked = onAddressClicked,
            )
// todo add later when sms sent from phone setup
//            Communications(
//                modifier = Modifier,
//                mobile = state.mobile,
//                onSmsSettingsClicked = onSmsSettingsClicked
//            )
            CustomerPermissions(
                modifier = Modifier,
                registered = state.registered,
                accountType = state.accountType,
                transactionRestricted = state.transactionRestricted,
                onDeniedTransactionSwitchClicked = onDeniedTransactionSwitchClicked,
            )
            Footer(
                mobile = state.mobile,
                registered = state.registered,
                blocked = state.blocked,
                onBlockRelationshipClicked = onBlockRelationshipClicked,
                onDeleteRelationshipClicked = onDeleteRelationshipClicked,
            )
        }
    }
}

@Composable
fun Footer(
    mobile: String,
    registered: Boolean,
    blocked: Boolean,
    onBlockRelationshipClicked: () -> Unit,
    onDeleteRelationshipClicked: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .background(MaterialTheme.colorScheme.background, shape = RoundedCornerShape(16.dp)),
    ) {
        if (registered && mobile.isNotEmpty()) {
            Row(
                modifier = Modifier
                    .clip(shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                    .clickable {
                        onBlockRelationshipClicked()
                    },
                verticalAlignment = Alignment.CenterVertically,

            ) {
                Icon(
                    painter = painterResource(Res.drawable.icon_block),
                    contentDescription = "icon",
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(
                        start = 16.dp,
                        top = 16.dp,
                        bottom = 16.dp,
                        end = 12.dp,
                    ),
                )
                Text(
                    text = stringResource(if (blocked) ledgerRes.string.unblock else ledgerRes.string.block),
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier,
                )
                Spacer(modifier = Modifier.weight(1f))
            }
            Spacer(
                modifier = Modifier
                    .height(1.dp)
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface),
            )
        }
        Row(
            modifier = Modifier
                .clip(
                    shape = RoundedCornerShape(
                        bottomStart = 16.dp,
                        bottomEnd = 16.dp,
                    ),
                )
                .clickable { onDeleteRelationshipClicked() },
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                painter = painterResource(Res.drawable.icon_delete),
                contentDescription = "icon",
                tint = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 16.dp, end = 12.dp),
            )
            Text(
                text = stringResource(app.okcredit.ledger.ui.Res.string.delete),
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier
                    .padding(vertical = 12.dp)
                    .weight(1f),
            )
        }
    }
}

@Composable
fun CustomerPermissions(
    modifier: Modifier,
    registered: Boolean,
    accountType: AccountType,
    transactionRestricted: Boolean,
    onDeniedTransactionSwitchClicked: (Boolean) -> Unit,
) {
    if (registered && !accountType.isSupplier()) {
        HeadingItem(
            modifier = modifier.padding(start = 16.dp, top = 8.dp),
            title = stringResource(app.okcredit.ledger.ui.Res.string.customer_permission),
        )
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .background(
                MaterialTheme.colorScheme.background,
                shape = RoundedCornerShape(16.dp),
            ),
    ) {
        if (registered && !accountType.isSupplier()) {
            Row(
                modifier = Modifier
                    .clip(shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                    .clickable {
                        onDeniedTransactionSwitchClicked(!transactionRestricted)
                    },
                verticalAlignment = Alignment.CenterVertically,

            ) {
                Icon(
                    painter = painterResource(app.okcredit.ledger.ui.Res.drawable.icon_add_txn_permission),
                    contentDescription = "icon",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(
                        start = 16.dp,
                        top = 16.dp,
                        bottom = 16.dp,
                        end = 12.dp,
                    ),
                )
                Text(
                    text = stringResource(app.okcredit.ledger.ui.Res.string.deny_to_add_transaction),
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier,
                )
                Spacer(modifier = Modifier.weight(1f))
                Switch(
                    checked = transactionRestricted,
                    onCheckedChange = {
                        onDeniedTransactionSwitchClicked(it)
                    },
                )
                Spacer(modifier = Modifier.padding(end = 4.dp))
            }
            Spacer(
                modifier = Modifier
                    .height(1.dp)
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface),
            )
        }
//        Row(
//            modifier = Modifier
//                .clip(
//                    shape = RoundedCornerShape(
//                        bottomStart = 16.dp,
//                        bottomEnd = 16.dp
//                    )
//                )
//                .clickable { onMoveRelationshipClicked() },
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            Icon(
//                painter = painterResource(app.okcredit.ledger.ui.Res.drawable.icon_move_to_supplier),
//                contentDescription = "icon",
//                tint = MaterialTheme.colorScheme.primary,
//                modifier = Modifier
//                    .graphicsLayer { if (accountType.isSupplier()) rotationY = 180f }
//                    .padding(
//                        start = 16.dp,
//                        top = 16.dp,
//                        bottom = 16.dp,
//                        end = 12.dp
//                    )
//            )
//            Text(
//                text = stringResource(
//                    if (accountType.isSupplier()) {
//                        app.okcredit.ledger.ui.Res.string.move_to_customer
//                    } else {
//                        app.okcredit.ledger.ui.Res.string.move_to_supplier
//                    }
//                ),
//                style = MaterialTheme.typography.titleSmall,
//                color = MaterialTheme.colorScheme.onSurface,
//                modifier = Modifier
//                    .padding(vertical = 12.dp)
//                    .weight(1f)
//            )
//            Icon(
//                painter = painterResource(Res.drawable.icon_chevron_right),
//                contentDescription = "edit",
//                tint = MaterialTheme.colorScheme.onSurface,
//                modifier = Modifier.padding(end = 16.dp)
//            )
//        }
    }
}

@Composable
fun Communications(
    modifier: Modifier,
    mobile: String?,
    onSmsSettingsClicked: () -> Unit,
) {
    if (mobile.isNullOrEmpty()) return
    HeadingItem(
        modifier = modifier.padding(start = 16.dp, top = 8.dp, bottom = 4.dp),
        title = stringResource(ledgerRes.string.communication),
    )
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape = RoundedCornerShape(16.dp))
            .padding(horizontal = 16.dp)
            .background(MaterialTheme.colorScheme.background, shape = RoundedCornerShape(16.dp))
            .clickable {
                onSmsSettingsClicked()
            },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = painterResource(Res.drawable.icon_sms_outline),
            contentDescription = "icon",
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 16.dp, end = 12.dp),
        )
        Column {
            Text(
                text = stringResource(app.okcredit.ledger.ui.Res.string.sms_settings),
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(top = 12.dp),
            )
            Text(
                text = stringResource(ledgerRes.string.sms_settings_info),
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Normal),
                modifier = Modifier.padding(bottom = 12.dp),
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        Icon(
            painter = painterResource(Res.drawable.icon_chevron_right),
            contentDescription = "edit",
            tint = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(end = 16.dp),
        )
    }
}

@Composable
fun ContactInformation(
    modifier: Modifier,
    mobile: String,
    address: String,
    onAddressClicked: () -> Unit,
    onMobileClicked: () -> Unit,
) {
    HeadingItem(
        modifier = modifier.padding(horizontal = 16.dp),
        title = stringResource(ledgerRes.string.contact_information),
    )
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .background(MaterialTheme.colorScheme.background, shape = RoundedCornerShape(16.dp)),
    ) {
        Row(
            modifier = Modifier
                .clip(shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                .clickable {
                    onMobileClicked()
                },
            verticalAlignment = Alignment.CenterVertically,

        ) {
            Icon(
                painter = painterResource(Res.drawable.icon_mobile),
                contentDescription = "icon",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 16.dp, end = 12.dp),
            )
            Text(
                text = mobile.ifBlank { stringResource(ledgerRes.string.add_mobile_number) },
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier,
            )
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                painter = painterResource(Res.drawable.icon_chevron_right),
                contentDescription = "edit",
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(end = 16.dp),
            )
        }
        Spacer(
            modifier = Modifier
                .height(1.dp)
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface),
        )
        Row(
            modifier = Modifier
                .clip(
                    shape = RoundedCornerShape(
                        bottomStart = 16.dp,
                        bottomEnd = 16.dp,
                    ),
                )
                .clickable { onAddressClicked() },
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                painter = painterResource(ledgerRes.drawable.icon_address),
                contentDescription = "icon",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 16.dp, end = 12.dp),
            )
            Text(
                text = address.ifBlank { stringResource(ledgerRes.string.add_address) },
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .padding(vertical = 12.dp)
                    .weight(1f),
            )
            Icon(
                painter = painterResource(Res.drawable.icon_chevron_right),
                contentDescription = "edit",
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(end = 16.dp),
            )
        }
    }
}

@Composable
fun ProfileName(
    modifier: Modifier,
    name: String,
    onNameClicked: () -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.background, shape = RoundedCornerShape(16.dp))
            .clickable { onNameClicked() },
        verticalAlignment = Alignment.CenterVertically,

    ) {
        Icon(
            painter = painterResource(Res.drawable.icon_user_outline),
            contentDescription = "icon",
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 16.dp, end = 12.dp),
        )
        Text(
            text = name,
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier
                .weight(1f),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        Spacer(modifier = Modifier.weight(1f))
        Icon(
            painter = painterResource(Res.drawable.icon_chevron_right),
            contentDescription = "edit",
            tint = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(end = 16.dp),
        )
    }
}

@Composable
fun HeadingItem(modifier: Modifier, title: String) {
    Text(
        modifier = modifier,
        text = title,
        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
    )
}

@Preview
@Composable
fun HeadingItemPreview() {
    HeadingItem(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surface),
        title = "Contact Info",
    )
}

@Preview
@Composable
fun RelationshipProfileContentPreview() {
    AccountProfileContent(
        state = ProfileContentState(
            name = "John Doe",
            mobile = "9876543210",
            profileImage = "",
            registered = true,
            accountType = AccountType.CUSTOMER,
            transactionRestricted = false,
            blocked = false,
            address = "",
        ),
        contentPadding = PaddingValues(0.dp), onProfileClicked = {},
        onMobileClicked = {},
        onSmsSettingsClicked = {},
        onBlockRelationshipClicked = {},
        onDeleteRelationshipClicked = {},
        onDeniedTransactionSwitchClicked = {},
        onNameClicked = {},
        onAddressClicked = {},
    )
}
