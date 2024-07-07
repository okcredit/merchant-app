package app.okcredit.ledger.core.remote.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SuppliersResponse(
    @SerialName("suppliers")
    val suppliers: List<ApiSupplier>,
)
