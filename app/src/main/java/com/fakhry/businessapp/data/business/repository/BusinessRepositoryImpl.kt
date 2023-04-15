package com.fakhry.businessapp.data.business.repository

import com.fakhry.businessapp.core.enums.DataResource
import com.fakhry.businessapp.core.enums.UiText
import com.fakhry.businessapp.core.network.NetworkState
import com.fakhry.businessapp.core.network.getMessageFromException
import com.fakhry.businessapp.data.business.model.request.BusinessQueryParam
import com.fakhry.businessapp.data.business.model.request.asMap
import com.fakhry.businessapp.data.business.model.response.BusinessesData
import com.fakhry.businessapp.data.business.remote.BusinessApiService
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
        offset: Int,
    ): DataResource<List<BusinessesData>> {
        if (!networkState.isNetworkAvailable()) return DataResource.Error(UiText.networkError)

        return try {
            val result = apiService.getBusiness(queryParam.asMap(), offset = offset)
            DataResource.Success(result.businesses)
        } catch (e: Exception) {
            val networkException = getMessageFromException(e)
            DataResource.Error(networkException.errorMessages)
        }
    }
}