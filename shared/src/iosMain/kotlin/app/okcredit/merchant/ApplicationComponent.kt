package app.okcredit.merchant

import me.tatarka.inject.annotations.Component
import me.tatarka.inject.annotations.KmpComponentCreate
import me.tatarka.inject.annotations.Provides
import okcredit.base.di.AppScreenModelFactory
import okcredit.base.di.AppVersion
import okcredit.base.di.AppVersionCode
import okcredit.base.di.BaseUrl
import okcredit.base.di.Debug
import okcredit.base.di.Flavor
import okcredit.base.syncer.AppInitializer
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.MergeComponent
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@Component
@MergeComponent(AppScope::class)
@SingleIn(AppScope::class)
abstract class ApplicationComponent(
    @get:Provides val baseUrl: BaseUrl,
    @get:Provides val appVersion: AppVersion,
    @get:Provides val versionCode: AppVersionCode,
    @get:Provides val debug: Debug,
    @get:Provides val flavor: Flavor,
) {

    companion object;

    abstract val appInitializers: Set<AppInitializer>

    abstract val appScreenModelFactory: AppScreenModelFactory
}

@KmpComponentCreate
expect fun ApplicationComponent.Companion.createKmp(
    baseUrl: BaseUrl,
    appVersion: AppVersion,
    versionCode: AppVersionCode,
    debug: Debug,
    flavor: Flavor,
): ApplicationComponent
