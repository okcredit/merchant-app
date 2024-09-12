package app.okcredit.ledger.ui.profile.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import app.okcredit.ledger.ui.profile.AccountProfileContract
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
    onMoveRelationshipClicked: () -> Unit,
    onBlockRelationshipClicked: () -> Unit,
    onDeleteRelationshipClicked: () -> Unit,
    onVerifiedButtonClicked: () -> Unit,
    onDismissBottomSheet: () -> Unit,
    onCyclicAccountCtaClicked: (Boolean) -> Unit,
    onDeniedTransactionSwitchClicked: (Boolean) -> Unit,
    onNameClicked: () -> Unit
) {
    val bottomSheetState = rememberModalBottomSheetState()

    LaunchedEffect(state.bottomSheetType) {
        if (state.bottomSheetType != null) {
            bottomSheetState.show()
        } else {
            bottomSheetState.hide()
        }
    }
    ModalBottomSheet(
        sheetState = bottomSheetState,
        onDismissRequest = {},
    ) {
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
                            .clickable { onDismissBottomSheet() },
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
                                    onDismiss = onDismissBottomSheet,
                                    onViewClicked = onCyclicAccountCtaClicked,
                                    active = type.active
                                )
                            }
                        }
                    }
                }
            },
            content = { contentPadding ->
                AccountProfileContent(
                    state = ProfileContentState(
                        profileImage = state.profileImage,
                        name = state.name,
                        mobile = state.mobile,
                        blocked = state.blocked,
                        transactionRestricted = state.transactionRestricted,
                        accountType = state.accountType,
                        registered = state.registered,
                    ),
                    contentPadding = contentPadding,
                    onProfileClicked = onProfileImageClicked,
                    onMobileClicked = onMobileClicked,
                    onSmsSettingsClicked = onSmsSettingsClicked,
                    onMoveRelationshipClicked = onMoveRelationshipClicked,
                    onBlockRelationshipClicked = onBlockRelationshipClicked,
                    onDeleteRelationshipClicked = onDeleteRelationshipClicked,
                    onDeniedTransactionSwitchClicked = onDeniedTransactionSwitchClicked,
                    onNameClicked = onNameClicked,
                )
            }
        )
    }
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
        onMoveRelationshipClicked = {},
        onBlockRelationshipClicked = {},
        onDeleteRelationshipClicked = {},
        onVerifiedButtonClicked = {},
        onDismissBottomSheet = {},
        onCyclicAccountCtaClicked = {},
        onDeniedTransactionSwitchClicked = {},
        onNameClicked = {}
    )
}
