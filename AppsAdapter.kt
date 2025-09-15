package com.firas.simplelauncher

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.firas.simplelauncher.databinding.ItemAppBinding

class AppsAdapter(
    private val onClick: (AppInfo) -> Unit
) : ListAdapter<AppInfo, AppsAdapter.AppViewHolder>(DIFF) {

    companion object {
        val DIFF = object : DiffUtil.ItemCallback<AppInfo>() {
            override fun areItemsTheSame(oldItem: AppInfo, newItem: AppInfo) = oldItem.pkg == newItem.pkg
            override fun areContentsTheSame(oldItem: AppInfo, newItem: AppInfo) = oldItem == newItem
        }
    }

    inner class AppViewHolder(private val binding: ItemAppBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: AppInfo) {
            binding.appLabel.text = item.label
            binding.appIcon.setImageDrawable(item.icon)
            binding.root.setOnClickListener { onClick(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppViewHolder {
        val binding = ItemAppBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AppViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AppViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}
