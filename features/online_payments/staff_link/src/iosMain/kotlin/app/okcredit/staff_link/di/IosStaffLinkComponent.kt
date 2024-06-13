package app.okcredit.staff_link.di

import me.tatarka.inject.annotations.Provides

interface IosStaffLinkComponent: StaffLinkComponent {

    @Provides
    fun IosStaffLinkDriverFactory.binds(): StaffLinkDriverFactory = this
}