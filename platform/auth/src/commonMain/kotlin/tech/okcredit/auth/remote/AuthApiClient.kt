package tech.okcredit.auth.remote

// api spec
interface AuthApiClient {

    companion object {
        const val GRANT_TYPE_REFRESH_TOKEN = "refresh_token"
        const val GRANT_TYPE_OTP = "otp"
        const val GRANT_TYPE_TRUECALLER = "truecaller"
        const val GRANT_TYPE_TRUECALLER_CALL = "truecaller_call"

        const val OTP_MODE_PUSH = "PUSH"
        const val OTP_MODE_PULL = "PULL"

        // by default it is kept as 10 seconds
        const val OTP_RETRY_TIME = 10

        // process of entering OTP should reset after 300 seconds, if the user is on the OTP screen
        const val OTP_FLOW_EXPIRY_TIME = 300

        const val OKC_LOGIN_FLOW_ID = "okc-login-flow-id"
    }

    enum class RetryDestination(val key: Int) {
        PRIMARY(0),
        SECONDARY(1),
        ;

        companion object {
            fun getDestination(code: Int?) = when (code) {
                SECONDARY.key -> SECONDARY
                else -> PRIMARY
            }
        }
    }

    enum class RequestOtpMedium(val key: Int) {
        SMS(0),
        WHATSAPP(1),
        CALL(2),
        ;

        companion object {
            fun getMedium(code: Int?) = when (code) {
                WHATSAPP.key -> WHATSAPP
                CALL.key -> CALL
                else -> SMS
            }
        }
    }
}
