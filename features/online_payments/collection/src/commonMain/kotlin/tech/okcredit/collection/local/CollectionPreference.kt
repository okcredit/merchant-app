package tech.okcredit.collection.local

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import me.tatarka.inject.annotations.Inject
import okcredit.base.local.BasePreferences
import okcredit.base.local.Scope

@Inject
class CollectionPreference(
    factory: CollectionSettingsFactory,
) : BasePreferences(
    prefs = lazy { factory.create() },
) {

    companion object {
        const val PREF_ONLINE_PAYMENTS_LAST_SYNC_TIME = "online_payments_last_sync_time"
        private const val INVALIDATED_ONLINE_PAYMENTS_LAST_SYNC_TIME = 0L

        const val PREF_INDIVIDUAL_SHOW_PAYMENT_REMINDER_EDUCATION = "show_payment_reminder_education"
        const val PREF_BUSINESS_PAYMENT_REMINDER_FIRST_TIME = "send_reminder_after_bank_addition_first_time"
        const val PREF_BUSINESS_SOUND_NOTIFICATION = "sound_notification"

        const val PREF_BUSINESS_SHOW_TPAP_ONBOARDING_TUTORIAL = "show_tpap_onboarding_tutorial"
    }

    suspend fun clearCollectionPref() {
        return clear()
    }

    suspend fun setPaymentReminderEducation(show: Boolean) {
        set(PREF_INDIVIDUAL_SHOW_PAYMENT_REMINDER_EDUCATION, show, Scope.Individual)
    }

    suspend fun setPaymentReminderAfterFirstDestinationAdded(show: Boolean, businessId: String) {
        set(PREF_BUSINESS_PAYMENT_REMINDER_FIRST_TIME, show, Scope.Business(businessId))
    }

    suspend fun getPaymentReminderEducation(): Boolean {
        return getBoolean(PREF_INDIVIDUAL_SHOW_PAYMENT_REMINDER_EDUCATION, Scope.Individual).first()
    }

    suspend fun setOnlinePaymentsLastSyncTime(dateTime: Long, businessId: String) {
        set(PREF_ONLINE_PAYMENTS_LAST_SYNC_TIME, (dateTime), Scope.Business(businessId))
    }

    fun getOnlinePaymentsLastSyncTime(businessId: String): Flow<Long?> {
        return getLong(PREF_ONLINE_PAYMENTS_LAST_SYNC_TIME, Scope.Business(businessId))
    }

    suspend fun invalidateOnlinePaymentsLastSyncTime(businessId: String) {
        set(PREF_ONLINE_PAYMENTS_LAST_SYNC_TIME, INVALIDATED_ONLINE_PAYMENTS_LAST_SYNC_TIME, Scope.Business(businessId))
    }

    suspend fun getPaymentReminderAfterFirstDestinationAdded(businessId: String): Boolean {
        return getBoolean(PREF_BUSINESS_PAYMENT_REMINDER_FIRST_TIME, Scope.Business(businessId)).first()
    }

    fun shouldPlaySoundNotification(businessId: String): Flow<Boolean> {
        return getBoolean(PREF_BUSINESS_SOUND_NOTIFICATION, Scope.Business(businessId), true)
    }

    suspend fun setSoundNotificationPref(businessId: String, enabled: Boolean) {
        set(PREF_BUSINESS_SOUND_NOTIFICATION, enabled, Scope.Business(businessId))
    }

    fun shouldShowTpapOnboardingTutorial(businessId: String): Flow<Boolean> {
        return getBoolean(PREF_BUSINESS_SHOW_TPAP_ONBOARDING_TUTORIAL, Scope.Business(businessId), true)
    }

    suspend fun setTpapOnboardingTutorialAsShown(businessId: String) {
        set(PREF_BUSINESS_SHOW_TPAP_ONBOARDING_TUTORIAL, false, Scope.Business(businessId))
    }
}
