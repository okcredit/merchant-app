package app.okcredit.staff_link.data.remote.response
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateSubscriptionResponse(
    @SerialName(value = "link_id")
    val linkId: String
)
