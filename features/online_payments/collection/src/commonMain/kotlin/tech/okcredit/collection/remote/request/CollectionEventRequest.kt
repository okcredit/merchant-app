package tech.okcredit.collection.remote.request

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class CollectionEventRequest(
    @SerialName("account_id")
    val accountId: String?,
    @SerialName("event_name")
    val eventName: String,
)
