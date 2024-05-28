package tech.okcredit.ab

import me.tatarka.inject.annotations.Inject
import okcredit.base.syncer.AppInitializer
import okcredit.base.syncer.toJsonObject

@Inject
class AbAppInitializer(
    private val profileSyncer: ProfileSyncer,
) : AppInitializer {
    override fun init() {
        profileSyncer.schedule(mapOf<String, String>().toJsonObject())
    }
}
