package tech.okcredit.identity

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import me.tatarka.inject.annotations.Inject
import okcredit.base.di.Singleton
import tech.okcredit.identity.contract.PreferenceKey
import tech.okcredit.identity.contract.model.*
import tech.okcredit.identity.contract.model.BusinessType
import tech.okcredit.identity.contract.model.Category
import tech.okcredit.identity.local.BusinessCategory
import tech.okcredit.identity.local.IdentityLocalSource
import tech.okcredit.identity.remote.IdentityRemoteSource
import tech.okcredit.identity.remote.request.UpdateIndividualRequest
import tech.okcredit.identity.remote.response.*
import tech.okcredit.identity.local.Business as DbBusiness
import tech.okcredit.identity.local.Individual as DbIndividual

@Inject
@Singleton
class IdentityRepository(
    private val localSource: IdentityLocalSource,
    private val remoteSource: IdentityRemoteSource,
) {

    suspend fun syncCategories(businessId: String) {
        val remote = remoteSource.getCategories(businessId)
        localSource.saveCategories(
            remote.map {
                BusinessCategory(
                    id = it.id ?: "",
                    name = it.name ?: "",
                    type = it.type?.toLong() ?: 0L,
                    imageUrl = it.image_url,
                    isPopular = it.is_popular ?: false,
                )
            },
        )
    }

    suspend fun syncBusinessTypes(businessId: String) {
        val remote = remoteSource.getBusinessTypes(businessId)
        localSource.saveBusinessTypes(
            remote.map {
                tech.okcredit.identity.local.BusinessType(
                    id = it.id,
                    name = it.name,
                    image_url = it.image_url,
                    title = it.title,
                    sub_title = it.sub_title,
                )
            },
        )
    }

    fun getCategories(): Flow<List<Category>> {
        return localSource.getCategories()
    }

    fun getBusinessTypes(): Flow<List<BusinessType>> {
        return localSource.getBusinessTypes()
    }

    suspend fun updateBusiness(
        updateBusinessRequest: UpdateBusinessRequest,
        businessId: String,
    ) {
        return when (updateBusinessRequest) {
            is UpdateBusinessRequest.UpdateBusinessName -> updateBusinessName(
                updateBusinessRequest.businessName,
                businessId,
            )
            is UpdateBusinessRequest.UpdateBusinessNameOnboarding -> updateBusinessNameOnboarding(
                updateBusinessRequest.businessName,
                businessId,
            )
            is UpdateBusinessRequest.UpdateName -> updatePersonName(
                updateBusinessRequest.personName,
                businessId,
            )
            is UpdateBusinessRequest.UpdateProfileImage -> updateProfileImage(
                updateBusinessRequest.profileImage,
                businessId,
            )
            is UpdateBusinessRequest.UpdateCategory -> updateCategory(
                updateBusinessRequest.categoryId,
                updateBusinessRequest.categoryName,
                businessId,
            )
            is UpdateBusinessRequest.UpdateEmail -> updateEmail(
                updateBusinessRequest.email,
                businessId,
            )
            is UpdateBusinessRequest.UpdateAddress -> updateAddress(
                updateBusinessRequest.address,
                updateBusinessRequest.latitude,
                updateBusinessRequest.longitude,
                businessId,
            )
            is UpdateBusinessRequest.UpdateAbout -> updateAbout(
                updateBusinessRequest.about,
                businessId,
            )
            is UpdateBusinessRequest.UpdateBusinessType -> updateBusinessType(
                updateBusinessRequest.businessTypeId,
                businessId,
            )
            is UpdateBusinessRequest.UpdateProfileInfo -> updateEditProfile(
                businessId,
                updateBusinessRequest.profileInfo,
            )
        }
    }

    private suspend fun updateBusinessType(businessTypeId: String, businessId: String) {
        val local = localSource.getBusiness(businessId).firstOrNull()
        local?.let {
            val apiRequest = tech.okcredit.identity.remote.request.UpdateBusinessRequest(
                business_user_id = local.id,
                business_user = GetBusinessResponseWrapper(
                    user = BusinessUser(
                        id = local.id,
                        mobile = local.mobile,
                        display_name = local.name,
                        create_time = local.createdAt,
                    ),
                    business_type = tech.okcredit.identity.remote.response.BusinessType(
                        id = businessTypeId,
                    ),
                ),
                update_business_type = true,
            )
            remoteSource.updateBusiness(apiRequest, businessId)
            syncBusiness(businessId)
        }
    }

    private suspend fun updateAbout(about: String, businessId: String) {
        val local = localSource.getBusiness(businessId).firstOrNull()
        local?.let {
            val apiRequest = tech.okcredit.identity.remote.request.UpdateBusinessRequest(
                business_user_id = local.id,
                business_user = GetBusinessResponseWrapper(
                    user = BusinessUser(
                        id = local.id,
                        mobile = local.mobile,
                        display_name = local.name,
                        create_time = local.createdAt,
                        about = about,
                    ),
                ),
                update_about = true,
            )
            remoteSource.updateBusiness(apiRequest, businessId)
            syncBusiness(businessId)
        }
    }

    private suspend fun updateAddress(
        address: String,
        latitude: Double,
        longitude: Double,
        businessId: String,
    ) {
        val local = localSource.getBusiness(businessId).firstOrNull()
        local?.let {
            val apiRequest = tech.okcredit.identity.remote.request.UpdateBusinessRequest(
                business_user_id = local.id,
                business_user = GetBusinessResponseWrapper(
                    user = BusinessUser(
                        id = local.id,
                        mobile = local.mobile,
                        display_name = local.name,
                        create_time = local.createdAt,
                        address = Address(
                            text = address,
                            latitude = latitude,
                            longitude = longitude,
                        ),
                    ),
                ),
                update_address = true,
            )
            remoteSource.updateBusiness(apiRequest, businessId)
            syncBusiness(businessId)
        }
    }

    private suspend fun updateEmail(email: String, businessId: String) {
        val local = localSource.getBusiness(businessId).firstOrNull()
        local?.let {
            if (email == local.email) return

            val apiRequest = tech.okcredit.identity.remote.request.UpdateBusinessRequest(
                business_user_id = local.id,
                business_user = GetBusinessResponseWrapper(
                    user = BusinessUser(
                        id = local.id,
                        mobile = local.mobile,
                        display_name = local.name,
                        create_time = local.createdAt,
                        email = email,
                    ),
                ),
                update_email = true,
            )
            remoteSource.updateBusiness(apiRequest, businessId)
            syncBusiness(businessId)
        }
    }

    private suspend fun updateCategory(
        categoryId: String?,
        categoryName: String?,
        businessId: String,
    ) {
        val local = localSource.getBusiness(businessId).firstOrNull()
        local?.let {
            if (categoryId == local.categoryId) return

            val apiRequest = tech.okcredit.identity.remote.request.UpdateBusinessRequest(
                business_user_id = local.id,
                business_user = GetBusinessResponseWrapper(
                    user = BusinessUser(
                        id = local.id,
                        mobile = local.mobile,
                        display_name = local.name,
                        create_time = local.createdAt,
                    ),
                    business_category = tech.okcredit.identity.remote.response.Category(
                        id = categoryId,
                        name = categoryName,
                    ),
                ),
                update_category = true,
            )
            remoteSource.updateBusiness(apiRequest, businessId)
            syncBusiness(businessId)
        }
    }

    private fun updateProfileImage(profileImage: String, businessId: String) {
    }

    private suspend fun updatePersonName(personName: String, businessId: String) {
        val local = localSource.getBusiness(businessId).firstOrNull()
        local?.let {
            if (personName == local.contactName) return

            val apiRequest = tech.okcredit.identity.remote.request.UpdateBusinessRequest(
                business_user_id = local.id,
                business_user = GetBusinessResponseWrapper(
                    user = BusinessUser(
                        id = local.id,
                        mobile = local.mobile,
                        display_name = local.name,
                        create_time = local.createdAt,
                    ),
                    contact_name = personName,
                ),
                update_contact_name = true,
            )
            remoteSource.updateBusiness(apiRequest, businessId)
            syncBusiness(businessId)
        }
    }

    private suspend fun updateBusinessNameOnboarding(businessName: String, businessId: String) {
        val remote = remoteSource.getBusiness(businessId)
        val apiRequest = tech.okcredit.identity.remote.request.UpdateBusinessRequest(
            business_user_id = remote.user.id,
            business_user = GetBusinessResponseWrapper(
                user = BusinessUser(
                    id = remote.user.id,
                    mobile = remote.user.mobile,
                    display_name = businessName,
                    create_time = remote.user.create_time,
                ),
            ),
            update_display_name = true,
        )
        remoteSource.updateBusiness(apiRequest, businessId)
        syncBusiness(businessId)
    }

    private suspend fun updateBusinessName(businessName: String, businessId: String) {
        val local = localSource.getBusiness(businessId).firstOrNull()
        local?.let {
            if (local.name == businessName) return

            val apiRequest = tech.okcredit.identity.remote.request.UpdateBusinessRequest(
                business_user_id = local.id,
                business_user = GetBusinessResponseWrapper(
                    user = BusinessUser(
                        id = local.id,
                        mobile = local.mobile,
                        display_name = businessName,
                        create_time = local.createdAt,
                    ),
                ),
                update_display_name = true,
            )
            remoteSource.updateBusiness(apiRequest, businessId)
            syncBusiness(businessId)
        }
    }

    private suspend fun syncBusiness(businessId: String) {
        val updated = remoteSource.getBusiness(businessId)
        localSource.saveBusiness(updated.toDbBusiness())
    }

    private suspend fun updateEditProfile(businessId: String, profileInfo: ProfileInfo) {
        val local = localSource.getBusiness(businessId).firstOrNull()
        local?.let {
            val apiRequest = tech.okcredit.identity.remote.request.UpdateBusinessRequest(
                business_user_id = local.id,
                business_user = GetBusinessResponseWrapper(
                    user = BusinessUser(
                        id = local.id,
                        mobile = local.mobile,
                        display_name = profileInfo.businessName,
                        email = profileInfo.email,
                        profile_image = profileInfo.profileImage,
                        create_time = local.createdAt,
                    ),
                    contact_name = profileInfo.name,
                ),
                update_email = local.email != profileInfo.email,
                update_display_name = local.name != profileInfo.businessName,
                update_profile_image = local.profileImage != profileInfo.profileImage,
                update_contact_name = local.contactName != profileInfo.name,
            )
            remoteSource.updateBusiness(apiRequest, businessId)
            syncBusiness(businessId)
        }
    }

    suspend fun clearLocalData() {
        localSource.clearAll()
    }

    fun getBusinessList(): Flow<List<Business>> {
        return localSource.getBusinessList()
    }

    suspend fun createBusiness(name: String, businessId: String): Business {
        val newBusiness = remoteSource.createBusiness(name, businessId).toDomainBusiness()
        localSource.saveBusiness(business = newBusiness.toDbBusiness())
        return newBusiness
    }

    suspend fun updateBusinessMobile(
        individualId: String,
        mobile: String,
        currentMobileOtpToken: String,
        newMobileOtpToken: String,
    ) {
        remoteSource.updateBusinessMobile(
            mobile = mobile,
            currentMobileOtpToken = currentMobileOtpToken,
            newMobileOtpToken = newMobileOtpToken,
            individualId = individualId,
        )
    }

    suspend fun updateIndividual(request: UpdateIndividualRequest) {
        remoteSource.updateIndividual(request)
    }

    suspend fun syncIndividual(flowId: String) {
        val remote = remoteSource.getIndividual(flowId)
        localSource.setIndividual(remote.toDbIndividual())
        putPreferences(remote.individualUser)
        remote.businessIds.forEach {
            syncBusiness(businessId = it)
        }
    }

    fun getIndividual(flowId: String, ignoreCache: Boolean = false): Flow<Individual> {
        return flow {
            if (ignoreCache) {
                val remote = remoteSource.getIndividual(flowId)
                localSource.setIndividual(remote.toDbIndividual())
                putPreferences(remote.individualUser)
                remote.businessIds.forEach {
                    syncBusiness(businessId = it)
                }
            }

            return@flow emitAll(localSource.getIndividual())
        }
    }

    private suspend fun putPreferences(individualUser: IndividualUser) {
        individualUser.appLockOptIn?.let {
            setIndividualPreference(
                PreferenceKey.APP_LOCK.key,
                it.toString(),
            )
        }
        individualUser.whatsappOptIn?.let {
            setIndividualPreference(
                PreferenceKey.WHATSAPP.key,
                it.toString(),
            )
        }
        individualUser.paymentPasswordEnabled?.let {
            setIndividualPreference(
                PreferenceKey.PAYMENT_PASSWORD.key,
                it.toString(),
            )
        }
        individualUser.fingerprintLockOptIn?.let {
            setIndividualPreference(
                PreferenceKey.FINGER_PRINT_LOCK.key,
                it.toString(),
            )
        }
        individualUser.fourDigitPinIn?.let {
            setIndividualPreference(
                PreferenceKey.FOUR_DIGIT_PIN.key,
                it.toString(),
            )
        }
    }

    suspend fun setIndividualPreference(key: String, value: String) {
        localSource.setIndividualPreference(key, value)
    }

    fun getIndividualPreference(preference: PreferenceKey): Flow<String> {
        return localSource.getIndividualPreference(preference.key, preference.defaultValue)
    }

    suspend fun getBusinessCount(): Int {
        return localSource.getBusinessCount()
    }

    suspend fun getActiveBusinessId(): String? {
        return localSource.getActiveBusinessId().firstOrNull()
    }
}

private fun GetIndividualResponse.toDbIndividual(): DbIndividual {
    return DbIndividual(
        id = individualUser.user.id,
        createTime = individualUser.user.createTime,
        mobile = individualUser.user.mobile,
        email = individualUser.user.email,
        registerTime = individualUser.user.registerTime,
        lang = individualUser.user.lang,
        displayName = individualUser.user.displayName,
        profileImage = individualUser.user.profileImage,
        addressText = individualUser.user.address?.text,
        longitude = individualUser.user.address?.longitude,
        latitude = individualUser.user.address?.latitude,
        about = individualUser.user.about,
        recoveryMobile = individualUser.alternateMobile,
        businessIds = businessIds.joinToString(";"),
    )
}

private fun Business.toDbBusiness(): DbBusiness {
    return DbBusiness(
        id = this.id,
        name = this.name,
        mobile = this.mobile,
        profileImage = this.profileImage,
        address = this.address,
        addressLatitude = this.addressLatitude,
        addressLongitude = this.addressLongitude,
        about = this.about,
        email = this.email,
        contactName = this.contactName,
        createdAt = this.createdAt,
        category = Json.encodeToString(category),
        business = Json.encodeToString(businessType),
        isFirst = this.isFirst,
    )
}

private fun GetBusinessResponseWrapper.toDomainBusiness(): Business {
    return Business(
        id = this.user.id,
        name = this.user.display_name ?: this.user.mobile,
        mobile = this.user.mobile,
        profileImage = this.user.profile_image,
        address = this.user.address?.text,
        addressLatitude = this.user.address?.latitude,
        addressLongitude = this.user.address?.longitude,
        about = this.user.about,
        email = this.user.email,
        contactName = this.contact_name,
        createdAt = this.user.create_time,
        category = this.business_category?.let {
            Category(
                id = it.id,
                name = it.name,
                imageUrl = it.image_url,
                type = it.type ?: 0,
            )
        },
        businessType = this.business_type?.let {
            BusinessType(
                id = it.id,
                name = it.name,
                title = it.title,
                sub_title = it.sub_title,
            )
        },
    )
}

private fun GetBusinessResponseWrapper.toDbBusiness(): DbBusiness {
    return DbBusiness(
        id = this.user.id,
        name = this.user.display_name ?: this.user.mobile,
        mobile = this.user.mobile,
        profileImage = this.user.profile_image,
        address = this.user.address?.text,
        addressLatitude = this.user.address?.latitude,
        addressLongitude = this.user.address?.longitude,
        about = this.user.about,
        email = this.user.email,
        contactName = this.contact_name,
        createdAt = this.user.create_time,
        category = if (this.business_category?.id.isNullOrEmpty()) {
            null
        } else {
            Json.encodeToString(this.business_category!!)
        },
        business = if (this.business_type?.id.isNullOrEmpty()) {
            null
        } else {
            Json.encodeToString(this.business_type)
        },
        isFirst = this.is_first ?: false,
    )
}
