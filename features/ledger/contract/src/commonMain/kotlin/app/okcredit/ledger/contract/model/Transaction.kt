package app.okcredit.ledger.contract.model

import okcredit.base.units.Paisa
import okcredit.base.units.Timestamp

data class Transaction(
    val id: String,
    val businessId: String,
    val accountId: String,
    val type: Type,
    val amount: Paisa,
    val note: String?,
    val createdAt: Timestamp,
    val updatedAt: Timestamp? = null,
    val billDate: Timestamp,
    val dirty: Boolean,
    val deleted: Boolean = false,
    val deleteTime: Timestamp? = null,
    val state: State = State.CREATED,
    val category: Category = Category.DEFAULT,
    val createdByCustomer: Boolean = false,
    val deletedByCustomer: Boolean = false,
    val amountUpdated: Boolean = false,
    val amountUpdateTime: Timestamp? = null,
    val referenceId: String? = null,
    val referenceSource: Int? = null,
) {

    enum class State(val code: Int) {
        PROCESSING(0),
        CREATED(1),
        DELETED(2),
        ;

        companion object {
            fun getTransactionState(code: Int) = when (code) {
                PROCESSING.code -> PROCESSING
                CREATED.code -> CREATED
                DELETED.code -> DELETED
                else -> CREATED
            }
        }
    }

    enum class Category(val code: Int) {
        DEFAULT(0),
        DISCOUNT(1),
        AUTO_CREDIT(2),
        ;

        companion object {
            fun getTransactionCategory(code: Int) = when (code) {
                DEFAULT.code -> DEFAULT
                DISCOUNT.code -> DISCOUNT
                AUTO_CREDIT.code -> AUTO_CREDIT
                else -> DEFAULT
            }
        }
    }

    enum class Type(val code: Int) {
        CREDIT(1),
        PAYMENT(2),
        ;

        companion object {
            fun fromCode(type: Int): Type {
                return when (type) {
                    CREDIT.code -> CREDIT
                    PAYMENT.code -> PAYMENT
                    else -> PAYMENT
                }
            }
        }
    }
}

/**
 * Possible values -
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
fun Transaction.lastActivityMetaInfo() = when {
    this.deleted -> {
        when {
            this.type == Transaction.Type.CREDIT -> 0
            this.category == Transaction.Category.DISCOUNT -> 6
            else -> 1
        }
    }
    this.amountUpdated -> {
        when (this.type) {
            Transaction.Type.CREDIT -> 8
            else -> 9
        }
    }
    this.state == Transaction.State.PROCESSING -> 5
    this.type == Transaction.Type.CREDIT -> 2
    this.category == Transaction.Category.DISCOUNT -> 7
    else -> 3
}
