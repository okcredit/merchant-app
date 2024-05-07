package tech.okcredit.identity.usecase

import me.tatarka.inject.annotations.Inject
import tech.okcredit.identity.IdentityRepository
import tech.okcredit.identity.contract.usecase.UpdateIndividualMobile

@Inject
class UpdateIndividualMobileImpl(
    private val repository: IdentityRepository,
) : UpdateIndividualMobile {

    override suspend fun execute(
        individualId: String,
        mobile: String,
        currentMobileOtpToken: String,
        newMobileOtpToken: String,
    ) {
        repository.updateBusinessMobile(
            individualId = individualId,
            mobile = mobile,
            currentMobileOtpToken = currentMobileOtpToken,
            newMobileOtpToken = newMobileOtpToken,
        )
    }
}
