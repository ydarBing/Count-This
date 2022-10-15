package com.gurpgork.countthis.extensions

import javax.inject.Qualifier

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
@MustBeDocumented
annotation class MediumDate

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
@MustBeDocumented
annotation class MediumDateTime

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
@MustBeDocumented
annotation class ShortDate

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
@MustBeDocumented
annotation class ShortTime

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
@MustBeDocumented
annotation class ApplicationId