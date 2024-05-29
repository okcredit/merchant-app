package app.okcredit.merchant

import app.okcredit.onboarding.OnboardingScreenRegistryProvider
import app.okcredit.onboarding.di.OnboardingModule
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
import tech.okcredit.ab.IosAbComponent
import tech.okcredit.analytics.di.AnalyticsComponent
import tech.okcredit.auth.IosAuthComponent
import tech.okcredit.device.IosDeviceComponent
import tech.okcredit.identity.IosIdentityComponent
import tech.okcredit.okdoc.IosOkDocComponent

@Component
@Singleton
abstract class ApplicationComponent(
    @get:Provides val baseUrl: BaseUrl,
    @get:Provides val appVersion: AppVersion,
    @get:Provides val versionCode: AppVersionCode,
    @get:Provides val debug: Debug,
    @get:Provides val flavor: Flavor,
) : BaseComponent,
    AnalyticsComponent,
    IosAbComponent,
    IosAuthComponent,
    IosDeviceComponent,
    IosIdentityComponent,
    IosOkDocComponent,
    IosSharedModule,
    OnboardingModule {

    abstract val onboardingScreenRegistryProvider: OnboardingScreenRegistryProvider

    abstract val sharedScreenRegistryProvider: SharedScreenRegistryProvider

    abstract val appScreenModelFactory: AppScreenModelFactory
}
