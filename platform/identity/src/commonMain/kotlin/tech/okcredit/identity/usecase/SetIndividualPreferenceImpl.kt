package tech.okcredit.identity.usecase

import kotlinx.coroutines.flow.first
import me.tatarka.inject.annotations.Inject
import okcredit.base.randomUUID
import tech.okcredit.identity.IdentityRepository
import tech.okcredit.identity.contract.PreferenceKey
import tech.okcredit.identity.contract.model.Individual
import tech.okcredit.identity.contract.usecase.SetIndividualPreference
import tech.okcredit.identity.remote.request.UpdateIndividualRequest
import tech.okcredit.identity.remote.response.IndividualUser
import tech.okcredit.identity.remote.response.User

@Inject
class SetIndividualPreferenceImpl constructor(
    private val repository: IdentityRepository,
) : SetIndividualPreference {

    override suspend fun execute(key: String, value: String, businessId: String) {
        val individual = repository.getIndividual(randomUUID(), false).first()
        repository.updateIndividual(buildUpdateIndividualRequest(individual, key, value))
        repository.setIndividualPreference(key, value)
    }

    private fun buildUpdateIndividualRequest(
        individual: Individual,
        key: String,
        value: String,
    ): UpdateIndividualRequest {
        return when (key) {
            PreferenceKey.WHATSAPP.key -> {
                UpdateIndividualRequest(
                    individual_user_id = individual.id,
                    individual_user = IndividualUser(
                        whatsappOptIn = value.toBoolean(),
                        user = User(id = individual.id, mobile = individual.mobile),
                    ),
                    update_whatsapp_opt_in = true,
                )
            }
            PreferenceKey.PAYMENT_PASSWORD.key -> {
                UpdateIndividualRequest(
                    individual_user_id = individual.id,
                    individual_user = IndividualUser(
                        paymentPasswordEnabled = value.toBoolean(),
                        user = User(id = individual.id, mobile = individual.mobile),
                    ),
                    update_payment_password_enabled = true,
                )
            }
            PreferenceKey.APP_LOCK.key -> {
                UpdateIndividualRequest(
                    individual_user_id = individual.id,
                    individual_user = IndividualUser(
                        appLockOptIn = value.toBoolean(),
                        user = User(id = individual.id, mobile = individual.mobile),
                    ),
                    update_app_lock_opt_in = true,
                )
            }
            PreferenceKey.FINGER_PRINT_LOCK.key -> {
                UpdateIndividualRequest(
                    individual_user_id = individual.id,
                    individual_user = IndividualUser(
                        fingerprintLockOptIn = value.toBoolean(),
                        user = User(id = individual.id, mobile = individual.mobile),
                    ),
                    update_fingerprint_lock_opt_in = true,
                )
            }
            PreferenceKey.FOUR_DIGIT_PIN.key -> {
                UpdateIndividualRequest(
                    individual_user_id = individual.id,
                    individual_user = IndividualUser(
                        fourDigitPinIn = value.toBoolean(),
                        user = User(id = individual.id, mobile = individual.mobile),
                    ),
                    update_four_digit_pin_opt_in = true,
                )
            }
            PreferenceKey.LANGUAGE.key -> {
                UpdateIndividualRequest(
                    individual_user_id = individual.id,
                    individual_user = IndividualUser(
                        user = User(id = individual.id, mobile = individual.mobile, lang = value),
                    ),
                    update_lang = true,
                )
            }
            else -> throw IllegalArgumentException("Unknown key : $key")
        }
    }
}
