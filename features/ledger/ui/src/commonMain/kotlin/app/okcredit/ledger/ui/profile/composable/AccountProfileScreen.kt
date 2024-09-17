package app.okcredit.ledger.ui.profile.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetValue
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import app.okcredit.ledger.ui.edit_address
import app.okcredit.ledger.ui.edit_mobile_number
import app.okcredit.ledger.ui.edit_name
import app.okcredit.ledger.ui.enter_address
import app.okcredit.ledger.ui.enter_mobile_number
import app.okcredit.ledger.ui.enter_name
import app.okcredit.ledger.ui.profile.AccountProfileContract
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RelationshipProfileScreen(
    state: AccountProfileContract.State,
    onBackClicked: () -> Unit,
    onHelpClicked: () -> Unit,
    onProfileImageClicked: () -> Unit,
    onMobileClicked: () -> Unit,
    onSmsSettingsClicked: () -> Unit,
    onBlockRelationshipClicked: () -> Unit,
    onDeleteRelationshipClicked: () -> Unit,
    onVerifiedButtonClicked: () -> Unit,
    onDismissInfoDialog: () -> Unit,
    onCyclicAccountCtaClicked: (Boolean) -> Unit,
    onDeniedTransactionSwitchClicked: (Boolean) -> Unit,
    onNameClicked: () -> Unit,
    onAddressClicked: () -> Unit,
    onSubmitName: (String) -> Unit,
    onSubmitMobile: (String) -> Unit,
    onSubmitAddress: (String) -> Unit,
    loadDetails: () -> Unit,
    onDismissBottomSheet: () -> Unit,
) {
    val bottomSheetState = rememberModalBottomSheetState(
    )

    LaunchedEffect(true) {
        loadDetails()
    }

    LaunchedEffect(state.bottomSheetType) {
        if (state.bottomSheetType != null) {
            bottomSheetState.show()
        } else {
            bottomSheetState.hide()
        }
    }

    Scaffold(
        topBar = {
            AccountProfileToolbar(
                onBackClicked = onBackClicked,
                onHelpClicked = onHelpClicked
            )
        },
        bottomBar = {
            val type = state.infoDialogType
            if (type != null) {
                Box(
                    modifier = Modifier
                        .clickable { onDismissInfoDialog() },
                ) {
                    when (type) {
                        is AccountProfileContract.InfoDialogType.Verified -> {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(
                                        color = MaterialTheme.colorScheme.onBackground.copy(
                                            alpha = 0.2f
                                        )
                                    )
                                    .align(Alignment.Center)
                            ) {
                                VerifiedDialog(
                                    modifier = Modifier.align(Alignment.Center),
                                    profileImage = state.profileImage,
                                    onPrimaryCtaClicked = onVerifiedButtonClicked
                                )
                            }
                        }

                        is AccountProfileContract.InfoDialogType.CyclicAccount -> {
                            CyclicAccountDialog(
                                modifier = Modifier.align(Alignment.Center),
                                name = type.name,
                                mobile = type.mobile,
                                isCustomer = !type.isSupplier,
                                onDismiss = onDismissInfoDialog,
                                onViewClicked = onCyclicAccountCtaClicked,
                                active = type.active
                            )
                        }
                    }
                }
            }
        },
        content = { contentPadding ->
            Box {
                if (state.bottomSheetType != null) {
                    ModalBottomSheet(
                        containerColor = MaterialTheme.colorScheme.surface,
                        sheetState = bottomSheetState,
                        onDismissRequest = { onDismissBottomSheet() },
                        content = {
                            when (state.bottomSheetType) {
                                is AccountProfileContract.BottomSheetType.ModifyName -> {
                                    ModifyDetailDialog(
                                        title = if (state.name.isEmpty())
                                            stringResource(app.okcredit.ledger.ui.Res.string.enter_name)
                                        else
                                            stringResource(app.okcredit.ledger.ui.Res.string.edit_name),
                                        prefillText = state.name,
                                        onSubmitClicked = { onSubmitName(it) },
                                        onCloseClicked = { onDismissInfoDialog() }
                                    )
                                }

                                is AccountProfileContract.BottomSheetType.ModifyPhoneNumber -> {
                                    ModifyDetailDialog(
                                        title = if (state.name.isEmpty())
                                            stringResource(app.okcredit.ledger.ui.Res.string.enter_mobile_number)
                                        else
                                            stringResource(app.okcredit.ledger.ui.Res.string.edit_mobile_number),
                                        prefillText = state.mobile,
                                        onSubmitClicked = onSubmitMobile,
                                        onCloseClicked = { onDismissInfoDialog() }
                                    )
                                }

                                is AccountProfileContract.BottomSheetType.ModifyAddress -> {
                                    ModifyDetailDialog(
                                        title = if (state.address.isEmpty()) {
                                            stringResource(app.okcredit.ledger.ui.Res.string.enter_address)
                                        } else {
                                            stringResource(app.okcredit.ledger.ui.Res.string.edit_address)
                                        },
                                        prefillText = state.address,
                                        onCloseClicked = {},
                                        onSubmitClicked = onSubmitAddress
                                    )
                                }

                                null -> {
                                    // Do nothing
                                }
                            }
                        },
                        dragHandle = { BottomSheetDefaults.DragHandle() }
                    )
                }
                AccountProfileContent(
                    state = ProfileContentState(
                        profileImage = state.profileImage,
                        name = state.name,
                        mobile = state.mobile,
                        blocked = state.blocked,
                        transactionRestricted = state.transactionRestricted,
                        accountType = state.accountType,
                        registered = state.registered,
                        address = state.address
                    ),
                    contentPadding = contentPadding,
                    onProfileClicked = onProfileImageClicked,
                    onMobileClicked = onMobileClicked,
                    onSmsSettingsClicked = onSmsSettingsClicked,
                    onBlockRelationshipClicked = onBlockRelationshipClicked,
                    onDeleteRelationshipClicked = onDeleteRelationshipClicked,
                    onDeniedTransactionSwitchClicked = onDeniedTransactionSwitchClicked,
                    onNameClicked = onNameClicked,
                    onAddressClicked = onAddressClicked,
                )
            }
        }
    )
}

@Preview
@Composable
fun RelationshipProfileScreenPreview() {
    RelationshipProfileScreen(
        state = AccountProfileContract.State(
            infoDialogType = null,
        ),
        onBackClicked = {},
        onHelpClicked = {},
        onProfileImageClicked = {},
        onMobileClicked = {},
        onSmsSettingsClicked = {},
        onBlockRelationshipClicked = {},
        onDeleteRelationshipClicked = {},
        onVerifiedButtonClicked = {},
        onDismissInfoDialog = {},
        onCyclicAccountCtaClicked = {},
        onDeniedTransactionSwitchClicked = {},
        onNameClicked = {},
        onSubmitName = {},
        onSubmitMobile = {},
        onAddressClicked = {},
        onSubmitAddress = {},
        loadDetails = {},
        onDismissBottomSheet = {}
    )
}
