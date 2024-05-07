package tech.okcredit.identity.remote.response
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetIndividualResponse(
    @SerialName("business_ids")
    val businessIds: List<String>,
    @SerialName("individual_user")
    val individualUser: IndividualUser,
)

@Serializable
data class IndividualUser(
    @SerialName("alternate_mobile")
    val alternateMobile: String? = null,
    @SerialName("app_lock_opt_in")
    val appLockOptIn: Boolean? = null,
    @SerialName("fingerprint_lock_opt_in")
    val fingerprintLockOptIn: Boolean? = null,
    @SerialName("four_digit_pin_in")
    val fourDigitPinIn: Boolean? = null,
    @SerialName("monthly_reminder_enabled")
    val monthlyReminderEnabled: Boolean? = null,
    @SerialName("payment_password_enabled")
    val paymentPasswordEnabled: Boolean? = null,
    @SerialName("referral_link")
    val referralLink: String? = null,
    @SerialName("self_reminder_enabled")
    val selfReminderEnabled: Boolean? = null,
    @SerialName("self_reminder_period")
    val selfReminderPeriod: String? = null,
    @SerialName("user")
    val user: User,
    @SerialName("whatsapp_opt_in")
    val whatsappOptIn: Boolean? = null,
)

@Serializable
data class User(
    @SerialName("about")
    val about: String? = null,
    @SerialName("address")
    val address: Address? = null,
    @SerialName("create_time")
    val createTime: Long? = null,
    @SerialName("display_name")
    val displayName: String? = null,
    @SerialName("email")
    val email: String? = null,
    @SerialName("id")
    val id: String,
    @SerialName("lang")
    val lang: String? = null,
    @SerialName("mobile")
    val mobile: String,
    @SerialName("profile_image")
    val profileImage: String? = null,
    @SerialName("register_time")
    val registerTime: Long? = null,
    @SerialName("registered")
    val registered: Boolean? = null,
    @SerialName("type")
    val type: Int? = null,
    @SerialName("update_time")
    val updateTime: Long? = null,
)
