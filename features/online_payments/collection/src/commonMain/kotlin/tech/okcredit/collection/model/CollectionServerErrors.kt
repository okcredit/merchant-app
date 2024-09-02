package tech.okcredit.collection.model

object CollectionServerErrors {
    // bank account validation errors
    class InvalidAccountNumber : Exception()

    class InvalidIFSCcode : Exception()

    class DailyLimitExceeded : Exception()

    // upi validation errors
    class InvalidAPaymentAddress : Exception()

    class AddressNotFound : Exception()
}
