package com.fakhry.businessapp.presentation.dashboard

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
import com.fakhry.businessapp.core.enums.asString
import com.fakhry.businessapp.core.network.getMessageFromException
import com.fakhry.businessapp.core.utils.components.collectLifecycleFlow
import com.fakhry.businessapp.core.utils.components.showToast
import com.fakhry.businessapp.core.utils.components.viewBinding
import com.fakhry.businessapp.core.utils.isShimmerStarted
import com.fakhry.businessapp.core.utils.isVisible
import com.fakhry.businessapp.databinding.ActivityDashboardBinding
import com.fakhry.businessapp.domain.business.model.Business
import com.fakhry.businessapp.presentation.adapters.BusinessPagingAdapter
import com.fakhry.businessapp.presentation.adapters.ItemLoadStateAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class DashboardActivity : AppCompatActivity() {
    private val binding by viewBinding(ActivityDashboardBinding::inflate)
    private val viewModel by viewModels<DashboardViewModel>()

    private lateinit var adapter: BusinessPagingAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initView()
        initListener()
        initObserver()
    }

    private fun initView() {
        adapter = BusinessPagingAdapter()
        binding.rvBusiness.adapter = adapter.withLoadStateFooter(ItemLoadStateAdapter())
    }

    private fun initListener() {
        adapter.onDetailClick = { id ->
            Timber.i("click $id")
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
        collectLifecycleFlow(adapter.loadStateFlow) { loadStates ->
            val state = loadStates.refresh
            populateLoadingBusiness(state is LoadState.Loading)
            if (state is LoadState.Error) {
                val networkException = getMessageFromException(state.error as Exception)
                showToast(networkException.errorMessage.asString(this))
                adapter.retry()
            }
        }
    }

    private suspend fun populateSuccessBusiness(data: PagingData<Business>) {
        adapter.submitData(PagingData.empty())
        adapter.submitData(data)
    }

    private fun populateLoadingBusiness(isLoading: Boolean) {
        binding.shimmerList.isShimmerStarted(isLoading)
        binding.rvBusiness.isVisible(!isLoading)
    }
}