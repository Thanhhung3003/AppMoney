package com.example.appmoney.ui.common.adapter

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.appmoney.data.model.CategoryColor
import com.example.appmoney.data.model.CategoryImageWrapper
import com.example.appmoney.databinding.ItemCategoryImageBinding


class CategoryImageDiffCallback : DiffUtil.ItemCallback<CategoryImageWrapper>() {
    override fun areItemsTheSame(oldItem: CategoryImageWrapper, newItem: CategoryImageWrapper): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: CategoryImageWrapper, newItem: CategoryImageWrapper): Boolean {
        return oldItem == newItem
    }

}

interface CategoryImageListener {
    fun onItemClick(categoryImage: CategoryImageWrapper)
}

class CategoryImageAdapter : ListAdapter<CategoryImageWrapper, CategoryImageAdapter.CategoryImageViewHolder>(
    CategoryImageDiffCallback()
) {

    private var listener: CategoryImageListener? = null
        fun setListener(listener: CategoryImageListener) {
        this.listener = listener
    }

    private var tintImageColor = CategoryColor.BLACK
    @SuppressLint("NotifyDataSetChanged")
    fun setTintColor(color: CategoryColor) {
        this.tintImageColor = color
        try {
            this.currentList.firstOrNull {it.isSelected}?.let {
                val position = this.currentList.indexOf(it)
                this.notifyItemChanged(position)
            }
        }catch (e: Exception) {
            Log.e(this.javaClass.name, "err: $e")
        }
    }

    inner class CategoryImageViewHolder(val binding: ItemCategoryImageBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindItem(
            item: CategoryImageWrapper,
            isSelected: Boolean
        ) {
            binding.apply {
                imageCat.setImageResource(item.categoryImage.resource)

                if (isSelected) {
                    imageCat.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(root.context, tintImageColor.resource))
                    cardItem.strokeColor = Color.GRAY
                    cardItem.strokeWidth = 4
                } else {
                    cardItem.strokeColor = Color.TRANSPARENT
                    imageCat.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(root.context, CategoryColor.BLACK.resource))
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
    ): CategoryImageViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return CategoryImageViewHolder(
            ItemCategoryImageBinding.inflate(inflater, parent, false)
        )
    }

    override fun onBindViewHolder(holder: CategoryImageViewHolder, position: Int) {
        val item = getItem(position)
        holder.bindItem(item, item.isSelected)
    }
}