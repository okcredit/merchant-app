package app.okcredit.ledger.core.remote.models

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class GetTransactionsRequest(
    @SerialName("txn_req")
    val txnReq: TransactionsRequest,
)

@kotlinx.serialization.Serializable
data class TransactionsRequest(
    // type of transactions (MERCHANT = 0, ACCOUNT = 1)
    @SerialName("type")
    val type: Int,
    // filter to get specific account transactions.
    @SerialName("account_id")
    val accountId: String? = null,
    // denotes role of a merchant in an account (UNKNOWN = 0, SELLER = 1, BUYER = 2)
    @SerialName("role")
    val role: Int? = null,
    @SerialName("start_time")
    val startTime: Long? = null,
    @SerialName("end_time")
    val endTime: Long? = null,
    @SerialName("exclude_deleted")
    val excludeDeleted: Long? = null,
    // denotes ordering of transactions (UNKNOWN = 0, UPDATE_TIME = 1, BILL_DATE = 2)
    @SerialName("order_by")
    val orderBy: Int? = 1,
    @SerialName("start_time_ms")
    val startTimeMs: Long? = null,
    @SerialName("end_time_ms")
    val endTimeMs: Long? = null,
)
