package com.fakhry.businessapp.core.enums

sealed class DataResource<out T> {
    data class Success<out T>(val data: T) : DataResource<T>()
    data class Error(val uiText: UiText) : DataResource<Nothing>()
}