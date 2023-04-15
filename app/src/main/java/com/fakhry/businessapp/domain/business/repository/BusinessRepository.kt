package com.fakhry.businessapp.domain.business.repository

import com.fakhry.businessapp.core.enums.DataResource
import com.fakhry.businessapp.data.business.model.request.SearchBusinessQueryParam
import com.fakhry.businessapp.data.business.model.response.BusinessesItem

interface BusinessRepository {
    suspend fun getBusiness(queryParam: SearchBusinessQueryParam): DataResource<List<BusinessesItem>>
}