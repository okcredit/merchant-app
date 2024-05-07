package tech.okcredit.identity.usecase

import me.tatarka.inject.annotations.Inject
import tech.okcredit.identity.IdentityRepository
import tech.okcredit.identity.contract.BusinessErrors
import tech.okcredit.identity.contract.model.BusinessConstants
import tech.okcredit.identity.contract.model.UpdateBusinessRequest
import tech.okcredit.identity.contract.usecase.GetActiveBusinessId
import tech.okcredit.identity.contract.usecase.Request
import tech.okcredit.identity.contract.usecase.UpdateBusiness

@Inject
class UpdateBusinessImpl(
    private val repository: IdentityRepository,
    private val getActiveBusinessId: GetActiveBusinessId,
) : UpdateBusiness {

    override suspend fun execute(request: Request) {
        val businessId = getActiveBusinessId.execute()
        updateBusiness(request, businessId)
    }

    private suspend fun updateBusiness(req: Request, businessId: String) {
        when (req.inputType) {
            BusinessConstants.BUSINESS_NAME -> {
                repository.updateBusiness(
                    UpdateBusinessRequest.UpdateBusinessName(req.updatedValue!!),
                    businessId,
                )
            }
            BusinessConstants.EMAIL -> {
                validateEmail(req.updatedValue!!)
                repository.updateBusiness(UpdateBusinessRequest.UpdateEmail(req.updatedValue!!), businessId)
            }
            BusinessConstants.ADDRESS -> {
                repository.updateBusiness(
                    UpdateBusinessRequest.UpdateAddress(
                        address = req.address?.first!!,
                        latitude = req.address?.second!!,
                        longitude = req.address?.third!!,
                    ),
                    businessId,
                )
            }
            BusinessConstants.ABOUT -> {
                repository.updateBusiness(UpdateBusinessRequest.UpdateAbout(req.updatedValue!!), businessId)
            }
            BusinessConstants.PERSON_NAME -> {
                repository.updateBusiness(UpdateBusinessRequest.UpdateName(req.updatedValue!!), businessId)
            }
            BusinessConstants.PROFILE_IMAGE -> {
                repository.updateBusiness(
                    UpdateBusinessRequest.UpdateProfileImage(req.updatedValue!!),
                    businessId,
                )
            }
            BusinessConstants.CATEGORY -> {
                repository.updateBusiness(
                    UpdateBusinessRequest.UpdateCategory(
                        req.category?.first,
                        req.category?.second,
                    ),
                    businessId,
                )
            }
            BusinessConstants.BUSINESS_TYPE -> {
                repository.updateBusiness(
                    UpdateBusinessRequest.UpdateBusinessType(req.businessType?.id!!),
                    businessId,
                )
            }
            BusinessConstants.OTHER_CATEGORY -> {
                if (req.category?.second.isNullOrBlank()) { // if enter category name is empty , that means  user wants to remove category
                    repository.updateBusiness(UpdateBusinessRequest.UpdateCategory(null, null), businessId)
                } else {
                    repository.updateBusiness(
                        UpdateBusinessRequest.UpdateCategory(
                            req.category?.first,
                            req.category?.second,
                        ),
                        businessId,
                    )
                }
            }
            else -> {
                repository.updateBusiness(
                    UpdateBusinessRequest.UpdateBusinessName(req.updatedValue!!),
                    businessId,
                )
            }
        }
    }
}

internal fun validateEmail(email: String): String {
    return when {
        email.isEmpty() -> ""
        isValidEmail(email).not() -> throw BusinessErrors.InvalidEmail()
        else -> ""
    }
}

fun isValidEmail(text: String) = text.contains("@")
