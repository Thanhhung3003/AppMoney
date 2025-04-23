package com.example.appmoney.ui.main.feature.categorytype.viewPagger

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.appmoney.data.model.Category
import com.example.appmoney.databinding.ItemTransactionBinding


class TransDiffCallback : DiffUtil.ItemCallback<Category>() {
    override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean {
        return oldItem == newItem

    }

    override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean {
        return oldItem == newItem
    }
}

interface CatTypeOnClickListener {
    fun onClick(item: Category)
}

class CategoryTypeAdapter : ListAdapter<Category, CategoryTypeAdapter.CategoryTypeViewHolder>(
    TransDiffCallback()
) {
    private var listener: CatTypeOnClickListener? = null
    fun setListener(listener: CatTypeOnClickListener) {
        this.listener = listener
    }

    inner class CategoryTypeViewHolder(val binding: ItemTransactionBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindItem(item: Category) {
            binding.apply {
                desCatType.text = item.desCat
                imgCatType.setImageResource(item.image.resource)
                imgCatType.imageTintList =
                    ContextCompat.getColorStateList(root.context, item.color.resource)
                root.setOnClickListener {
                    listener?.onClick(item)
                }
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CategoryTypeViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return CategoryTypeViewHolder(ItemTransactionBinding.inflate(inflater, parent, false))
    }


    override fun onBindViewHolder(holder: CategoryTypeViewHolder, position: Int) {
        val item = getItem(position)
        holder.bindItem(item)

    }

}