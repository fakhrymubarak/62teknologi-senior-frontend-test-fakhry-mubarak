package com.fakhry.businessapp.domain.business.usecases

import com.fakhry.businessapp.core.enums.DataResource
import com.fakhry.businessapp.data.business.model.response.toDomainDetails
import com.fakhry.businessapp.domain.business.model.BusinessDetails
import com.fakhry.businessapp.domain.business.repository.BusinessRepository
import javax.inject.Inject

class GetBusinessDetailsUseCase @Inject constructor(
    private val repository: BusinessRepository
) {
    suspend operator fun invoke(id: String): DataResource<BusinessDetails> {
        return when (val dataResult = repository.getBusinessDetails(id)) {
            is DataResource.Error -> dataResult
            is DataResource.Success -> DataResource.Success(dataResult.data.toDomainDetails())
        }
    }
}