package com.example.appmoney.ui.main.feature.report.report_moth.report_income

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appmoney.databinding.FragmentRpIncomeByMonthBinding
import com.example.appmoney.ui.main.feature.report.report_moth.ReportMonthAdapter
import com.example.appmoney.ui.main.feature.report.report_moth.RpByMonthViewModel
import com.example.appmoney.ui.main.feature.report.report_moth.setupPieChart


class RpIncomeByMonthFragment : Fragment() {
    private var _binding: FragmentRpIncomeByMonthBinding? = null
    private val binding get() = _binding!!
    private lateinit var shareViewModel: RpByMonthViewModel
    private val adapter = ReportMonthAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRpIncomeByMonthBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        shareViewModel = ViewModelProvider(requireParentFragment())[RpByMonthViewModel::class.java]

        binding.rcReportIncome.layoutManager = LinearLayoutManager(requireContext())
        binding.rcReportIncome.adapter = adapter

        setupObserve()

    }

    private fun setupObserve() {
        shareViewModel.incomeDetailList.observe(viewLifecycleOwner){ list ->
            setupPieChart(binding.pieChartIncome,list,requireContext())
            adapter.submitList(list)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}