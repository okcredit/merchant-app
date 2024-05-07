package tech.okcredit.identity.contract.usecase

interface UpdateIndividualMobile {
    suspend fun execute(
        individualId: String,
        mobile: String,
        currentMobileOtpToken: String,
        newMobileOtpToken: String,
    )
}
