package app.okcredit.ledger.ui.model


enum class AccountType {
    Customer,
    Supplier;

    fun isSupplier() = this == Supplier
}