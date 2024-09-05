package app.okcredit.onboarding.login

import okcredit.base.ui.BaseViewEvent
import okcredit.base.ui.UiState
import okcredit.base.ui.UserIntent
import tech.okcredit.auth.OtpToken

interface LoginContract {
    data class State(
        val mobile: String = "",
        val showEnterOtp: Boolean = false,
        val otpToken: OtpToken? = null,
        val showRecoveryNumber: Boolean = false,
        val showResendOptions: Boolean = false,
        val maskedRecoveryMobile: String? = null,
        val error: String? = null,
        val success: Boolean = false,
    ) : UiState

    sealed class Intent : UserIntent {
        data class ValidMobileEntered(val mobile: String) : Intent()
        data class FetchOtpToken(val mobile: String) : Intent()
        data class FetchFallbackOptions(val mobile: String) : Intent()
        data class OtpEntered(val otp: String) : Intent()
        data object ResendOtpOnSms : Intent()
        data object ResendOtpOnWhatsApp : Intent()
        data object ResendOtpOnRecoveryNumber : Intent()
    }

    sealed class PartialState : UiState.Partial {
        data object NoChange : PartialState()
        data class SetValidMobile(val mobile: String) : PartialState()
        data class SetOtpToken(val otpToken: OtpToken) : PartialState()
        data class SetVerified(val newUser: Boolean, val appLockEnabled: Boolean) : PartialState()
        data class SetFallbackOptions(
            val canShowRecoveryMobileOption: Boolean,
            val maskedRecoveryMobile: String?,
            val filteredIntentList: ArrayList<Int>,
        ) : PartialState()
    }

    sealed class ViewEvent : BaseViewEvent {
        data object GoToEnterBusinessName : ViewEvent()
        data object GoToSyncScreen : ViewEvent()
    }
}
