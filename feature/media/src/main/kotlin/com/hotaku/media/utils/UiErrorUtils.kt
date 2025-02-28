package com.hotaku.media.utils

import com.hotaku.domain.utils.Error
import com.hotaku.domain.utils.ErrorResult
import com.hotaku.feature.media.R
import com.hotaku.media_domain.util.SyncFailureReason
import com.hotaku.ui.UiText

internal fun Error.asUiError(): UiText =
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

internal fun SyncFailureReason.asUiError(): UiText =
    when (this) {
        SyncFailureReason.LOW_STORAGE -> UiText.StringResource(resId = R.string.all_storage_low_error)
        SyncFailureReason.UNKNOWN -> UiText.StringResource(R.string.all_unknown_error)
    }
