package app.okcredit.staff_link.data.local

import app.cash.sqldelight.db.SqlDriver
import me.tatarka.inject.annotations.Inject


typealias StaffLinkSqlDriver = SqlDriver

@Inject
class StaffLinkLocalSource(
    sqlDriver: StaffLinkSqlDriver
) {

    private val database = StaffLinkDatabase(sqlDriver)

    private val collectionListEntityQueries = database.collectionListEntityQueries

    suspend fun insertCollectionEntity() {
    }
}