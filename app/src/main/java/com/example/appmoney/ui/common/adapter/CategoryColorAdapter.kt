package com.example.appmoney.ui.common.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.appmoney.data.model.CategoryColorWrapper
import com.example.appmoney.databinding.ItemCategoryColorBinding


class CategoryColorDiffCallback : DiffUtil.ItemCallback<CategoryColorWrapper>() {
    override fun areItemsTheSame(oldItem: CategoryColorWrapper, newItem: CategoryColorWrapper): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: CategoryColorWrapper, newItem: CategoryColorWrapper): Boolean {
        return oldItem == newItem
    }

}

interface CategoryColorListener {
    fun onItemClick(categoryColor: CategoryColorWrapper)
}

class CategoryColorAdapter :
    ListAdapter<CategoryColorWrapper, CategoryColorAdapter.CategoryColorViewHolder>(
        CategoryColorDiffCallback()
    ) {

        private var listener: CategoryColorListener? = null
        fun setListener(listener: CategoryColorListener) {
            this.listener = listener
        }

    inner class CategoryColorViewHolder(val binding: ItemCategoryColorBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindItem(
            item: CategoryColorWrapper,
            isSelected: Boolean
        ) {
            binding.apply {
                viewColor.setBackgroundColor(ContextCompat.getColor(root.context, item.categoryColor.resource))
                if (isSelected) {
                    cardItem.strokeColor = Color.GRAY
                    cardItem.strokeWidth = 8
                } else {
                    cardItem.strokeColor = Color.TRANSPARENT
                }
                root.setOnClickListener {
                    listener?.onItemClick(item)
                }
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CategoryColorViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return CategoryColorViewHolder(
            ItemCategoryColorBinding.inflate(inflater, parent, false)
        )
    }

    override fun onBindViewHolder(holder: CategoryColorViewHolder, position: Int) {
        val item = getItem(position)
        holder.bindItem(item, item.isSelected)
    }
}