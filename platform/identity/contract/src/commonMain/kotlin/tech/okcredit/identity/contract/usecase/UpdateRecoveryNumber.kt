package tech.okcredit.identity.contract.usecase

interface UpdateRecoveryNumber {
    suspend fun execute(
        individualId: String,
        primaryNumber: String,
        recoveryNumberOtpToken: String,
    )
}
