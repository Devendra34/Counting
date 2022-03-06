package com.devtech.counting.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.devtech.counting.data.Item
import com.devtech.counting.databinding.LargeItemBinding

class TreeItemsAdapter(
    private val onItemClicked: (position: Int) -> Unit,
) : ListAdapter<Item, TreeItemsAdapter.TreeItemViewHolder>(DIFF_UTIL) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = TreeItemViewHolder(parent)

    override fun onBindViewHolder(holder: TreeItemViewHolder, position: Int) {
        if (position == RecyclerView.NO_POSITION) return
        val data = getItem(position) ?: return
        holder.bind(data)
    }

    inner class TreeItemViewHolder(
        parent: ViewGroup,
        private val binding: LargeItemBinding = LargeItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                if (binding.image.visibility != View.VISIBLE) return@setOnClickListener
                onItemClicked(adapterPosition)
            }
        }

        fun bind(item: Item) {
            binding.image.visibility = if (item.isSelected) View.INVISIBLE else View.VISIBLE
        }
    }

    companion object {
        private val DIFF_UTIL = object : DiffUtil.ItemCallback<Item>() {
            override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
                return oldItem == newItem
            }
        }
    }
}