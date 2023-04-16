package com.fakhry.businessapp.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.fakhry.businessapp.core.utils.loadWithShimmer
import com.fakhry.businessapp.databinding.ItemBusinessBinding
import com.fakhry.businessapp.domain.business.model.Business
import com.fakhry.businessapp.domain.business.model.ratingWithReviewCount

class BusinessPagingAdapter : PagingDataAdapter<Business, BusinessPagingAdapter.ViewHolder>(
        BusinessDiffCallback()
    ) {

    var onDetailClick: ((String) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            ItemBusinessBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null) holder.bind(data)
    }

    inner class ViewHolder(
        private val binding: ItemBusinessBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(data: Business) {
            initView(data)
            initListener(data)
        }

        private fun initView(data: Business) {
            binding.apply {
                ivBusiness.loadWithShimmer(data.imageUrl)
                tvBusinessName.text = data.name
                tvBusinessRating.text = data.ratingWithReviewCount()
                tvBusinessContact.text = data.phone
                tvBusinessAddress.text = data.fullAddress
            }
        }

        private fun initListener(data: Business) {
            binding.apply {
                root.setOnClickListener {
                    onDetailClick?.invoke(data.id)
                }
            }
        }
    }
}

class BusinessDiffCallback : DiffUtil.ItemCallback<Business>() {
    override fun areItemsTheSame(
        oldItem: Business,
        newItem: Business,
    ): Boolean = oldItem.id == newItem.id

    override fun areContentsTheSame(
        oldItem: Business,
        newItem: Business,
    ): Boolean = oldItem == newItem
}
