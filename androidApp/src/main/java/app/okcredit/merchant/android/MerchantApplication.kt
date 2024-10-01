package app.okcredit.merchant.android

import android.app.Application
import android.os.Build
import android.os.StrictMode
import android.webkit.WebView
import androidx.work.Configuration
import app.okcredit.ledger.ui.LedgerScreenRegistryProvider
import app.okcredit.merchant.SharedScreenRegistryProvider
import app.okcredit.onboarding.OnboardingScreenRegistryProvider
import app.okcredit.web.WebScreenRegistryProvider
import cafe.adriel.voyager.core.registry.ScreenRegistry

class MerchantApplication : Application(), Configuration.Provider {

    val component: ApplicationComponent by lazy(LazyThreadSafetyMode.NONE) {
        ApplicationComponent::class.create(
            application = this,
            baseUrl = BuildConfig.COMMON_BASE_URL,
            appVersion = BuildConfig.VERSION_NAME,
            versionCode = BuildConfig.VERSION_CODE,
            debug = BuildConfig.DEBUG,
            flavor = BuildConfig.FLAVOR,
        )
    }

    override fun onCreate() {
        if (BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(
                StrictMode.ThreadPolicy.Builder()
                    .detectAll() // or .detectAll() for all detectable problems
                    .penaltyLog()
                    .build(),
            )
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                StrictMode.setVmPolicy(
                    StrictMode.VmPolicy.Builder()
                        .detectUnsafeIntentLaunch()
                        .penaltyLog()
                        .penaltyDeath()
                        .build(),
                )
            }
            WebView.setWebContentsDebuggingEnabled(BuildConfig.DEBUG)
        }
        super.onCreate()
        ScreenRegistry {
            SharedScreenRegistryProvider.screenRegistry().invoke(this)
            OnboardingScreenRegistryProvider.screenRegistry().invoke(this)
            LedgerScreenRegistryProvider.screenRegistry().invoke(this)
            WebScreenRegistryProvider.screenRegistry().invoke(this)
        }

        component.appInitializers.forEach { it.init() }
    }

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(component.appWorkerFactory)
            .build()
}
