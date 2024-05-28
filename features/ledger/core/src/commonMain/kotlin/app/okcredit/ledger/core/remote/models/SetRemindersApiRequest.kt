package app.okcredit.ledger.core.remote.models

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class SetRemindersApiRequest(
    @SerialName(value = "reminders")
    val reminders: List<LastReminderSendTime>,
)

@kotlinx.serialization.Serializable
data class LastReminderSendTime(
    @SerialName(value = "customer_id")
    val customerId: String,
    @SerialName(value = "last_reminder_sent")
    val lastReminderSendTime: Long = 0,
)
