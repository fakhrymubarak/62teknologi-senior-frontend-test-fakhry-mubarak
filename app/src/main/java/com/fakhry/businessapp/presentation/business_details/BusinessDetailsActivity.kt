package com.fakhry.businessapp.presentation.business_details

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.fakhry.businessapp.R
import com.fakhry.businessapp.core.enums.EXTRA_BUSINESS_ID
import com.fakhry.businessapp.core.enums.UiState
import com.fakhry.businessapp.core.enums.asString
import com.fakhry.businessapp.core.utils.components.collectLifecycleFlow
import com.fakhry.businessapp.core.utils.components.showToast
import com.fakhry.businessapp.core.utils.components.viewBinding
import com.fakhry.businessapp.core.utils.isShimmerStarted
import com.fakhry.businessapp.core.utils.isVisible
import com.fakhry.businessapp.databinding.ActivityBusinessDetailsBinding
import com.fakhry.businessapp.domain.business.model.BusinessDetails
import com.fakhry.businessapp.domain.business.model.ratingWithReviewCount
import com.fakhry.businessapp.domain.review.model.Review
import com.fakhry.businessapp.presentation.adapters.CategoryAdapter
import com.fakhry.businessapp.presentation.adapters.PhotoAdapter
import com.fakhry.businessapp.presentation.adapters.ReviewAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.roundToInt


@AndroidEntryPoint
class BusinessDetailsActivity : AppCompatActivity() {
    private val binding by viewBinding(ActivityBusinessDetailsBinding::inflate)
    private val viewModel by viewModels<BusinessDetailsViewModel>()

    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var photoAdapter: PhotoAdapter
    private lateinit var reviewAdapter: ReviewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initView()
        initListener()
        initObserver()
    }

    private fun initView() {
        categoryAdapter = CategoryAdapter()
        binding.rvCategories.adapter = categoryAdapter

        photoAdapter = PhotoAdapter()
        binding.vpPhotoSlider.adapter = photoAdapter
        binding.dotsPhoto.attachTo(binding.vpPhotoSlider)

        reviewAdapter = ReviewAdapter()
        binding.rvReviews.adapter = reviewAdapter

    }

    private fun initListener() {
        binding.btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.vpPhotoSlider.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                viewModel.setCarouselCurrentIndex(position)
                super.onPageSelected(position)
            }
        })
    }

    private fun initObserver() {
        val businessId = intent.getStringExtra(EXTRA_BUSINESS_ID) ?: return onBackPressedDispatcher.onBackPressed()
        viewModel.getBusinessDetails(businessId)
        viewModel.getReviews(businessId)

        collectLifecycleFlow(viewModel.carouselState) {
            binding.vpPhotoSlider.currentItem = it
        }

        collectLifecycleFlow(viewModel.businessState) { state ->
            when (state) {
                is UiState.Error -> showToast(state.uiText.asString(this))
                is UiState.Loading -> populateLoadingDetails(state.isLoading)
                is UiState.Success -> populateSuccessDetails(state.data)
            }
        }
        collectLifecycleFlow(viewModel.reviewsState) { state ->
            when (state) {
                is UiState.Error -> showToast(state.uiText.asString(this))
                is UiState.Loading -> populateLoadingReviews(state.isLoading)
                is UiState.Success -> populateSuccessReviews(state.data)
            }
        }
    }

    private fun populateLoadingDetails(isLoading: Boolean) {
        binding.shimmerSlider.isShimmerStarted(isLoading)
        binding.vpPhotoSlider.isVisible(!isLoading)
        binding.rvCategories.isVisible(!isLoading)
    }

    private fun populateSuccessDetails(data: BusinessDetails) {
        binding.apply {
            var spanCount = (data.categories.size / 3.0).roundToInt()
            spanCount = if(spanCount > 0) spanCount else 1
            rvCategories.layoutManager = StaggeredGridLayoutManager(spanCount, StaggeredGridLayoutManager.HORIZONTAL)
            categoryAdapter.setData(data.categories)
            photoAdapter.setData(data.photoUrls)

            tvBusinessName.text = data.business.name
            tvBusinessRating.text = data.business.ratingWithReviewCount()
            tvBusinessContact.text = data.business.displayPhone
            tvBusinessAddress.text = data.business.fullAddress

            tvBusinessIsOpen.apply {
                text = if (data.isOpenNow) getString(R.string.text_open_now) else getString(R.string.text_closed)
                setCompoundDrawablesWithIntrinsicBounds(
                    AppCompatResources.getDrawable(
                        context,
                        if (data.isOpenNow) R.drawable.ic_open_now_24
                        else R.drawable.ic_close_24
                    ),
                    null, null, null
                )
            }

            btnSeeOnMaps.isVisible(true)

            btnSeeOnMaps.setOnClickListener {
                val url = "https://maps.google.com/?q=${data.location.latitude},${data.location.longitude}"
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(url)
                startActivity(intent)
            }
        }
    }


    private fun populateLoadingReviews(isLoading: Boolean) {
        binding.shimmerList.isShimmerStarted(isLoading)
        binding.rvReviews.isVisible(!isLoading)
    }

    private fun populateSuccessReviews(data: List<Review>) {
        reviewAdapter.setData(data)
    }
}