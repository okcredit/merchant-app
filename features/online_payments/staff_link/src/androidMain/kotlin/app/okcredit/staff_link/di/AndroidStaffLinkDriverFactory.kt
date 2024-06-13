package app.okcredit.staff_link.di

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import app.okcredit.staff_link.data.local.StaffLinkDatabase
import me.tatarka.inject.annotations.Inject

@Inject
class AndroidStaffLinkDriverFactory(
    private val context: Context
) : StaffLinkDriverFactory {

    override fun createDriver(): SqlDriver {
        return AndroidSqliteDriver(StaffLinkDatabase.Schema, context, "okcredit_staff_link.db")
    }
}