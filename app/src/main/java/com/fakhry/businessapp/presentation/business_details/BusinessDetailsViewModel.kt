package com.fakhry.businessapp.presentation.business_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fakhry.businessapp.core.enums.DataResource
import com.fakhry.businessapp.core.enums.UiState
import com.fakhry.businessapp.domain.business.model.BusinessDetails
import com.fakhry.businessapp.domain.business.usecases.GetBusinessDetailsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BusinessDetailsViewModel @Inject constructor(
    private val _getBusinessDetails: GetBusinessDetailsUseCase
) : ViewModel() {

    private val _businessState = MutableStateFlow<UiState<BusinessDetails>?>(null)
    val businessState = _businessState.asSharedFlow()

    private var _totalImages = 0
    private var _currImageIndex = 0
    private var _runCarouselJob: Job? = null
    val carouselState = MutableSharedFlow<Int>()

    override fun onCleared() {
        _runCarouselJob?.cancel()
        super.onCleared()
    }

    fun getBusinessDetails(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _businessState.emit(UiState.Loading(true))
            when (val result = _getBusinessDetails(id)) {
                is DataResource.Error -> _businessState.emit(UiState.Error(result.uiText))
                is DataResource.Success -> {
                    _businessState.emit(UiState.Success(result.data))
                    _totalImages = result.data.photoUrls.size
                    runCarousel()
                }
            }
            _businessState.emit(UiState.Loading(false))
        }
    }

    private fun runCarousel() {
        _runCarouselJob = viewModelScope.launch(Dispatchers.IO) {
            while (true) {
                delay(3000)
                val nextIndex = if (_currImageIndex >= _totalImages - 1) 0 else (_currImageIndex + 1)
                carouselState.emit(nextIndex)
            }
        }
    }

    fun setCarouselCurrentIndex(index: Int) {
        _currImageIndex = index
    }
}