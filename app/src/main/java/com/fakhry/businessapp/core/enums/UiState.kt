package com.fakhry.businessapp.core.enums

sealed class UiState<out T> {
    data class Loading(val isLoading: Boolean) : UiState<Nothing>()
    data class Success<out T>(val data: T) : UiState<T>()
    data class Error(val uiText: UiText) : UiState<Nothing>()
}