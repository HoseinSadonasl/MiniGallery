package com.hotaku.media.utils

import com.hotaku.domain.utils.Error
import com.hotaku.domain.utils.ErrorResult
import com.hotaku.feature.media.R
import com.hotaku.ui.UiText

fun Error.asUiError(): UiText =
    when (val error = this as ErrorResult) {
        is ErrorResult.ApiError -> UiText.DynamicString("${error.message}(${error.code})")
        is ErrorResult.LocalError -> {
            when (error) {
                ErrorResult.LocalError.UNKNOWN -> UiText.StringResource(R.string.all_unknown_error)
                ErrorResult.LocalError.DISK_FULL -> UiText.StringResource(R.string.all_disk_full)
                ErrorResult.LocalError.IO -> UiText.StringResource(R.string.all_io_error)
                ErrorResult.LocalError.READ_DATA_ERROR -> UiText.StringResource(R.string.all_io_error)
                ErrorResult.LocalError.SYNC_DATA_ERROR -> UiText.StringResource(R.string.all_sync_error)
            }
        }
    }
