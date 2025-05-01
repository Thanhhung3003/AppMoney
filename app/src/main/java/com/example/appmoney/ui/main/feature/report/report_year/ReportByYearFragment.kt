package com.example.appmoney.ui.main.feature.report.report_year

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appmoney.R
import com.example.appmoney.databinding.FragmentYearReportBinding
import com.example.appmoney.ui.common.helper.showApiResultToast
import com.example.appmoney.ui.main.feature.transactionhistory.MonthYearDialog
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import java.util.Calendar

class ReportByYearFragment : Fragment() {
    private var _binding: FragmentYearReportBinding? = null
    private val binding get() = _binding!!
    private val calendar = Calendar.getInstance()
    private lateinit var viewModel: RpByYearViewModel
    private var dataList = mutableListOf<MonthData>()
    private val adapter = ReportYearAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentYearReportBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[RpByYearViewModel::class.java]
        setupDate()
        observe()
        binding.rcReportYear.layoutManager = LinearLayoutManager(requireContext())
        binding.rcReportYear.adapter = adapter
        updateText()

    }

    private fun observe() {
        viewModel.monthData.observe(viewLifecycleOwner){ list ->
            showIncomeExpenseBarChart(binding.barChart,list)
            adapter.submitList(list)
        }
    }
    fun showIncomeExpenseBarChart(barChart: BarChart, dataList: List<MonthData>) {
        val incomeEntries = ArrayList<BarEntry>()
        val expenseEntries = ArrayList<BarEntry>()
        val monthLabels = ArrayList<String>()

        dataList.forEachIndexed { index, data ->
            incomeEntries.add(BarEntry(index.toFloat(), data.income.toFloat()))
            expenseEntries.add(BarEntry(index.toFloat(), data.expenditure.toFloat()))
            monthLabels.add("Th${data.month}")
        }

        val incomeSet = BarDataSet(incomeEntries, "Thu").apply {
            color = Color.rgb(104, 241, 175)
        }
        val expenseSet = BarDataSet(expenseEntries, "Chi").apply {
            color = Color.rgb(244, 117, 117)
        }

        val barWidth = 0.4f
        val barSpace = 0.05f
        val groupSpace = 0.2f
        val groupCount = dataList.size

        val barData = BarData(incomeSet, expenseSet).apply {
            this.barWidth = barWidth
        }

        val xAxis = barChart.xAxis
        xAxis.valueFormatter = IndexAxisValueFormatter(monthLabels)
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.granularity = 1f
        xAxis.setCenterAxisLabels(true)
        xAxis.setDrawGridLines(false)
        xAxis.labelRotationAngle = -45f

        val groupWidth = barData.getGroupWidth(groupSpace, barSpace)
        val startX = 0f
        val endX = startX + groupCount * groupWidth

        xAxis.axisMinimum = startX
        xAxis.axisMaximum = endX

        barChart.axisLeft.axisMinimum = 0f
        barChart.axisRight.isEnabled = false

        barChart.apply {
            this.data = barData
            this.description.isEnabled = false
            this.legend.isEnabled = true
            this.setVisibleXRangeMaximum(6f)
            this.isDragEnabled = true
            this.setScaleEnabled(false)
            this.setPinchZoom(false)

            barData.groupBars(startX, groupSpace, barSpace)
            invalidate()
        }
    }




    private fun setupDate() {
        binding.tvMonthDate.setOnClickListener {
           YearDialog(requireContext()) { year ->
                calendar.set(Calendar.YEAR, year)
                updateText()
            }.show()
        }
        binding.btnPre.setOnClickListener {
            calendar.add(Calendar.YEAR, -1)
            updateText()
        }
        binding.btnAfter.setOnClickListener {
            calendar.add(Calendar.YEAR, 1)
            updateText()
        }
    }

    private fun updateText() {
        val year = calendar.get(Calendar.YEAR)
        val date = "$year"
        binding.tvMonthDate.text = "Năm $date"
        fetchData(date)
    }
    private fun fetchData(date: String) {
        viewModel.getTransByYear(date) { err ->
            showApiResultToast(false,err)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}