package com.fakhry.businessapp.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fakhry.businessapp.core.utils.loadWithShimmer
import com.fakhry.businessapp.databinding.ItemReviewBinding
import com.fakhry.businessapp.domain.review.model.Review

class ReviewAdapter : RecyclerView.Adapter<ReviewAdapter.ViewHolder>() {
    private val listData = ArrayList<Review>()

    fun setData(newListData: List<Review>) {
        val previousContentSize = listData.size
        listData.clear()
        listData.addAll(newListData)
        notifyItemRangeRemoved(0, previousContentSize)
        notifyItemRangeInserted(0, newListData.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        ItemReviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = listData[position]
        holder.bind(data)
    }

    override fun getItemCount() = listData.size

    inner class ViewHolder(private val binding: ItemReviewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: Review) {
            binding.apply {
                ivUserAvatar.loadWithShimmer(data.userAvatarUrl)
                tvUserName.text = data.userName
                ratingBar.rating = data.rating.toFloat()
                tvReviewDate.text = data.timeCreatedAgo
                tvReviewText.text = data.text
            }
        }
    }
}