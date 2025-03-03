package com.hotaku.ui

enum class MediaType {
    UNKNOWN,
    IMAGE,
    VIDEO,
}

fun String.asMediaType(): MediaType {
    return when {
        this.startsWith("video") -> MediaType.VIDEO
        this.startsWith("image") -> MediaType.IMAGE
        else -> MediaType.UNKNOWN
    }
}
