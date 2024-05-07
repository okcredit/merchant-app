package tech.okcredit.identity.remote.request

import tech.okcredit.identity.remote.response.IndividualUser

@kotlinx.serialization.Serializable
data class UpdateIndividualRequest(
    val individual_user_id: String,
    val individual_user: IndividualUser,
    val update_app_lock_opt_in: Boolean? = null,
    val update_payment_password_enabled: Boolean? = null,
    val update_whatsapp_opt_in: Boolean? = null,
    val update_fingerprint_lock_opt_in: Boolean? = null,
    val update_four_digit_pin_opt_in: Boolean? = null,
    val update_lang: Boolean? = null,
    val current_mobile_otp_token: String? = null,
    val new_mobile_otp_token: String? = null,
    val update_mobile: Boolean? = null,
    val update_alternate_mobile: Boolean? = null,
    val alternate_mobile_otp_token: String? = null,
)
