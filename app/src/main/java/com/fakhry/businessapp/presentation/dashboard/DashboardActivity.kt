package com.fakhry.businessapp.presentation.dashboard

import android.Manifest
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.LoadState
import androidx.paging.PagingData
import com.fakhry.businessapp.R
import com.fakhry.businessapp.core.enums.EXTRA_BUSINESS_ID
import com.fakhry.businessapp.core.enums.asString
import com.fakhry.businessapp.core.network.getMessageFromException
import com.fakhry.businessapp.core.utils.components.collectLifecycleFlow
import com.fakhry.businessapp.core.utils.components.showToast
import com.fakhry.businessapp.core.utils.components.viewBinding
import com.fakhry.businessapp.core.utils.isShimmerStarted
import com.fakhry.businessapp.core.utils.isVisible
import com.fakhry.businessapp.core.utils.location.AssistantLocationListener
import com.fakhry.businessapp.core.utils.location.AssistantLocationSingleRequest
import com.fakhry.businessapp.core.utils.location.PermissionUtil
import com.fakhry.businessapp.databinding.ActivityDashboardBinding
import com.fakhry.businessapp.domain.business.model.Business
import com.fakhry.businessapp.presentation.adapters.BusinessPagingAdapter
import com.fakhry.businessapp.presentation.adapters.FilterAdapter
import com.fakhry.businessapp.presentation.adapters.ItemLoadStateAdapter
import com.fakhry.businessapp.presentation.business_details.BusinessDetailsActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DashboardActivity : AppCompatActivity(), PermissionUtil.PermissionCallbacks,
    AssistantLocationListener {
    private val binding by viewBinding(ActivityDashboardBinding::inflate)
    private val viewModel by viewModels<DashboardViewModel>()

    private lateinit var businessAdapter: BusinessPagingAdapter
    private lateinit var filteradapter: FilterAdapter
    private lateinit var assistantLocationSingleRequest: AssistantLocationSingleRequest

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initView()
        initListener()
        initObserver()
    }

    override fun onDestroy() {
        super.onDestroy()
        assistantLocationSingleRequest.onDetach()
    }

    private fun initView() {
        businessAdapter = BusinessPagingAdapter()
        filteradapter = FilterAdapter()
        binding.rvBusiness.adapter = businessAdapter.withLoadStateFooter(ItemLoadStateAdapter())
        binding.rvFilter.adapter = filteradapter
    }

    private fun initListener() {
        businessAdapter.onDetailClick = { id ->
            val intent = Intent(this, BusinessDetailsActivity::class.java)
            intent.putExtra(EXTRA_BUSINESS_ID, id)
            startActivity(intent)
        }

        filteradapter.onClick = {
            if (it.id == 0 && it.isActive) getUserLocation()
            viewModel.apply { if (it.isActive) addFilterBusiness(it) else removeFilterBusiness(it) }
        }

        binding.etSearchCatalog.doOnTextChanged { text, _, _, _ ->
            viewModel.queryQrDebounced(text.toString())
        }

        binding.etSearchCatalog.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                viewModel.queryQrDebounced(v.text.toString(), isDebounce = false)
                return@OnEditorActionListener true
            }
            false
        })

    }

    private fun initObserver() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.listBusiness.collectLatest { data ->
                    if (data == null) {
                        viewModel.getListBusiness()
                        return@collectLatest
                    }
                    populateSuccessBusiness(data)
                }
            }
        }

        collectLifecycleFlow(businessAdapter.loadStateFlow) { loadStates ->
            val state = loadStates.refresh
            populateLoadingBusiness(state is LoadState.Loading)
            if (state is LoadState.Error) {
                val networkException = getMessageFromException(state.error as Exception)
                showToast(networkException.errorMessage.asString(this))
                businessAdapter.retry()
            }
        }
    }

    private suspend fun populateSuccessBusiness(data: PagingData<Business>) {
        businessAdapter.submitData(PagingData.empty())
        businessAdapter.submitData(data)
    }

    private fun populateLoadingBusiness(isLoading: Boolean) {
        binding.shimmerList.isShimmerStarted(isLoading)
        binding.rvBusiness.isVisible(!isLoading)
    }

    private fun getUserLocation() {
        assistantLocationSingleRequest = AssistantLocationSingleRequest(this, this)
        if (PermissionUtil.hasPermissions(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            assistantLocationSingleRequest.requestLocation()
        } else {
            PermissionUtil.requestPermissions(
                this, getString(R.string.text_permission_request_title),
                REQUEST_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION
            )
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
        assistantLocationSingleRequest.requestLocation()
    }

    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
        filteradapter.unCheckedNearbyFilter()
        PermissionUtil.checkDeniedPermissionsNeverAskAgain(
            this, getString(R.string.text_permission_message),
            R.string.text_permission_grant, R.string.text_permission_denny, perms
        )
    }

    override fun onSuccessGetLocation(location: Location) {
        viewModel.setNearbyLocation(location.latitude, location.longitude)
    }

    override fun onFailedGetLocation(errorMessage: String) {
        filteradapter.unCheckedNearbyFilter()
        showToast(errorMessage)
    }

    companion object {
        const val REQUEST_LOCATION = 101
    }

}