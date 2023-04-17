package com.fakhry.businessapp.domain.business.repository

import androidx.paging.Pager
import com.fakhry.businessapp.core.enums.DataResource
import com.fakhry.businessapp.data.business.model.request.BusinessQueryParam
import com.fakhry.businessapp.data.business.model.response.BusinessesData
import com.fakhry.businessapp.data.business.model.response.review.ReviewData

interface BusinessRepository {
    suspend fun getBusiness(
        queryParam: BusinessQueryParam = BusinessQueryParam(),
        filters: List<String>,
    ): Pager<Int, BusinessesData>

    suspend fun getBusinessDetails(id: String): DataResource<BusinessesData>
    suspend fun getBusinessReviews(id: String): DataResource<List<ReviewData>>
}