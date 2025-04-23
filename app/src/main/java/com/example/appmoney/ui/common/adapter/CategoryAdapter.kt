package com.example.appmoney.ui.common.adapter

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.example.appmoney.data.model.Category
import com.example.appmoney.data.model.CategoryImage
import com.example.appmoney.data.model.CategoryType
import com.example.appmoney.databinding.ItemCategoryHorizontalBinding
import com.example.appmoney.databinding.ItemCategoryVerticalBinding


class CategoryDiffCallback : DiffUtil.ItemCallback<Category>() {
    override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean {
        return oldItem == newItem
    }

}

interface CategoryListener {
    fun onItemClick(category: Category)
}

class CategoryAdapter : ListAdapter<Category, CategoryAdapter.CategoryViewHolder>(
    CategoryDiffCallback()
) {

     override fun submitList(list: MutableList<Category>?) {
        list?.add(Category(image = CategoryImage.EDIT, desCat = "Chỉnh sửa", categoryType = CategoryType.BUTTON))
        super.submitList(list)
    }

    private var listener: CategoryListener? = null
    fun setListener(listener: CategoryListener) {
        this.listener = listener
    }

    abstract class CategoryViewHolder(open val binding: ViewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        abstract fun bindItem(item: Category, isSelected: Boolean)
    }

    inner class ViewHolder(override val binding: ItemCategoryVerticalBinding) :
        CategoryViewHolder(binding) {
        override fun bindItem(
            item: Category,
            isSelected: Boolean
        ) {
            binding.apply {
                txtCat.text = item.desCat
                imageCat.setImageResource(item.image.resource)
                imageCat.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(root.context, item.color.resource))
                if (isSelected) {
                    cardItem.strokeColor = Color.GRAY
                    cardItem.strokeWidth = 4
                } else {
                    cardItem.strokeColor = Color.TRANSPARENT
                }
                root.setOnClickListener {
                    listener?.onItemClick(item)
                }
            }
        }
    }

    inner class ButtonViewHolder(override val binding: ItemCategoryHorizontalBinding) :
        CategoryViewHolder(binding) {
        override fun bindItem(
            item: Category,
            isSelected: Boolean
        ) {
            binding.apply {
                txtCat.text = item.desCat
                imageCat.setImageResource(item.image.resource)
                cardButton.strokeColor = Color.TRANSPARENT
                root.setOnClickListener {
                    listener?.onItemClick(item)
                }
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CategoryViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            CategoryType.ITEM.viewType -> ViewHolder(
                ItemCategoryVerticalBinding.inflate(inflater, parent, false)
            )

            CategoryType.BUTTON.viewType -> ButtonViewHolder(
                ItemCategoryHorizontalBinding.inflate(inflater, parent, false)
            )

            else -> ViewHolder(
                ItemCategoryVerticalBinding.inflate(inflater, parent, false)
            )
        }
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val item = getItem(position)
        holder.bindItem(item, item.isSelected)
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).categoryType.viewType
    }

}