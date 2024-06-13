package app.okcredit.staff_link.di

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import app.okcredit.staff_link.data.local.StaffLinkDatabase

class IosStaffLinkDriverFactory: StaffLinkDriverFactory {
    override fun createDriver(): SqlDriver {
        return NativeSqliteDriver(StaffLinkDatabase.Schema, "staff_link.db")
    }
}