package tech.okcredit.identity.local

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOne
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import me.tatarka.inject.annotations.Inject
import okcredit.base.di.IoDispatcher
import tech.okcredit.identity.contract.model.Business as DomainBusiness
import tech.okcredit.identity.contract.model.BusinessType as DomainBusinessType
import tech.okcredit.identity.contract.model.Category as DomainCategory
import tech.okcredit.identity.contract.model.Individual as DomainIndividual

@Inject
class IdentityLocalSource constructor(
    private val businessDao: Lazy<IdentityDatabaseQueries>,
    private val preferences: Lazy<IdentityPreferences>,
    private val ioDispatcher: IoDispatcher,
) {

    companion object {
        const val DEFAULT_BUSINESS_ID = "default_business_id"
    }

    fun getBusiness(businessId: String): Flow<DomainBusiness> {
        return businessDao.value.getBusiness(businessId).asFlow().mapToOne(ioDispatcher)
            .map { it.toBusiness() }
            .flowOn(ioDispatcher)
    }

    fun getCategories(): Flow<List<DomainCategory>> {
        return businessDao.value.getCategories()
            .asFlow()
            .mapToList(ioDispatcher)
            .map { categories ->
                categories.map {
                    DomainCategory(
                        id = it.id,
                        name = it.name,
                        imageUrl = it.imageUrl,
                        isPopular = it.isPopular,
                        type = it.type.toInt(),
                    )
                }
            }
            .flowOn(ioDispatcher)
    }

    fun getBusinessTypes(): Flow<List<DomainBusinessType>> {
        return businessDao.value.getBusinessTypes()
            .asFlow()
            .mapToList(ioDispatcher)
            .map { businessTypes ->
                businessTypes.map {
                    DomainBusinessType(
                        id = it.id,
                        name = it.name,
                        image_url = it.image_url,
                        sub_title = it.sub_title,
                        title = it.title,
                    )
                }
            }
            .flowOn(ioDispatcher)
    }

    suspend fun saveBusiness(business: Business) {
        withContext(ioDispatcher) {
            businessDao.value.insertBusiness(
                id = business.id,
                business = business.business,
                about = business.about,
                address = business.address,
                isFirst = business.isFirst,
                profileImage = business.profileImage,
                addressLatitude = business.addressLatitude,
                addressLongitude = business.addressLongitude,
                category = business.category,
                contactName = business.contactName,
                createdAt = business.createdAt,
                email = business.email,
                mobile = business.mobile,
                name = business.name,
            )
        }
    }

    suspend fun saveCategories(categories: List<BusinessCategory>) {
        withContext(ioDispatcher) {
            categories.forEach {
                businessDao.value.saveCategories(
                    id = it.id,
                    name = it.name,
                    imageUrl = it.imageUrl,
                    isPopular = it.isPopular,
                    type = it.type,
                )
            }
        }
    }

    suspend fun saveBusinessTypes(businessTypes: List<BusinessType>) {
        withContext(ioDispatcher) {
            businessTypes.forEach {
                businessDao.value.saveBusinessTypes(
                    id = it.id,
                    name = it.name,
                    image_url = it.image_url,
                    sub_title = it.sub_title,
                    title = it.title,
                )
            }
        }
    }

    fun getBusinessList(): Flow<List<DomainBusiness>> {
        return businessDao.value.getBusinessList()
            .asFlow()
            .mapToList(ioDispatcher)
            .map { list ->
                list.map {
                    it.toBusiness()
                }
            }
            .flowOn(ioDispatcher)
    }

    suspend fun clearAll() {
        withContext(ioDispatcher) {
            businessDao.value.deleteBusiness()
            businessDao.value.deleteBusinessCategory()
            businessDao.value.deleteBusinessType()
            businessDao.value.deleteIndividual()
            preferences.value.clear()
        }
    }

    suspend fun setActiveBusinessId(businessId: String) {
        preferences.value.set(DEFAULT_BUSINESS_ID, businessId, okcredit.base.local.Scope.Individual)
    }

    fun getActiveBusinessId(): Flow<String> {
        return preferences.value.getString(DEFAULT_BUSINESS_ID, okcredit.base.local.Scope.Individual, "")
            .flowOn(ioDispatcher)
    }

    fun getActiveBusiness(): Flow<DomainBusiness> {
        return preferences.value.getString(DEFAULT_BUSINESS_ID, okcredit.base.local.Scope.Individual, "")
            .flatMapLatest {
                if (it.isEmpty()) {
                    getBusinessList().map { list ->
                        list.first()
                    }
                } else {
                    getBusiness(it)
                }
            }
            .flowOn(ioDispatcher)
    }

    suspend fun setIndividual(individual: Individual) {
        withContext(ioDispatcher) {
            businessDao.value.setIndividual(
                id = individual.id,
                createTime = individual.createTime,
                mobile = individual.mobile,
                email = individual.email,
                registerTime = individual.registerTime,
                lang = individual.lang,
                displayName = individual.displayName,
                profileImage = individual.profileImage,
                addressText = individual.addressText,
                longitude = individual.longitude,
                latitude = individual.latitude,
                about = individual.about,
                recoveryMobile = individual.recoveryMobile,
                businessIds = individual.businessIds,
            )
        }
    }

    fun getIndividual(): Flow<DomainIndividual> {
        return businessDao.value.getIndividual()
            .asFlow()
            .mapToOne(ioDispatcher)
            .map { it.toIndividual() }
    }

    suspend fun setIndividualPreference(key: String, value: String) {
        preferences.value.set(key, value, okcredit.base.local.Scope.Individual)
    }

    fun getIndividualPreference(key: String, defaultValue: String): Flow<String> {
        return preferences.value.getString(key, okcredit.base.local.Scope.Individual, defaultValue)
    }

    suspend fun getBusinessCount(): Int {
        return withContext(ioDispatcher) { businessDao.value.getBusinessCount().executeAsOne().toInt() }
    }
}

private fun Individual.toIndividual(): DomainIndividual {
    return DomainIndividual(
        id = id,
        createTime = createTime,
        mobile = mobile,
        email = email,
        registerTime = registerTime,
        lang = lang,
        displayName = displayName,
        profileImage = profileImage,
        addressText = addressText,
        longitude = longitude,
        latitude = latitude,
        about = about,
        recoveryMobile = recoveryMobile,
        businessIds = businessIds.split(";"),
    )
}

fun Business.toBusiness(): DomainBusiness {
    return DomainBusiness(
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
        category = parseToCategory(this.category),
        businessType = parseToBusinessType(this.business),
        isFirst = isFirst,
    )
}

fun parseToBusinessType(business: String?): DomainBusinessType? {
    if (business.isNullOrEmpty()) return null
    val result = Json.decodeFromString<tech.okcredit.identity.remote.response.BusinessType>(business)
    return DomainBusinessType(
        id = result.id,
        name = result.name,
        image_url = result.image_url,
        sub_title = result.sub_title,
        title = result.title,
    )
}

fun parseToCategory(category: String?): DomainCategory? {
    if (category.isNullOrEmpty()) return null
    val result = Json.decodeFromString<tech.okcredit.identity.remote.response.Category>(category)
    return DomainCategory(
        id = result.id,
        name = result.name,
        imageUrl = result.image_url,
        isPopular = result.is_popular ?: false,
        type = result.type ?: 0,
    )
}
