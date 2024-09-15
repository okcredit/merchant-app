package app.okcredit.ledger.ui.profile

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import app.okcredit.ledger.contract.model.AccountType
import app.okcredit.ledger.ui.profile.AccountProfileContract.BottomSheetType.ModifyName
import app.okcredit.ledger.ui.profile.AccountProfileContract.BottomSheetType.ModifyPhoneNumber
import app.okcredit.ledger.ui.profile.AccountProfileContract.Intent
import app.okcredit.ledger.ui.profile.AccountProfileContract.State
import app.okcredit.ledger.ui.profile.AccountProfileContract.ViewEvent
import app.okcredit.ledger.ui.profile.composable.RelationshipProfileScreen
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import okcredit.base.di.observeViewEvents
import okcredit.base.di.rememberScreenModel

data class AccountProfileScreen(
    val accountId: String,
    val accountType: AccountType
) : Screen {

    @Composable
    override fun Content() {
        val screenModel = rememberScreenModel<AccountProfileModel>()

        val state by screenModel.states.collectAsState()

        val navigator = LocalNavigator.currentOrThrow

        screenModel.observeViewEvents {
            handleViewEvent(it, navigator)
        }

        render(screenModel, state, navigator)
    }

    @Composable
    private fun render(
        screenModel: AccountProfileModel,
        state: State,
        navigator: Navigator
    ) {
        RelationshipProfileScreen(
            state = state,
            onBackClicked = { navigator.pop() },
            onMobileClicked = { screenModel.pushIntent(Intent.SetBottomSheetType(ModifyPhoneNumber)) },
            onNameClicked = { screenModel.pushIntent(Intent.SetBottomSheetType(ModifyName)) },
            onDeniedTransactionSwitchClicked = {
                screenModel.pushIntent(
                    Intent.UpdateAddTransactionPermission(
                        it
                    )
                )
            },
            onDismissInfoDialog = { screenModel.pushIntent(Intent.SetInfoDialogType(null)) },
            onCyclicAccountCtaClicked = { screenModel.pushIntent(Intent.SetInfoDialogType(null)) },
            onVerifiedButtonClicked = { screenModel.pushIntent(Intent.SetInfoDialogType(null)) },
            onSubmitMobile = { screenModel.pushIntent(Intent.SubmitPhoneNumber(it)) },
            onSubmitName = { screenModel.pushIntent(Intent.SubmitName(it)) },
            onBlockRelationshipClicked = { screenModel.pushIntent(Intent.ModifyState(!state.blocked)) },
            onHelpClicked = { },
            onDeleteRelationshipClicked = {},
            onSmsSettingsClicked = {},
            onProfileImageClicked = {},
        )
    }

    private fun handleViewEvent(event: ViewEvent, navigator: Navigator) {}
}