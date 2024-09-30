package app.okcredit.merchant.android

import android.content.Context
import app.okcredit.ledger.core.di.AndroidLedgerComponent
import app.okcredit.ledger.ui.LedgerScreenRegistryProvider
import app.okcredit.ledger.ui.di.LedgerUiComponent
import app.okcredit.merchant.SharedScreenRegistryProvider
import app.okcredit.merchant.di.SharedComponent
import app.okcredit.onboarding.OnboardingComponent
import app.okcredit.onboarding.OnboardingScreenRegistryProvider
import app.okcredit.web.WebComponent
import app.okcredit.web.WebScreenRegistryProvider
import com.mixpanel.android.mpmetrics.MixpanelAPI
import me.tatarka.inject.annotations.Component
import me.tatarka.inject.annotations.Provides
import okcredit.base.di.AndroidBaseComponent
import okcredit.base.di.AppScreenModelFactory
import okcredit.base.di.AppVersion
import okcredit.base.di.AppVersionCode
import okcredit.base.di.BaseUrl
import okcredit.base.di.Debug
import okcredit.base.di.Flavor
import okcredit.base.di.Singleton
import okcredit.base.syncer.AppInitializer
import okcredit.base.syncer.AppWorkerFactory
import tech.okcredit.ab.AndroidAbComponent
import tech.okcredit.analytics.AndroidAnalyticsComponent
import tech.okcredit.auth.AndroidAuthComponent
import tech.okcredit.collection.AndroidCollectionComponent
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
) : AndroidBaseComponent,
    AndroidAnalyticsComponent,
    AndroidAbComponent,
    AndroidAuthComponent,
    AndroidCustomizationComponent,
    AndroidCollectionComponent,
    AndroidDeviceComponent,
    AndroidIdentityComponent,
    AndroidOkDocComponent,
    AndroidLedgerComponent,
    SharedComponent,
    OnboardingComponent,
    LedgerUiComponent,
    WebComponent {

    abstract val appWorkerFactory: () -> AppWorkerFactory

    abstract val appInitializers: Set<AppInitializer>

    abstract val appScreenModelFactory: AppScreenModelFactory

    abstract val sharedScreenRegistryProvider: SharedScreenRegistryProvider

    abstract val onboardingScreenRegistryProvider: OnboardingScreenRegistryProvider

    abstract val ledgerScreenRegistryProvider: LedgerScreenRegistryProvider

    abstract val webScreenRegistryProvider: WebScreenRegistryProvider

    @Singleton
    @Provides
    fun mixpanel(): MixpanelAPI {
        return MixpanelAPI.getInstance(application, BuildConfig.MIXPANEL_TOKEN, false)
    }
}

val Context.applicationComponent get() = (applicationContext as MerchantApplication).component
