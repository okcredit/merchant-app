package tech.okcredit.auth.local

import com.russhwolf.settings.ExperimentalSettingsApi
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import me.tatarka.inject.annotations.Inject
import tech.okcredit.auth.Grant
import tech.okcredit.auth.SettingsFactory
import tech.okcredit.auth.remote.AuthRemoteSource
import tech.okcredit.auth.usecases.EncryptionHelper

@OptIn(ExperimentalSettingsApi::class, DelicateCoroutinesApi::class)
@Inject
class AuthLocalSource constructor(
    settingsFactory: SettingsFactory,
    private val encryptionHelper: EncryptionHelper,
) {

    companion object {
        internal const val LATEST_STORE_VERSION = 2

        internal const val KEY_LOCAL_AUTH_VERSION = "version"
        internal const val KEY_MOBILE = "mobile"
        internal const val KEY_ACCESS_TOKEN = "access_token"
        internal const val KEY_REFRESH_TOKEN = "refresh_token"
        internal const val KEY_EXPIRE_TIME = "expire_time"
        internal const val KEY_ACCESS_TOKEN_COOKIE = "key_access_token_cookie"
        internal const val KEY_PASSWORD_HASH = "password_hash_"
        internal const val CURRENT_MOBILE_OTP_TOKEN = "current_mobile_otp_token"
        internal const val NEW_MOBILE_OTP_TOKEN = "new_mobile_otp_token"
        internal const val KEY_SERVER_AUTH_VERSION = "server_auth_version"
    }

    private val prefs by lazy { settingsFactory.create() }

    private var inMemoryAuthGrant: Grant? = null

    init {
        GlobalScope.launch {
            getGrant()
        }
    }

    suspend fun setCurrentMobileOTPToken(token: String) {
        prefs.putString(CURRENT_MOBILE_OTP_TOKEN, token)
    }

    suspend fun setNewMobileOtpToken(token: String) {
        prefs.putString(NEW_MOBILE_OTP_TOKEN, token)
    }

    suspend fun getNewMobileOtpToken(): String? {
        return prefs.getStringOrNull(NEW_MOBILE_OTP_TOKEN)
    }

    suspend fun getCurrentMobileOtpToken(): String? {
        return prefs.getStringOrNull(CURRENT_MOBILE_OTP_TOKEN)
    }

    fun observeAuthStateChanges(): Flow<Boolean> =
        prefs.getStringFlow(KEY_ACCESS_TOKEN, "")
            .map { it.isNotEmpty() }
            .distinctUntilChanged()

    suspend fun getGrant(): Grant? = prefs.run {
        val accessToken = getStringOrNull(KEY_ACCESS_TOKEN) ?: return null
        val refreshToken = getStringOrNull(KEY_REFRESH_TOKEN) ?: return null
        val expireTime = (getLong(KEY_EXPIRE_TIME, 0))
        val accessTokenCookie = getStringOrNull(KEY_ACCESS_TOKEN_COOKIE) ?: return null
        val version =
            getInt(KEY_SERVER_AUTH_VERSION, AuthRemoteSource.LATEST_SERVER_AUTH_VERSION)
        inMemoryAuthGrant = Grant(
            accessToken = (accessToken),
            refreshToken = (refreshToken),
            expireTime = expireTime,
            accessTokenCookie = (accessTokenCookie),
            version = version,
        )

        return@run inMemoryAuthGrant
    }

    suspend fun setGrant(grant: Grant) {
        prefs.putString(KEY_ACCESS_TOKEN, (grant.accessToken))
        prefs.putString(KEY_ACCESS_TOKEN_COOKIE, (grant.accessTokenCookie))
        prefs.putString(KEY_REFRESH_TOKEN, (grant.refreshToken))
        prefs.putLong(KEY_EXPIRE_TIME, grant.expireTime)
        prefs.putInt(KEY_SERVER_AUTH_VERSION, grant.version)
    }

    fun deleteAllExceptMobile() = runBlocking {
        val encryptedMobile = prefs.getString(KEY_MOBILE, "")
        prefs.clear()
        prefs.putString(KEY_MOBILE, encryptedMobile)
    }

    suspend fun getMobile(): String? {
        val encryptedMobile = prefs.getStringOrNull(KEY_MOBILE) ?: return null
        return encryptionHelper.decrypt(encryptedMobile)
    }

    suspend fun setMobile(mobile: String) {
        prefs.putString(KEY_MOBILE, encryptionHelper.encrypt(mobile))
    }

    suspend fun getPasswordHash(): String? = prefs.getStringOrNull(KEY_PASSWORD_HASH)

    suspend fun setPasswordHash(passwordHash: String) {
        prefs.putString(KEY_PASSWORD_HASH, passwordHash)
    }

    suspend fun getVersion(): Int {
        return prefs.getInt(KEY_LOCAL_AUTH_VERSION, LATEST_STORE_VERSION)
    }

    fun getCookieWithExpiryTime(): Flow<Pair<String, Long>> {
        return prefs.getStringFlow(KEY_ACCESS_TOKEN_COOKIE, "")
            .map {
                if (inMemoryAuthGrant == null) {
                    getGrant()
                }
                inMemoryAuthGrant!!.accessTokenCookie to inMemoryAuthGrant!!.expireTime
            }
    }

    suspend fun isAuthenticated(): Boolean {
        return if (inMemoryAuthGrant != null) {
            inMemoryAuthGrant?.accessToken?.isNotEmpty() ?: false
        } else {
            prefs.getStringOrNull(KEY_ACCESS_TOKEN)?.isNotEmpty() ?: false
        }
    }

    suspend fun clearLocalData() {
        prefs.clear()
        inMemoryAuthGrant = null
    }
}
