package tech.okcredit.identity.local

import me.tatarka.inject.annotations.Inject
import okcredit.base.di.IoDispatcher
import okcredit.base.local.BasePreferences

@Inject
class IdentityPreferences(
    settingsFactory: IdentitySettingsFactory,
    ioDispatcher: IoDispatcher,
) : BasePreferences(lazy { settingsFactory.create() }, ioDispatcher)
