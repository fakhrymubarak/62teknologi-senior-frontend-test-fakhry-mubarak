package com.fakhry.businessapp.domain.business.usecases

import androidx.paging.PagingData
import androidx.paging.map
import com.fakhry.businessapp.data.business.model.request.BusinessQueryParam
import com.fakhry.businessapp.data.business.model.response.toDomain
import com.fakhry.businessapp.domain.business.model.Business
import com.fakhry.businessapp.domain.business.repository.BusinessRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetBusinessListUseCase @Inject constructor(
    private val repository: BusinessRepository
) {
    suspend operator fun invoke(query: String, filters: List<String>): Flow<PagingData<Business>> {
        val queryParam = BusinessQueryParam()
        queryParam.terms = query
        return repository.getBusiness(queryParam, filters).flow.map { pagingData ->
            pagingData.map { data -> data.toDomain() }
        }
    }
}