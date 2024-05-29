package app.okcredit.onboarding.login

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun EnterOtpUiPreview() {
    EnterOtpUi(
        state = LoginContract.State(showResendOptions = true),
        otpEntered = {},
        resendOtpOnRecoveryNumber = {},
        resendOtpOnWhatsApp = {},
        resendOtpOnSms = {},
    )
}
