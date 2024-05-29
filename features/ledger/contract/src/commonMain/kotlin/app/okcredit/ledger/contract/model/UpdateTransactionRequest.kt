package app.okcredit.ledger.contract.model

import okcredit.base.units.Paisa

sealed class UpdateTransactionRequest {

    data class UpdateAmount(
        val transactionId: String,
        val accountType: AccountType,
        val amount: Paisa,
    ) : UpdateTransactionRequest()

    data class UpdateNote(
        val transactionId: String,
        val accountType: AccountType,
        val note: String?,
    ) : UpdateTransactionRequest()
}
