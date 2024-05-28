package app.okcredit.ledger.contract.model

import okcredit.base.units.Paisa
import okcredit.base.units.Timestamp
import okcredit.base.units.paisa

data class Customer(
    override val id: String,
    override val name: String,
    override val mobile: String?,
    override val businessId: String,
    override val profileImage: String?,
    override val registered: Boolean,
    val status: CustomerStatus,
    val gstNumber: String?,
    val accountUrl: String?,
    val createdAt: Timestamp,
    val updatedAt: Timestamp,
    val settings: CustomerSettings,
    val summary: CustomerSummary,
) : Account(AccountType.CUSTOMER) {

    override val balance: Paisa
        get() = summary.balance

    data class CustomerSettings(
        val txnAlertEnabled: Boolean = false,
        val restrictContactSync: Boolean = false,
        val blockedByCustomer: Boolean = false,
        val addTransactionRestricted: Boolean = false,
        val reminderMode: String? = null,
        val language: String? = null,
    )

    data class CustomerSummary(
        val balance: Paisa = 0.paisa,
        val transactionCount: Long = 0,
        val lastActivity: Timestamp = Timestamp(0L),
        val lastActivityMetaInfo: Long = 4,
        val lastPayment: Timestamp? = null,
        val lastAmount: Paisa? = null,
        val lastReminderSendTime: Timestamp? = null,
        val updatedAt: Timestamp? = null,
    )
}

enum class CustomerStatus {
    ACTIVE,
    BLOCKED,
    DELETED,
    ;

    companion object {
        fun from(status: Int): CustomerStatus {
            return when (status) {
                1 -> ACTIVE
                2 -> BLOCKED
                3 -> DELETED
                else -> ACTIVE
            }
        }
    }
}
