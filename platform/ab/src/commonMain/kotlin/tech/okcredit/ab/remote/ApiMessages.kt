package tech.okcredit.ab.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Profile(
    @SerialName(value = "merchant_id")
    val merchantId: String,
    @SerialName(value = "features")
    val features: Map<String, Boolean>,
    @SerialName(value = "experiments")
    val experiments: Map<String, Experiment>,
)

@Serializable
data class Experiment(
    @SerialName(value = "name")
    val name: String,
    @SerialName(value = "status")
    val status: Int,
    @SerialName(value = "variant")
    val variant: String,
    @SerialName(value = "vars")
    val vars: Map<String, String>,
)

@Serializable
data class GetProfileResponse(
    @SerialName(value = "profile")
    val profile: Profile,
)

@Serializable
data class AcknowledgementRequest(
    @SerialName(value = "device_id")
    val deviceId: String,
    @SerialName(value = "type")
    val type: Int,
    @SerialName(value = "time")
    val time: Long,
    @SerialName(value = "experiments")
    val experiments: List<Experiment>,
)

@Serializable
data class DisableFeatureRequest(
    @SerialName(value = "feature")
    val feature: String,
    @SerialName(value = "merchantIds")
    val merchantIds: List<String>,
    @SerialName(value = "reason")
    val reason: String,
)

@Serializable
data class EnableFeatureRequest(
    @SerialName(value = "feature")
    val feature: String,
    @SerialName(value = "merchantIds")
    val merchantIds: List<String>,
)
