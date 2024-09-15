package app.okcredit.ledger.contract.model

import okcredit.base.units.Paisa
import okcredit.base.units.Timestamp
import okcredit.base.units.paisa

data class Supplier(
    override val id: String,
    override val businessId: String,
    override val name: String,
    override val mobile: String?,
    override val profileImage: String?,
    override val registered: Boolean,
    override val address: String?,
    val createdAt: Timestamp,
    val updatedAt: Timestamp,
    val settings: SupplierSettings,
    val summary: SupplierSummary,
    val status: AccountStatus
) : Account(AccountType.SUPPLIER) {

    override val balance: Paisa
        get() = summary.balance

    val blockedBySelf: Boolean
        get() = status == AccountStatus.BLOCKED

    data class SupplierSettings(
        val lang: String,
        val txnAlertEnabled: Boolean,
        val blockedBySupplier: Boolean,
        val addTransactionRestricted: Boolean,
    )

    data class SupplierSummary(
        val balance: Paisa = 0.paisa,
        val transactionCount: Long = 0L,
        val lastActivity: Timestamp = Timestamp(0L),
        val lastActivityMetaInfo: Int = 4,
        val lastPayment: Timestamp? = null,
        val lastAmount: Paisa? = null,
    )
}