package com.example.appmoney.ui.main.feature.report.report_year

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.appmoney.R
import com.example.appmoney.databinding.ItemReportBinding
import com.example.appmoney.databinding.ItemReportYearBinding

class DataMonthDiffCalBack: DiffUtil.ItemCallback<MonthData>() {
    override fun areItemsTheSame(oldItem: MonthData, newItem: MonthData): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: MonthData, newItem: MonthData): Boolean {
        return oldItem == newItem
    }
}

class ReportYearAdapter : ListAdapter<MonthData,ReportYearAdapter.ReportYearViewHolder>(DataMonthDiffCalBack()){

    inner class ReportYearViewHolder( val binding: ItemReportYearBinding): RecyclerView.ViewHolder(binding.root){
        fun binItem( item : MonthData){
            binding.apply {
                tvMonth.text = "Tháng ${item.month.toString()}"
                tvIncome.text = item.income.toString()
                tvExpenditure.text = item.expenditure.toString()
            }
        }
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ReportYearAdapter.ReportYearViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ReportYearViewHolder(ItemReportYearBinding.inflate(inflater,parent,false))
    }

    override fun onBindViewHolder(holder: ReportYearAdapter.ReportYearViewHolder, position: Int) {
        val item = getItem(position)
        holder.binItem(item)
    }
}