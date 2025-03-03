package com.hotaku.media.utils

internal enum class MediaType {
    UNKNOWN,
    IMAGE,
    VIDEO,
}

internal fun String.asMediaType(): MediaType {
    return when {
        this.startsWith("video") -> MediaType.VIDEO
        this.startsWith("image") -> MediaType.IMAGE
        else -> MediaType.UNKNOWN
    }
}
