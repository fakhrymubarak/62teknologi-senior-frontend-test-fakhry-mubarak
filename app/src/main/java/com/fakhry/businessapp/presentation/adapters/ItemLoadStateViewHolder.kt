package com.fakhry.businessapp.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.fakhry.businessapp.core.utils.isShimmerStarted
import com.fakhry.businessapp.databinding.ItemLoadStateBinding


class ItemLoadStateViewHolder(
    private val binding: ItemLoadStateBinding,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(loadState: LoadState) {
        binding.shimmerList.isShimmerStarted(loadState is LoadState.Loading)
    }

    companion object {
        fun create(parent: ViewGroup): ItemLoadStateViewHolder {
            val binding =
                ItemLoadStateBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return ItemLoadStateViewHolder(binding)
        }
    }
}

class ItemLoadStateAdapter : LoadStateAdapter<ItemLoadStateViewHolder>() {
    override fun onBindViewHolder(holder: ItemLoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): ItemLoadStateViewHolder {
        return ItemLoadStateViewHolder.create(parent)
    }
}