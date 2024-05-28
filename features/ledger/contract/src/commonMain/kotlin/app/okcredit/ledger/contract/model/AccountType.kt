package app.okcredit.ledger.contract.model

enum class AccountType {
    SUPPLIER,
    CUSTOMER,
}

fun AccountType.isCustomer() = this == AccountType.CUSTOMER
