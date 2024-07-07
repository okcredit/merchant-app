package okcredit.base.local

import android.content.Context
import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.db.SqlSchema
import app.cash.sqldelight.driver.android.AndroidSqliteDriver

class AndroidSqlDriverFactory(
    private val context: Context,
    private val schema: SqlSchema<QueryResult.Value<Unit>>,
    private val name: String,
) : SqlDriverFactory {

    override fun createDriver(): SqlDriver {
        return AndroidSqliteDriver(
            context = context,
            schema = schema,
            name = name,
        )
    }
}
