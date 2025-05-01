package com.example.appmoney.ui.main.feature.report.report_moth

import android.content.Context
import android.graphics.Color
import androidx.core.content.ContextCompat
import com.example.appmoney.R
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry

fun setupPieChart(pieChart: PieChart, list: List<CategoryDetail>, context: Context) {
    val entries = list.map {
        PieEntry(it.amount.toFloat(), it.desCat ?: "Không tên")
    }

    val colors = list.map {
        val resId = it.color?.resource ?: R.color.black
        ContextCompat.getColor(context, resId)
    }

    val dataSet = PieDataSet(entries, "").apply {
        this.colors = colors
        valueTextSize = 14f
        valueTextColor = Color.WHITE
    }

    val data = PieData(dataSet)

    pieChart.data = data
    pieChart.description.isEnabled = false
    pieChart.setUsePercentValues(true)
    pieChart.setDrawEntryLabels(true)
    pieChart.legend.isEnabled = false
    pieChart.setEntryLabelColor(Color.BLACK)
    pieChart.setCenterText("Thống kê")
    pieChart.setCenterTextSize(18f)
    pieChart.animateY(1000, Easing.EaseInOutQuad)
    pieChart.invalidate()
}