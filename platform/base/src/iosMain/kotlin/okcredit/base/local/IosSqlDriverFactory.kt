package okcredit.base.local

import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.db.SqlSchema
import app.cash.sqldelight.driver.native.NativeSqliteDriver

class IosSqlDriverFactory(
    private val schema: SqlSchema<QueryResult.Value<Unit>>,
    private val name: String,
) : SqlDriverFactory {

    override fun createDriver(): SqlDriver {
        return NativeSqliteDriver(schema, name)
    }
}
