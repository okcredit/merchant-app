package okcredit.base.syncer

interface SyncNotificationListener {
    fun onSyncNotification(payload: Map<String, Any?>)
}

enum class SyncNotificationType(val value: String) {
    UNKNOWN("unknown"),
    SUPPLIER_V2("supplier_v2"),
    SUPPLIER_TXNS("supplier_txns"),
    CUSTOMER_V2("customer_v2"),
    CUSTOMER_TXNS("customer_txns"),
    COLLECTION("collection"),
    MERCHANT("merchant"),
    LOGOUT("logout"),
    CUSTOMER_DELETED("customer_deleted"),
    DELETE_CUSTOMER("delete_customer"),
    DUE_INFO("due_info"),
    SYNC_CONTACT("sync_contact"),
    SYNC_PROFILE("sync_profile"),
    RECOVERY_ACTION("recovery_action"),
    CUSTOMER_TXN_RESTRICTION("sync_customer_txn_restriction"),
    SYNC_BILLS("sync_bills"),
    COLLECTION_DESTINATION("collection_destination"),
    COLLECTION_KYC("collection-kyc"),
    PAYMENT_SYNC("payment_sync"),
    INDIVIDUAL("individual"),
    MERCHANT_PAYMENT("merchant_payment"),
    CUSTOMER_PROFILE_CREATED("customer_profile_created"),
    SUPPLIER_PROFILE_CREATED("supplier_profile_created"),
    UPLOAD_DB_FILES("upload_db_files"),
    ENABLE_REMOTE_LOGGING("ENABLE_REMOTE_LOGGING"), // For 24 hours // All caps cos dev notification
    SYNC_BNPL_MODE_INFO("sync_bnpl_mode_info"),
    SEND_MONEY("send_money"),
    DYNAMIC_COMPONENT("dynamic_component"),
    OK_SUBSCRIPTION("subscription_added"),
    AUDIT_FCM("audit_fcm"),
    ;

    companion object
}
