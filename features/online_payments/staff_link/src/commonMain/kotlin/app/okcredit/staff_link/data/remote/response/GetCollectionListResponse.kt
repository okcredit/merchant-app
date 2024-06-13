package app.okcredit.staff_link.data.remote.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetCollectionListResponse(
    @SerialName(value = "collections_lists")
    val collectionsLists: List<CollectionsLists>?,
    @SerialName(value = "pagination_details")
    val paginationDetails: PaginationDetails?
) {
    @Serializable
    data class CollectionsLists(
        @SerialName(value = "associated_account_ids")
        val associatedAccountIds: List<String>,
        @SerialName(value = "business_id")
        val businessId: String,
        @SerialName(value = "create_time")
        val createTime: Long,
        @SerialName(value = "due_config")
        val dueConfig: DueConfig?,
        @SerialName(value = "filters")
        val filters: Filters?,
        @SerialName(value = "id")
        val id: String,
        @SerialName(value = "is_active")
        val isActive: Boolean,
        @SerialName(value = "name")
        val name: String,
        @SerialName(value = "summary")
        val summary: Summary?,
        @SerialName(value = "type")
        val type: Int,
        @SerialName(value = "update_time")
        val updateTime: Long,
        @SerialName(value = "url")
        val url: String,
        @SerialName(value = "usage_type")
        val usageType: Int
    ) {
        @Serializable
        data class DueConfig(
            @SerialName(value = "config")
            val config: Int,
            @SerialName(value = "end_time")
            val endTime: String,
            @SerialName(value = "start_time")
            val startTime: String
        )

        @Serializable
        data class Filters(
            @SerialName(value = "sort_by")
            val sortBy: String?
        )

        @Serializable
        data class Summary(
            @SerialName(value = "action_required_bills_count")
            val actionRequiredBillsCount: Int?,
            @SerialName(value = "due_amount")
            val dueAmount: Long,
            @SerialName(value = "id")
            val id: String,
            @SerialName(value = "merchant_id")
            val merchantId: String,
            @SerialName(value = "unsettled_bills_count")
            val unsettledBillsCount: Int?
        )
    }

    @Serializable
    data class PaginationDetails(
        @SerialName(value = "limit")
        val limit: Int?,
        @SerialName(value = "page")
        val page: Int,
        @SerialName(value = "per_page")
        val perPage: Int,
        @SerialName(value = "total_items")
        val totalItems: Int,
        @SerialName(value = "total_pages")
        val totalPages: Int
    )
}
