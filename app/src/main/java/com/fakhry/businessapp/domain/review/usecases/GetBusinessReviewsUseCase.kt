package com.fakhry.businessapp.domain.review.usecases

import com.fakhry.businessapp.core.enums.DataResource
import com.fakhry.businessapp.data.business.model.response.review.toDomain
import com.fakhry.businessapp.domain.business.repository.BusinessRepository
import com.fakhry.businessapp.domain.review.model.Review
import javax.inject.Inject

class GetBusinessReviewsUseCase @Inject constructor(
    private val repository: BusinessRepository
) {
    suspend operator fun invoke(id: String): DataResource<List<Review>> {
        return when (val dataResult = repository.getBusinessReviews(id)) {
            is DataResource.Error -> dataResult
            is DataResource.Success -> DataResource.Success(dataResult.data.toDomain())
        }
    }
}