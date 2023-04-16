package com.fakhry.businessapp.domain.business.repository

import androidx.paging.Pager
import androidx.paging.PagingData
import com.fakhry.businessapp.data.business.model.request.BusinessQueryParam
import com.fakhry.businessapp.data.business.model.response.BusinessesData

interface BusinessRepository {
    suspend fun getBusiness(
        queryParam: BusinessQueryParam = BusinessQueryParam()
    ): Pager<Int, BusinessesData>
}