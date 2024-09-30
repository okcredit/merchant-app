package app.okcredit.ledger.ui.delete.usecase

import me.tatarka.inject.annotations.Inject
import tech.okcredit.analytics.AnalyticsProvider

@Inject
class DeleteAccountTracker(
    analyticsProvider: Lazy<AnalyticsProvider>,
) {
    private val analyticsProvider by lazy { analyticsProvider.value }

    companion object {
        const val DELETE_SUPPLIER_SCREEN_CLICK_SETTLEMENT = "DeleteSupplierScreen: Settlement Clicked"
        const val ERROR = "Delete Error"
        const val ADD_TRANSACTION_STARTED = "Add Transaction Started"
        const val DELETE_SUPPLIER_INCORRECT_PASSWORD = "DeleteSupplierScreen: Incorrect Password"
        const val DELETE_SUPPLIER_SCREEN_CLICK_DELETE = "DeleteSupplierScreen: Delete Clicked"
        const val DELETE_CUSTOMER_SCREEN_CLICK_DELETE = "DeleteCustomerScreen: Delete Clicked"
        const val DELETE_RELATIONSHIP = "Delete Relationship"

        const val TYPE = "type"
        const val REASON = "reason"
        const val SCREEN = "screen"
        const val PAYMENT = "Payment"
        const val CREDIT = "Credit"
        const val RELATION = "relation"
        const val SUPPLIER = "Supplier"
        const val CUSTOMER = "Customer"
        const val ACCOUNT_ID = "account_id"
        const val SOURCE = "source"
        const val DELETE_SUPPLIER = "DeleteSupplier"
        const val DELETE_CUSTOMER = "DeleteCustomer"
    }

    fun trackDeletionError(
        isSupplier: Boolean,
        error: Throwable,
    ) {
        analyticsProvider.logProductEvent(
            ERROR,
            mapOf(
                TYPE to if (isSupplier) "DeleteSupplier" else "DeleteCustomer",
                SCREEN to if (isSupplier) "DeleteSupplierActivity" else "DeleteCustomerActivity",
                REASON to error.stackTraceToString(),
            ),
        )
    }

    fun trackDeleteSettleClicked() {
        analyticsProvider.logProductEvent(DELETE_SUPPLIER_SCREEN_CLICK_SETTLEMENT, mapOf())
    }

    fun trackAddTransactionFlowStarted(
        isSupplier: Boolean,
        isPayment: Boolean,
        accountId: String,
    ) {
        val properties = mutableMapOf<String, Any>()
        properties[TYPE] = if (isPayment) PAYMENT else CREDIT
        properties[RELATION] = if (isSupplier) SUPPLIER else CUSTOMER
        if (accountId.isNotBlank()) properties[ACCOUNT_ID] = accountId
        properties[SOURCE] = if (isSupplier) DELETE_SUPPLIER else DELETE_CUSTOMER
        analyticsProvider.logProductEvent(ADD_TRANSACTION_STARTED, properties)
    }

    fun trackIncorrectPassword() {
        analyticsProvider.logProductEvent(DELETE_SUPPLIER_INCORRECT_PASSWORD, mapOf())
    }

    fun trackDeleteRelationshipClicked(
        isSupplier: Boolean,
        accountId: String,
    ) {
        analyticsProvider.logProductEvent(
            eventName = if (isSupplier) DELETE_SUPPLIER_SCREEN_CLICK_DELETE else DELETE_CUSTOMER_SCREEN_CLICK_DELETE,
            properties = mapOf(
                ACCOUNT_ID to accountId,
            ),
        )
    }

    fun trackDeleteRelationship(
        isSupplier: Boolean,
        accountId: String,
    ) {
        val properties = mutableMapOf<String, Any>()
        properties[TYPE] = if (isSupplier) SUPPLIER else CUSTOMER
        if (accountId.isNotBlank()) properties[ACCOUNT_ID] = accountId
        analyticsProvider.logProductEvent(DELETE_RELATIONSHIP, properties)
    }
}
