package com.hotaku.common.mapper

interface Mapper<F, T> {
    fun map(from: F): T
}
