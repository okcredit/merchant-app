package app.okcredit.ledger.contract.model

import okcredit.base.units.Paisa

sealed class Account(val accountType: AccountType) {
    abstract val id: String
    abstract val businessId: String
    abstract val name: String
    abstract val mobile: String?
    abstract val balance: Paisa
    abstract val profileImage: String?
    abstract val registered: Boolean
}

fun Account.isCustomer() = this.accountType == AccountType.CUSTOMER
fun Account.isSupplier() = this.accountType == AccountType.SUPPLIER
