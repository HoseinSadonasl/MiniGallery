package com.hotaku.common.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class Dispatcher(val miniGalleryDispatchers: MiniGalleryDispatchers)

enum class MiniGalleryDispatchers {
    Default,
    IO,
}
