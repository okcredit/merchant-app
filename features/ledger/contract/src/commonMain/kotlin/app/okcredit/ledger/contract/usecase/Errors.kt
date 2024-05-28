package app.okcredit.ledger.contract.usecase

class InvalidNameError : Exception()

class MobileConflictError(val accountId: String) : Exception()

class DeletedCustomerError(val accountId: String) : Exception()

class CyclicAccountError(val accountId: String) : Exception()

class CustomerNotFoundError : Exception()

class BalanceNonZeroError : Exception()

class PermissionDeniedError : Exception()

class InvalidAmountError : Exception()
