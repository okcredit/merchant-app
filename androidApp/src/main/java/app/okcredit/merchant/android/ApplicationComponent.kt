package app.okcredit.merchant.android

import android.content.Context
import com.mixpanel.android.mpmetrics.MixpanelAPI
import me.tatarka.inject.annotations.Component
import me.tatarka.inject.annotations.Provides
import okcredit.base.di.AppScreenModelFactory
import okcredit.base.di.AppVersion
import okcredit.base.di.AppVersionCode
import okcredit.base.di.BaseUrl
import okcredit.base.di.Debug
import okcredit.base.di.Flavor
import okcredit.base.syncer.AppInitializer
import okcredit.base.syncer.AppWorkerFactory
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.MergeComponent
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@Component
@MergeComponent(AppScope::class)
@SingleIn(AppScope::class)
abstract class ApplicationComponent(
    @get:Provides val application: Context,
    @get:Provides val baseUrl: BaseUrl,
    @get:Provides val appVersion: AppVersion,
    @get:Provides val versionCode: AppVersionCode,
    @get:Provides val flavor: Flavor,
    @get:Provides val debug: Debug,
) : ApplicationComponentMerged {

    abstract val appWorkerFactory: AppWorkerFactory

    abstract val appInitializers: Set<AppInitializer>

    abstract val appScreenModelFactory: () -> AppScreenModelFactory

    @SingleIn(AppScope::class)
    @Provides
    fun mixpanel(): MixpanelAPI {
        return MixpanelAPI.getInstance(application, BuildConfig.MIXPANEL_TOKEN, false)
    }
}

val Context.applicationComponent get() = (applicationContext as MerchantApplication).component
