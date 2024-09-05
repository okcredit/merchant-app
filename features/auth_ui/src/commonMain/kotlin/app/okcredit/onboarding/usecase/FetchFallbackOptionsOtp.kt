package app.okcredit.onboarding.usecase

import me.tatarka.inject.annotations.Inject
import tech.okcredit.auth.AuthService
import tech.okcredit.auth.remote.AuthApiClient

@Inject
class FetchFallbackOptionsOtp(
    private val authService: Lazy<AuthService>,
) {

    suspend fun execute(mobileNumber: String): OtpFallbackOption {
        val fallbackOptions = authService.value.requestFallbackOptions(mobileNumber)

        var filteredIntentList = ArrayList<Int>()
        var canShowRecoveryMobileOption = false
        var maskedRecoveryMobile: String? = null

        for (fallbackOption in fallbackOptions.retry_options) {
            if (fallbackOption.destination == AuthApiClient.RetryDestination.PRIMARY.key) {
                filteredIntentList = fallbackOption.intents
            } else if (fallbackOption.destination == AuthApiClient.RetryDestination.SECONDARY.key) {
                maskedRecoveryMobile = fallbackOption.data?.masked_alternate_number
                canShowRecoveryMobileOption = !fallbackOption.data?.masked_alternate_number.isNullOrEmpty()
            }
        }
        return OtpFallbackOption(
            filteredIntentList = filteredIntentList,
            canShowRecoveryMobileOption = canShowRecoveryMobileOption,
            maskedRecoveryMobile = maskedRecoveryMobile,
        )
    }

    data class OtpFallbackOption(
        val filteredIntentList: ArrayList<Int>,
        val canShowRecoveryMobileOption: Boolean = false,
        val maskedRecoveryMobile: String? = null,
    )
}
