package com.fakhry.businessapp.data.business.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.fakhry.businessapp.core.enums.API_SEARCH_LIMIT
import com.fakhry.businessapp.core.enums.DataResource
import com.fakhry.businessapp.core.enums.UiText
import com.fakhry.businessapp.core.network.NetworkState
import com.fakhry.businessapp.core.network.getMessageFromException
import com.fakhry.businessapp.data.business.model.request.BusinessQueryParam
import com.fakhry.businessapp.data.business.model.request.asMap
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
    ): Pager<Int, BusinessesData> {
        return Pager(
            config = PagingConfig(
                enablePlaceholders = false,
                pageSize = API_SEARCH_LIMIT
            ),
            pagingSourceFactory = {
                BusinessPagingSource(
                    apiService = apiService,
                    queryParam = queryParam,
                )
            }
        )
    }
}