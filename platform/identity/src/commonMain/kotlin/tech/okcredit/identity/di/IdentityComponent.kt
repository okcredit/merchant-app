package tech.okcredit.identity.di

import de.jensklingenberg.ktorfit.Ktorfit
import me.tatarka.inject.annotations.IntoSet
import me.tatarka.inject.annotations.Provides
import okcredit.base.syncer.SignOutListener
import tech.okcredit.identity.IdentityDriverFactory
import tech.okcredit.identity.IdentitySignOutListener
import tech.okcredit.identity.contract.usecase.*
import tech.okcredit.identity.local.IdentityDatabase
import tech.okcredit.identity.local.IdentityDatabaseQueries
import tech.okcredit.identity.remote.IdentityApiClient
import tech.okcredit.identity.usecase.*

interface IdentityComponent {

    @Provides
    fun identityApiClient(ktorfit: Ktorfit): IdentityApiClient = ktorfit.create()

    @Provides
    fun identityDatabase(driverFactory: IdentityDriverFactory): IdentityDatabaseQueries {
        val database = IdentityDatabase(driverFactory.createDriver())
        return database.identityDatabaseQueries
    }

    @Provides
    fun getIndividual(getIndividual: GetIndividualImpl): GetIndividual {
        return getIndividual
    }

    @Provides
    fun getActiveBusinessId(getActiveBusinessId: GetActiveBusinessIdImpl): GetActiveBusinessId {
        return getActiveBusinessId
    }

    @Provides
    fun getActiveBusiness(getActiveBusiness: GetActiveBusinessImpl): GetActiveBusiness {
        return getActiveBusiness
    }

    @Provides
    fun setActiveBusinessId(setActiveBusinessId: SetActiveBusinessIdImpl): SetActiveBusinessId {
        return setActiveBusinessId
    }

    @Provides
    fun updateBusiness(updateBusiness: UpdateBusinessImpl): UpdateBusiness {
        return updateBusiness
    }

    @Provides
    fun syncIndividual(syncIndividual: SyncIndividualImpl): SyncIndividual {
        return syncIndividual
    }

    @Provides
    fun updateIndividualMobile(updateIndividualMobile: UpdateIndividualMobileImpl): UpdateIndividualMobile {
        return updateIndividualMobile
    }

    @Provides
    fun updateRecoveryNumber(updateRecoveryNumber: UpdateRecoveryNumberImpl): UpdateRecoveryNumber {
        return updateRecoveryNumber
    }

    @Provides
    fun setIndividualPreference(setIndividualPreference: SetIndividualPreferenceImpl): SetIndividualPreference {
        return setIndividualPreference
    }

    @Provides
    fun getIndividualPreference(getIndividualPreference: GetIndividualPreferenceImpl): GetIndividualPreference {
        return getIndividualPreference
    }

    @Provides
    @IntoSet
    fun identitySignOutListener(listener: IdentitySignOutListener): SignOutListener {
        return listener
    }
}
