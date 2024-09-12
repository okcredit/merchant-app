package app.okcredit.ledger.ui.profile

import app.okcredit.ledger.contract.model.AccountType
import app.okcredit.ledger.ui.profile.usecase.GetRelationshipDetails
import okcredit.base.ui.BaseViewEvent
import okcredit.base.ui.UiState
import okcredit.base.ui.UserIntent

interface AccountProfileContract {

    data class State(
        val loading: Boolean = false,
        val accountId: String = "",
        val profileImage: String = "",
        val name: String = "",
        val mobile: String = "",
        val blocked: Boolean = false,
        val transactionRestricted: Boolean = false,
        val accountType: AccountType = AccountType.CUSTOMER,
        val registered: Boolean = false,
        val bottomSheetType: BottomSheetType? = null,
        val errorMessage: String? = null,
    ) : UiState

    sealed class BottomSheetType {
        data object Verified : BottomSheetType()

        data class CyclicAccount(
            val name: String,
            val mobile: String,
            val profileImage: String,
            val isSupplier: Boolean,
            val active: Boolean,
        ) : BottomSheetType()
    }

    sealed class Intent : UserIntent {
        data class SetBottomSheetType(val type: BottomSheetType?) : Intent()

        data class SubmitAddress(val newAddress: String) : Intent()

        data class SubmitPhoneNumber(val mobileNumber: String) : Intent()

        data class LoadDetails(val accountId: String, val accountType: AccountType) : Intent()

        data object HelpClicked : Intent()

        data class ModifyState(val block: Boolean) : Intent()

        data class ChangeProfileImage(val imagePath: String?) : Intent()

        data class UpdateAddTransactionPermission(val switch: Boolean) : Intent()

        data class SubmitName(val name: String) : Intent()

        data object OnMobileClicked : Intent()
    }

    sealed class PartialState : UiState.Partial {

        data class RelationshipDetails(val res: GetRelationshipDetails.Response) : PartialState()


        data class SetMobileNumber(val mobileNumber: String) : PartialState()

        data class SetLoading(val loading: Boolean) : PartialState()

        data class SetBottomSheetType(val type: BottomSheetType?) : PartialState()

        data object NoChange : PartialState()

        data class SetError(val errorMessage: String): PartialState()
    }

    sealed class ViewEvent : BaseViewEvent {
        data class ShowHelpScreen(val helpIds: List<String>) : ViewEvent()
    }
}