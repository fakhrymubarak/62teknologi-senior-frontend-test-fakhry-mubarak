package com.fakhry.businessapp.data.business.remote

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.fakhry.businessapp.core.enums.API_SEARCH_LIMIT
import com.fakhry.businessapp.core.enums.NetworkException
import com.fakhry.businessapp.core.network.NetworkState
import com.fakhry.businessapp.data.business.model.request.BusinessQueryParam
import com.fakhry.businessapp.data.business.model.request.asMap
import com.fakhry.businessapp.data.business.model.response.BusinessesData
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException

class BusinessPagingSource(
    private val networkState: NetworkState,
    private val apiService: BusinessApiService,
    private val queryParam: BusinessQueryParam,
    private val filters: List<String>,
) : PagingSource<Int, BusinessesData>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, BusinessesData> {
        return try {
            if (networkState.isNetworkNotAvailable) throw NetworkException()

            val nextOffset = params.key ?: 0
            val result =
                if (queryParam.latitude == null || queryParam.longitude == null) {
                    apiService.getBusiness(
                        queryParam.asMap(),
                        offset = nextOffset,
                        filters = filters
                    )
                } else {
                    apiService.getBusinessNearby(
                        queryParam.terms,
                        latitude = queryParam.latitude!!,
                        longitude = queryParam.longitude!!,
                        offset = nextOffset,
                        filters = filters
                    )
                }

            val data = result.businesses

            if (data.isEmpty() || result.total == 0) {
                return LoadResult.Page(listOf(), null, null)
            }

            LoadResult.Page(
                data = data,
                prevKey = null, // Only paging forward.
                nextKey = if (nextOffset > result.total) null else nextOffset + API_SEARCH_LIMIT
            )
        } catch (e: IOException) {
            Timber.e(e)
            LoadResult.Error(e)
        } catch (e: HttpException) {
            Timber.e(e)
            LoadResult.Error(e)
        } catch (e: NetworkException){
            Timber.e(e)
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, BusinessesData>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}