package com.example.appmoney.ui.main.feature.report.report_moth.report_exp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appmoney.databinding.FragmentRpExpByMonthBinding
import com.example.appmoney.ui.main.feature.report.report_moth.ReportMonthAdapter
import com.example.appmoney.ui.main.feature.report.report_moth.RpByMonthViewModel
import com.example.appmoney.ui.main.feature.report.report_moth.setupPieChart

class RpExpByMonthFragment : Fragment() {
    private var _binding: FragmentRpExpByMonthBinding? = null
    private val binding get() = _binding!!
    private lateinit var shareViewModel: RpByMonthViewModel
    private val adapter = ReportMonthAdapter()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRpExpByMonthBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        shareViewModel = ViewModelProvider(requireParentFragment())[RpByMonthViewModel::class.java]

        binding.rcReportExp.layoutManager = LinearLayoutManager(requireContext())
        binding.rcReportExp.adapter = adapter

        setupObserve()

    }
    private fun setupObserve() {
        shareViewModel.expDetailList.observe(viewLifecycleOwner){ list ->
            setupPieChart(binding.pieChartExp,list,requireContext())
            adapter.submitList(list)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}