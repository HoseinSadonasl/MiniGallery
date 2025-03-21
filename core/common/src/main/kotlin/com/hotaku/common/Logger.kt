package com.hotaku.common

import android.util.Log
import com.hotaku.core.common.BuildConfig
import kotlin.reflect.KClass
import kotlin.reflect.KFunction

object Logger {
    private val isDebug = BuildConfig.DEBUG

    fun debugWarningLog(
        kClass: KClass<*>,
        message: String,
    ) {
        if (isDebug) {
            Log.i(kClass.java.name, message)
        }
    }

    fun debugWarningLog(
        kFun: KFunction<*>,
        message: String,
    ) {
        if (isDebug) {
            Log.i(kFun.name, message)
        }
    }

    fun debugWarningLog(
        tag: String,
        message: String,
    ) {
        if (isDebug) {
            Log.i(tag, message)
        }
    }

    fun Throwable.debugWarningLog(kClass: KClass<*>) {
        if (isDebug) {
            Log.i(kClass.java.name, localizedMessage ?: message ?: "Unknown error")
        }
    }

    fun Throwable.debugWarningLog(kFun: KFunction<*>) {
        if (isDebug) {
            Log.i(kFun.name, localizedMessage ?: message ?: "Unknown error")
        }
    }

    fun Throwable.debugErrorLog(kClass: KClass<*>) {
        if (isDebug) {
            Log.e(kClass.java.name, localizedMessage ?: message ?: "Unknown error")
        }
    }

    fun Throwable.debugErrorLog(kFun: KFunction<*>) {
        if (isDebug) {
            Log.e(kFun.name, localizedMessage ?: message ?: "Unknown error")
        }
    }

    fun Exception.debugLogStackTrace(kClass: KClass<*>) {
        if (isDebug) {
            Log.e(kClass.java.name, localizedMessage ?: message ?: "Unknown error")
        }
    }

    fun Exception.debugLogStackTrace(kFun: KFunction<*>) {
        if (isDebug) {
            Log.e(kFun.name, localizedMessage ?: message ?: "Unknown error")
        }
    }
}
