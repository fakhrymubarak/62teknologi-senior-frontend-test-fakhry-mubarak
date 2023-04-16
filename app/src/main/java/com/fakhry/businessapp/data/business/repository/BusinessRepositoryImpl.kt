package com.fakhry.businessapp.data.business.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.fakhry.businessapp.core.enums.API_SEARCH_LIMIT
import com.fakhry.businessapp.core.network.NetworkState
import com.fakhry.businessapp.data.business.model.request.BusinessQueryParam
import com.fakhry.businessapp.data.business.model.response.BusinessesData
import com.fakhry.businessapp.data.business.remote.BusinessApiService
import com.fakhry.businessapp.data.business.remote.BusinessPagingSource
import com.fakhry.businessapp.domain.business.repository.BusinessRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BusinessRepositoryImpl @Inject constructor(
    private val networkState: NetworkState,
    private val apiService: BusinessApiService,
) : BusinessRepository {

    override suspend fun getBusiness(
        queryParam: BusinessQueryParam,
        filters: List<String>,
    ): Pager<Int, BusinessesData> {
        return Pager(
            config = PagingConfig(
                enablePlaceholders = false,
                pageSize = API_SEARCH_LIMIT
            ),
            pagingSourceFactory = {
                BusinessPagingSource(
                    networkState = networkState,
                    apiService = apiService,
                    queryParam = queryParam,
                    filters = filters,
                )
            }
        )
    }
}