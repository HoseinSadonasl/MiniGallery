package com.hotaku.media.utils

import java.util.Locale

object TimeUtils {
    fun Int.millisAsFormattedDuration(): String {
        val durationInSeconds = this.div(1000)
        val (hours, minutes, seconds) = durationInSeconds.getTimeUnits()
        val locale = Locale.getDefault()
        return if (durationInSeconds >= 3600) {
            String.format(locale, "%02d:%02d:%02d", hours, minutes, seconds)
        } else {
            String.format(locale, "%02d:%02d", minutes, seconds)
        }
    }

    private fun Int.getTimeUnits(): Triple<Int, Int, Int> {
        val hours = this / 3600
        val minutes = (this % 3600) / 60
        val seconds = this % 60
        return Triple(hours, minutes, seconds)
    }
}
