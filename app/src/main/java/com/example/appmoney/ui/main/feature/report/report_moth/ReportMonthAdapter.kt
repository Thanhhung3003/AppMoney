package com.example.appmoney.ui.main.feature.report.report_moth

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.appmoney.R
import com.example.appmoney.databinding.ItemReportBinding
import com.example.appmoney.ui.main.feature.report.CategoryDetail

class CatDetailDiffCalBack: DiffUtil.ItemCallback<CategoryDetail>() {
    override fun areItemsTheSame(oldItem: CategoryDetail, newItem: CategoryDetail): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: CategoryDetail, newItem: CategoryDetail): Boolean {
        return oldItem == newItem
    }
}

class ReportMonthAdapter : ListAdapter<CategoryDetail,ReportMonthAdapter.ReportMonthViewHolder>(CatDetailDiffCalBack()){

    inner class ReportMonthViewHolder( val binding: ItemReportBinding): RecyclerView.ViewHolder(binding.root){
        fun binItem( item : CategoryDetail){
            binding.apply {
                val img = item.image?.resource ?: R.drawable.resource_null
                imgRp.setImageResource(img)
                val color = item.color?.resource ?: R.color.black
                imgRp.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(root.context, color))
                tvDesCat.text = item.desCat ?: "Trống"
                tvMoneyRp.text = "${item.amount.toString()}đ"
                tvPercent.text = "${item.percent}%"
            }
        }
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ReportMonthAdapter.ReportMonthViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ReportMonthViewHolder(ItemReportBinding.inflate(inflater,parent,false))
    }

    override fun onBindViewHolder(holder: ReportMonthAdapter.ReportMonthViewHolder, position: Int) {
        val item = getItem(position)
        holder.binItem(item)
    }
}