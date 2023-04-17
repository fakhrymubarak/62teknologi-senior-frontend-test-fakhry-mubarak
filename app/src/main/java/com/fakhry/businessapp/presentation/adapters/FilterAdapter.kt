package com.fakhry.businessapp.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fakhry.businessapp.databinding.ItemFilterBinding
import com.fakhry.businessapp.presentation.dashboard.model.FilterBusiness
import com.fakhry.businessapp.presentation.dashboard.model.getDefaultFilters

class FilterAdapter : RecyclerView.Adapter<FilterAdapter.ViewHolder>() {
    private val listData = getDefaultFilters().toMutableList()

    var onClick: ((FilterBusiness) -> Unit)? = null

    private fun activateData(newData: FilterBusiness) {
        // update state
        val oldData = listData.first { it.id == newData.id }
        newData.isActive = !oldData.isActive

        // replace data
        val prevDataIndex = listData.indexOf(oldData)
        this.listData.removeAt(prevDataIndex)
        this.listData.add(prevDataIndex, newData)

        // notify data update
        notifyItemChanged(newData.id)
    }

    fun unCheckedNearbyFilter() {
        val data = listData.first()
        activateData(data)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        ItemFilterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = listData[position]
        holder.bind(data)
    }

    override fun getItemCount() = listData.size

    inner class ViewHolder(private val binding: ItemFilterBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: FilterBusiness) {
            initView(data)
            initListener(data)
        }

        private fun initView(data: FilterBusiness) {
            binding.tvFilterName.text = data.name
            binding.root.isChecked = data.isActive
        }

        private fun initListener(data: FilterBusiness) {
            binding.root.setOnClickListener {
                activateData(data)
                onClick?.invoke(data)
            }
        }
    }
}
