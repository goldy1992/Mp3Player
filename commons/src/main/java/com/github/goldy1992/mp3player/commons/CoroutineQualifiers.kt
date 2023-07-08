package com.github.goldy1992.mp3player.commons

import javax.inject.Qualifier


@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class DefaultDispatcher

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class IoDispatcher

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class MainDispatcher

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class MainImmediateDispatcher

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class ActivityCoroutineScope

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class ServiceCoroutineScope

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class SingletonCoroutineScope

