package tech.okcredit.identity.usecase

import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding
import tech.okcredit.identity.IdentityRepository
import tech.okcredit.identity.contract.usecase.UpdateRecoveryNumber
import tech.okcredit.identity.remote.request.UpdateIndividualRequest
import tech.okcredit.identity.remote.response.IndividualUser
import tech.okcredit.identity.remote.response.User

@Inject
@ContributesBinding(AppScope::class)
class UpdateRecoveryNumberImpl(
    private val repository: IdentityRepository,
) : UpdateRecoveryNumber {

    override suspend fun execute(
        individualId: String,
        primaryNumber: String,
        recoveryNumberOtpToken: String,
    ) {
        repository.updateIndividual(
            request = buildIndividualRequest(individualId, primaryNumber, recoveryNumberOtpToken),
        )
    }

    private fun buildIndividualRequest(individualId: String, primaryNumber: String, recoveryNumberOtpToken: String) =
        UpdateIndividualRequest(
            individual_user_id = individualId,
            individual_user = IndividualUser(
                user = User(
                    id = individualId,
                    mobile = primaryNumber,
                ),
            ),
            update_alternate_mobile = true,
            alternate_mobile_otp_token = recoveryNumberOtpToken,
        )
}
