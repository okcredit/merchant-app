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
    val status: AccountStatus,
    val gstNumber: String?,
    val accountUrl: String?,
    val createdAt: Timestamp,
    val updatedAt: Timestamp,
    val settings: CustomerSettings,
    val summary: CustomerSummary,
    val dueDate: Timestamp? = null,
) : Account(AccountType.CUSTOMER) {

    override val balance: Paisa
        get() = summary.balance

    val blockedBySelf: Boolean
        get() = status == AccountStatus.BLOCKED

    data class CustomerSettings(
        val txnAlertEnabled: Boolean = false,
        val restrictContactSync: Boolean = false,
        val blockedByCustomer: Boolean = false,
        val addTransactionRestricted: Boolean = false,
        val reminderMode: String? = null,
        val language: String? = null,
    )

    /**
     * Possible values for lastActivityMetaInfo -
     * 0 - deleted credit txn
     * 1 - deleted payment txn
     * 2 - normal credit txn
     * 3 - normal payment txn
     * 4 - customer just added, no txn present
     * 5 - processing transaction
     * 6 - deleted discount txn
     * 7 - normal discount txn
     * 8 - updated credit txn
     * 9 - updated payment txn
     **/
    data class CustomerSummary(
        val balance: Paisa = 0.paisa,
        val transactionCount: Long = 0,
        val lastActivity: Timestamp = Timestamp(0L),
        val lastActivityMetaInfo: Int = 4,
        val lastPayment: Timestamp? = null,
        val lastAmount: Paisa? = null,
        val lastReminderSendTime: Timestamp? = null,
    )
}

enum class AccountStatus {
    ACTIVE,
    BLOCKED,
    DELETED;

    companion object {
        fun from(status: Int): AccountStatus {
            return when (status) {
                1 -> ACTIVE
                2 -> BLOCKED
                3 -> DELETED
                else -> ACTIVE
            }
        }
    }
}
