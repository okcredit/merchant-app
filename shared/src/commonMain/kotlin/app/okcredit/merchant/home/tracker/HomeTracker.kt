package app.okcredit.merchant.home.tracker

import app.okcredit.merchant.home.tracker.HomeTracker.Event.ACCOUNT_CLICKED
import app.okcredit.merchant.home.tracker.HomeTracker.Event.COMPLETE_KYC_CLICKED
import app.okcredit.merchant.home.tracker.HomeTracker.Event.COPY_CLICKED
import app.okcredit.merchant.home.tracker.HomeTracker.Event.HOME_SCREEN_VIEWED
import app.okcredit.merchant.home.tracker.HomeTracker.Event.SEND_PAYMENT_LINK_CLICKED
import app.okcredit.merchant.home.tracker.HomeTracker.Event.TNC_CLICKED
import app.okcredit.merchant.home.tracker.HomeTracker.Key.HAS_PROFILE_IMAGE
import app.okcredit.merchant.home.tracker.HomeTracker.Key.KYC_STATUS
import app.okcredit.merchant.home.tracker.HomeTracker.Key.SCREEN
import me.tatarka.inject.annotations.Inject
import tech.okcredit.analytics.AnalyticsProvider

@Inject
class HomeTracker constructor(
    private val analyticsProvider: Lazy<AnalyticsProvider>,
) {

    object Key {
        const val SCREEN = "screen"
        const val KYC_STATUS = "kyc_status"
        const val HAS_PROFILE_IMAGE = "has_profile_image"
    }

    object Event {
        const val HOME_SCREEN_VIEWED = "home_screen_viewed"
        const val ACCOUNT_CLICKED = "home_screen_account_clicked"
        const val COMPLETE_KYC_CLICKED = "home_screen_complete_kyc_clicked"
        const val TNC_CLICKED = "home_screen_tnc_clicked"
        const val SEND_PAYMENT_LINK_CLICKED = "home_screen_send_payment_link_clicked"
        const val COPY_CLICKED = "home_screen_copy_clicked"
    }

    fun trackHomeScreenViewed(screen: String) {
        val properties = hashMapOf(
            SCREEN to screen,
        )
        analyticsProvider.value.logProductEvent(HOME_SCREEN_VIEWED, properties)
    }

    fun trackAccountClicked(screen: String, kycStatus: String, hasProfileImage: Boolean) {
        val properties = hashMapOf(
            SCREEN to screen,
            KYC_STATUS to kycStatus,
            HAS_PROFILE_IMAGE to hasProfileImage,
        )
        analyticsProvider.value.logProductEvent(ACCOUNT_CLICKED, properties)
    }

    fun trackCompleteKycClicked(screen: String, kycStatus: String) {
        val properties = hashMapOf(
            SCREEN to screen,
            KYC_STATUS to kycStatus,
        )
        analyticsProvider.value.logProductEvent(COMPLETE_KYC_CLICKED, properties)
    }

    fun trackTncClicked(screen: String, kycStatus: String) {
        val properties = hashMapOf(
            SCREEN to screen,
            KYC_STATUS to kycStatus,
        )
        analyticsProvider.value.logProductEvent(TNC_CLICKED, properties)
    }

    fun trackSendPaymentLinkClicked(screen: String, kycStatus: String) {
        val properties = hashMapOf(
            SCREEN to screen,
            KYC_STATUS to kycStatus,
        )
        analyticsProvider.value.logProductEvent(SEND_PAYMENT_LINK_CLICKED, properties)
    }

    fun trackCopyClicked(screen: String, kycStatus: String) {
        val properties = hashMapOf(
            SCREEN to screen,
            KYC_STATUS to kycStatus,
        )
        analyticsProvider.value.logProductEvent(COPY_CLICKED, properties)
    }
}
