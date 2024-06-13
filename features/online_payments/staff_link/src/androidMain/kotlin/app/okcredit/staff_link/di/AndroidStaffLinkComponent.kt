package app.okcredit.staff_link.di

import me.tatarka.inject.annotations.Provides

interface AndroidStaffLinkComponent : StaffLinkComponent {

    @Provides
    fun AndroidStaffLinkDriverFactory.binds(): StaffLinkDriverFactory = this
}