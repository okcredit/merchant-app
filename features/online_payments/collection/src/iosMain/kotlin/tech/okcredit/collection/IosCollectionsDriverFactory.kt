package tech.okcredit.collection

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import me.tatarka.inject.annotations.Inject
import tech.okcredit.collection.local.CollectionDatabase
import tech.okcredit.collection.local.CollectionsDriverFactory

@Inject
class IosCollectionsDriverFactory : CollectionsDriverFactory {
    override fun createDriver(): SqlDriver {
        return NativeSqliteDriver(CollectionDatabase.Schema, "okcredit_collections.db")
    }
}
