package com.fakhry.businessapp.presentation.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.fakhry.businessapp.domain.business.model.Business
import com.fakhry.businessapp.domain.business.usecases.GetBusinessListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val getBusinessList: GetBusinessListUseCase
) : ViewModel() {
    private var _queryJob: Job? = null
    private val _queryBusiness = MutableStateFlow("")

    private val _listBusiness = MutableStateFlow<PagingData<Business>?>(null)
    val listBusiness = _listBusiness.asStateFlow()

    fun queryQrDebounced(query: String, isDebounce: Boolean = true) {
        _queryJob?.cancel()
        _queryJob = viewModelScope.launch {
            if (isDebounce) delay(500)
            _queryBusiness.value = query
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun getListBusiness() {
        viewModelScope.launch(Dispatchers.IO) {
            _queryBusiness.flatMapLatest { getBusinessList(it) }.collectLatest { pagingData ->
                _listBusiness.emit(pagingData)
            }
        }
    }
}