package app.okcredit.ledger.core.remote.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdateSupplierResponse(
    @SerialName("supplier")
    val supplier: ApiSupplier,
)