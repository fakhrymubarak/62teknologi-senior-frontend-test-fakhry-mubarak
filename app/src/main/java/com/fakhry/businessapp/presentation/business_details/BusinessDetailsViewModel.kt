package com.fakhry.businessapp.presentation.business_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fakhry.businessapp.core.enums.DataResource
import com.fakhry.businessapp.core.enums.UiState
import com.fakhry.businessapp.domain.business.model.BusinessDetails
import com.fakhry.businessapp.domain.business.usecases.GetBusinessDetailsUseCase
import com.fakhry.businessapp.domain.review.model.Review
import com.fakhry.businessapp.domain.review.usecases.GetBusinessReviewsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BusinessDetailsViewModel @Inject constructor(
    private val _getBusinessDetails: GetBusinessDetailsUseCase,
    private val _getBusinessReviews: GetBusinessReviewsUseCase,
) : ViewModel() {

    private val _businessState = MutableSharedFlow<UiState<BusinessDetails>>()
    val businessState = _businessState.asSharedFlow()

    private val _reviewsState = MutableSharedFlow<UiState<List<Review>>>()
    val reviewsState = _reviewsState.asSharedFlow()

    private var _totalImages = 0
    private var _currImageIndex = 0
    private var _runCarouselJob: Job? = null
    val carouselState = MutableSharedFlow<Int>()

    override fun onCleared() {
        _runCarouselJob?.cancel()
        super.onCleared()
    }

    private fun runCarousel() {
        _runCarouselJob = viewModelScope.launch(Dispatchers.IO) {
            delay(3000)
            val nextIndex = if (_currImageIndex >= _totalImages - 1) 0 else (_currImageIndex + 1)
            carouselState.emit(nextIndex)
        }
    }

    fun setCarouselCurrentIndex(index: Int) {
        _runCarouselJob?.cancel()
        _currImageIndex = index
        runCarousel()
    }

    fun getBusinessDetails(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _businessState.emit(UiState.Loading(true))
            when (val result = _getBusinessDetails(id)) {
                is DataResource.Error -> _businessState.emit(UiState.Error(result.uiText))
                is DataResource.Success -> {
                    _businessState.emit(UiState.Loading(false))
                    _businessState.emit(UiState.Success(result.data))
                    _totalImages = result.data.photoUrls.size
                    runCarousel()
                }
            }
        }
    }

    fun getReviews(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _reviewsState.emit(UiState.Loading(true))
            when (val result = _getBusinessReviews(id)) {
                is DataResource.Error -> _reviewsState.emit(UiState.Error(result.uiText))
                is DataResource.Success -> {
                    _reviewsState.emit(UiState.Loading(false))
                    _reviewsState.emit(UiState.Success(result.data))
                }
            }
        }
    }
}