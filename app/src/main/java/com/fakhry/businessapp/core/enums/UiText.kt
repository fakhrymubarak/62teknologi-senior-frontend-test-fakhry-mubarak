package com.fakhry.businessapp.core.enums

import android.content.Context
import androidx.annotation.StringRes
import com.fakhry.businessapp.R

sealed class UiText {
    data class DynamicString(val value: String) : UiText()
    data class StringResource(@StringRes val id: Int) : UiText()
    data class StringResourceWithInt(@StringRes val id: Int, val params: Int) : UiText()

    companion object {
        val unknownError = StringResource(R.string.text_error_unknown)
        val networkError = StringResource(R.string.text_error_network_not_avail)
    }
}

fun UiText.asString(context: Context) =
    when (this) {
        is UiText.DynamicString -> this.value
        is UiText.StringResource -> context.getString(this.id)
        is UiText.StringResourceWithInt -> context.getString(this.id, params)
    }

