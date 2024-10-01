package app.okcredit.onboarding.login

import app.okcredit.onboarding.login.LoginContract.*
import app.okcredit.onboarding.usecase.FetchFallbackOptionsOtp
import app.okcredit.onboarding.usecase.LoginUser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import me.tatarka.inject.annotations.Inject
import okcredit.base.randomUUID
import okcredit.base.ui.BaseCoroutineScreenModel
import okcredit.base.ui.Result
import tech.okcredit.auth.AuthService
import tech.okcredit.auth.Credential
import tech.okcredit.auth.InvalidOtp
import tech.okcredit.auth.remote.AuthApiClient

@Inject
class LoginScreenModel(
    private val authService: AuthService,
    private val loginUser: LoginUser,
    private val fetchFallbackOptionsOtp: FetchFallbackOptionsOtp,
) : BaseCoroutineScreenModel<State, PartialState, ViewEvent, Intent>(State()) {

    private val flowId by lazy { randomUUID() }

    override fun partialStates(): Flow<PartialState> {
        return merge(
            loadFallbackOptions(),
            observeValidMobileEntered(),
            observeFetchOtpToken(),
            observeOtpEntered(),
            observeResendOtpOnSms(),
            observeResendOtpOnWhatsApp(),
            observeResendOtpOnRecoveryNumber(),
        )
    }

    private fun loadFallbackOptions() = intent<Intent.FetchFallbackOptions>()
        .flatMapLatest { wrap { fetchFallbackOptionsOtp.execute(it.mobile) } }
        .map {
            when (it) {
                is Result.Failure -> PartialState.NoChange
                is Result.Progress -> PartialState.NoChange
                is Result.Success -> {
                    PartialState.SetFallbackOptions(
                        canShowRecoveryMobileOption = it.value.canShowRecoveryMobileOption,
                        maskedRecoveryMobile = it.value.maskedRecoveryMobile,
                        filteredIntentList = it.value.filteredIntentList,
                    )
                }
            }
        }

    private fun observeResendOtpOnSms() = intent<Intent.ResendOtpOnSms>()
        .flatMapLatest {
            wrap {
                authService.resendOtp(
                    mobile = currentState.mobile,
                    flowId = flowId,
                    destination = AuthApiClient.RetryDestination.PRIMARY,
                    otpId = currentState.otpToken?.id!!,
                    requestMedium = AuthApiClient.RequestOtpMedium.SMS,
                    flowType = "Login",
                )
            }
        }.map { PartialState.NoChange }

    private fun observeResendOtpOnWhatsApp() = intent<Intent.ResendOtpOnWhatsApp>()
        .flatMapLatest {
            wrap {
                authService.resendOtp(
                    mobile = currentState.mobile,
                    flowId = flowId,
                    destination = AuthApiClient.RetryDestination.PRIMARY,
                    otpId = currentState.otpToken?.id!!,
                    requestMedium = AuthApiClient.RequestOtpMedium.WHATSAPP,
                    flowType = "Login",
                )
            }
        }.map { PartialState.NoChange }

    private fun observeResendOtpOnRecoveryNumber() = intent<Intent.ResendOtpOnRecoveryNumber>()
        .flatMapLatest {
            wrap {
                authService.resendOtp(
                    mobile = currentState.mobile,
                    flowId = flowId,
                    destination = AuthApiClient.RetryDestination.SECONDARY,
                    otpId = currentState.otpToken?.id!!,
                    requestMedium = AuthApiClient.RequestOtpMedium.SMS,
                    flowType = "Login",
                )
            }
        }.map { PartialState.NoChange }

    private fun observeFetchOtpToken() = intent<Intent.ValidMobileEntered>()
        .flatMapLatest {
            wrap {
                authService.requestOtp(
                    mobile = it.mobile,
                    flowId = flowId,
                    flowType = "Login",
                )
            }
        }
        .map {
            when (it) {
                is Result.Failure -> PartialState.NoChange
                is Result.Progress -> PartialState.NoChange
                is Result.Success -> PartialState.SetOtpToken(otpToken = it.value)
            }
        }

    private fun observeValidMobileEntered() = intent<Intent.ValidMobileEntered>()
        .map {
            pushIntent(Intent.FetchOtpToken(it.mobile))
            pushIntent(Intent.FetchFallbackOptions(it.mobile))
            PartialState.SetValidMobile(mobile = it.mobile)
        }

    private fun observeOtpEntered() = intent<Intent.OtpEntered>()
        .flatMapLatest {
            wrap {
                loginUser.execute(
                    credential = Credential.Otp(currentState.otpToken!!, code = it.otp),
                    flowId = flowId,
                    flowType = "Login",
                )
            }
        }.map {
            when (it) {
                is Result.Failure -> {
                    if (it.error is InvalidOtp) {
                    }
                    PartialState.NoChange
                }
                is Result.Progress -> PartialState.NoChange
                is Result.Success -> {
                    if (it.value.first) {
                        emitViewEvent(ViewEvent.GoToEnterBusinessName)
                    } else {
                        emitViewEvent(ViewEvent.GoToSyncScreen)
                    }
                    PartialState.SetVerified(it.value.first, it.value.second)
                }
            }
        }

    override fun reduce(currentState: State, partialState: PartialState): State {
        return when (partialState) {
            PartialState.NoChange -> currentState
            is PartialState.SetValidMobile -> currentState.copy(
                mobile = partialState.mobile,
                showEnterOtp = true,
            )
            is PartialState.SetOtpToken -> currentState.copy(otpToken = partialState.otpToken)
            is PartialState.SetVerified -> currentState.copy(success = true)
            is PartialState.SetFallbackOptions -> currentState.copy(
                showResendOptions = true,
                showRecoveryNumber = partialState.canShowRecoveryMobileOption,
                maskedRecoveryMobile = partialState.maskedRecoveryMobile,
            )
        }
    }
}
