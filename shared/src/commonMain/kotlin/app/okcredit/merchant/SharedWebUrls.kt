package app.okcredit.merchant

import me.tatarka.inject.annotations.Inject
import okcredit.base.di.Flavor

@Inject
class SharedWebUrls(flavor: Flavor) {

    private val isStaging = flavor == "staging"

    val tncUrl = "https://www.okcredit.in/terms"

    val privacyUrl = "https://okcredit.in/privacy"

    val kycUrl = if (isStaging) {
        "https://wkyc.staging.okcredit.in/?serviceName=okPay&verify=pan,aadhaar"
    } else {
        "https://wkyc.okcredit.in/?serviceName=okPay&verify=pan,aadhaar"
    }

    val paymentChargesUrl = if (isStaging) {
        "https://account.staging.okcredit.in/client/charging-module/okpay"
    } else {
        "https://account.okcredit.in/client/charging-module/okpay"
    }
}
