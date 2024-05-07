package okcredit.base.syncer

import android.content.Context
import com.russhwolf.settings.SharedPreferencesSettings
import com.russhwolf.settings.coroutines.toFlowSettings
import me.tatarka.inject.annotations.Inject
import okcredit.base.appDispatchers
import okcredit.base.local.BasePreferences

class WorkManagerPrefs @Inject constructor(
    private val context: Context,
) : BasePreferences(
    lazy {
        SharedPreferencesSettings.Factory(context).create("worker_settings")
            .toFlowSettings(appDispatchers.io)
    },
)
