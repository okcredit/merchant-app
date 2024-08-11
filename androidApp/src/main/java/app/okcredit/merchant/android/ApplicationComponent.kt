package app.okcredit.merchant.android

import android.content.Context
import app.okcredit.ledger.core.di.AndroidLedgerComponent
import app.okcredit.ledger.core.di.LedgerComponent
import app.okcredit.merchant.AndroidSharedComponent
import app.okcredit.merchant.SharedScreenRegistryProvider
import app.okcredit.onboarding.OnboardingScreenRegistryProvider
import app.okcredit.onboarding.di.OnboardingComponent
import com.mixpanel.android.mpmetrics.MixpanelAPI
import me.tatarka.inject.annotations.Component
import me.tatarka.inject.annotations.Provides
import okcredit.base.di.AppScreenModelFactory
import okcredit.base.di.AppVersion
import okcredit.base.di.AppVersionCode
import okcredit.base.di.BaseComponent
import okcredit.base.di.BaseUrl
import okcredit.base.di.Debug
import okcredit.base.di.Flavor
import okcredit.base.di.Singleton
import okcredit.base.syncer.AppInitializer
import okcredit.base.syncer.AppWorkerFactory
import tech.okcredit.ab.AndroidAbComponent
import tech.okcredit.analytics.AndroidAnalyticsComponent
import tech.okcredit.auth.AndroidAuthComponent
import tech.okcredit.customization.di.AndroidCustomizationComponent
import tech.okcredit.device.AndroidDeviceComponent
import tech.okcredit.identity.AndroidIdentityComponent
import tech.okcredit.okdoc.AndroidOkDocComponent

@Component
@Singleton
abstract class ApplicationComponent(
    @get:Provides val application: Context,
    @get:Provides val baseUrl: BaseUrl,
    @get:Provides val appVersion: AppVersion,
    @get:Provides val versionCode: AppVersionCode,
    @get:Provides val flavor: Flavor,
    @get:Provides val debug: Debug,
) : BaseComponent,
    AndroidAnalyticsComponent,
    AndroidAbComponent,
    AndroidAuthComponent,
    AndroidDeviceComponent,
    AndroidIdentityComponent,
    AndroidOkDocComponent,
    AndroidSharedComponent,
    AndroidLedgerComponent,
    OnboardingComponent,
    LedgerComponent,
    AndroidCustomizationComponent {

    abstract val appWorkerFactory: () -> AppWorkerFactory

    abstract val appInitializers: Set<AppInitializer>

    abstract val appScreenModelFactory: AppScreenModelFactory

    abstract val sharedScreenRegistryProvider: SharedScreenRegistryProvider

    abstract val onboardingScreenRegistryProvider: OnboardingScreenRegistryProvider

    @Singleton
    @Provides
    fun mixpanel(): MixpanelAPI {
        return MixpanelAPI.getInstance(application, BuildConfig.MIXPANEL_TOKEN, false)
    }
}

val Context.applicationComponent get() = (applicationContext as MerchantApplication).component
