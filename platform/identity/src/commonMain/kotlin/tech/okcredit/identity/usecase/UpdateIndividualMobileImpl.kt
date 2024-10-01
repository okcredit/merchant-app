package tech.okcredit.identity.usecase

import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding
import tech.okcredit.identity.IdentityRepository
import tech.okcredit.identity.contract.usecase.UpdateIndividualMobile

@Inject
@ContributesBinding(AppScope::class)
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
