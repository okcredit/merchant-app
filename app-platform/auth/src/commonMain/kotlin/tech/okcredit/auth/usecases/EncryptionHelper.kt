package tech.okcredit.auth.usecases

import me.tatarka.inject.annotations.Inject

@Inject
class EncryptionHelper {

    fun encrypt(data: String): String {
        return data
    }

    fun decrypt(data: String): String {
        return data
    }
}
