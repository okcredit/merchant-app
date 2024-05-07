package okcredit.base.di

import me.tatarka.inject.annotations.Scope

@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.CLASS)
annotation class Singleton

@Scope
annotation class ActivityScope
