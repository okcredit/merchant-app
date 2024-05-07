package tech.okcredit.ab.remote

import kotlinx.serialization.Serializable

@Serializable
data class Profile(
    val merchant_id: String,
    val features: Map<String, Boolean>,
    val experiments: Map<String, Experiment>,
)

@Serializable
data class Experiment(
    val name: String,
    val status: Int,
    val variant: String,
    val vars: Map<String, String>,
)

@Serializable
data class GetProfileResponse(
    val profile: Profile,
)

@Serializable
data class AcknowledgementRequest(
    val device_id: String,
    val type: Int,
    val time: Long,
    val experiments: List<Experiment>,
)

@Serializable
data class DisableFeatureRequest(
    val feature: String,
    val merchantIds: List<String>,
    val reason: String,
)

@Serializable
data class EnableFeatureRequest(
    val feature: String,
    val merchantIds: List<String>,
)
