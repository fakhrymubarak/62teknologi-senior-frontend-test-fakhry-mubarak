package com.fakhry.businessapp.presentation.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.fakhry.businessapp.domain.business.model.Business
import com.fakhry.businessapp.domain.business.usecases.GetBusinessListUseCase
import com.fakhry.businessapp.presentation.dashboard.model.FilterBusiness
import com.fakhry.businessapp.presentation.dashboard.model.toListString
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val getBusinessList: GetBusinessListUseCase
) : ViewModel() {
    private var _queryJob: Job? = null
    private val _queryBusiness = MutableStateFlow("")

    private val _filtersBusiness = MutableStateFlow<List<FilterBusiness>>(listOf())

    private val _listBusiness = MutableStateFlow<PagingData<Business>?>(null)
    val listBusiness = _listBusiness.asStateFlow()

    fun queryQrDebounced(query: String, isDebounce: Boolean = true) {
        _queryJob?.cancel()
        _queryJob = viewModelScope.launch {
            if (isDebounce) delay(500)
            _queryBusiness.value = query
        }
    }

    fun addFilterBusiness(value: FilterBusiness) {
        val activeFilters = _filtersBusiness.value.toMutableList()
        activeFilters.remove(value)
        _filtersBusiness.value = activeFilters
    }

    fun removeFilterBusiness(value: FilterBusiness) {
        val activeFilters = _filtersBusiness.value.toMutableList()
        activeFilters.add(value)
        _filtersBusiness.value = activeFilters
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun getListBusiness() {
        viewModelScope.launch(Dispatchers.IO) {
            _queryBusiness.flatMapLatest {
                getBusinessList(query = it, filters = _filtersBusiness.value.toListString())
            }.collectLatest { pagingData ->
                _listBusiness.emit(pagingData)
            }
        }

        viewModelScope.launch(Dispatchers.IO) {
            _filtersBusiness.flatMapLatest {
                getBusinessList(query = _queryBusiness.value, filters = it.toListString())
            }.collectLatest { pagingData ->
                _listBusiness.emit(pagingData)
            }
        }
    }
}