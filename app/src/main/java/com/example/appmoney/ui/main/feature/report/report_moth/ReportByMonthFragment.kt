package com.example.appmoney.ui.main.feature.report.report_moth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.appmoney.databinding.FragmentMonthReportBinding
import com.example.appmoney.ui.common.helper.showApiResultToast
import com.example.appmoney.ui.main.feature.transactionhistory.MonthYearDialog
import com.example.appmoney.ui.main.main_screen.ScreenHomeViewModel
import com.google.android.material.tabs.TabLayoutMediator
import java.util.Calendar


class ReportByMonthFragment : Fragment() {

    private var _binding: FragmentMonthReportBinding? = null
    private val binding get() = _binding!!
    private var calendar = Calendar.getInstance()
    private lateinit var viewModel : RpByMonthViewModel
    private lateinit var shareViewModel: ScreenHomeViewModel

    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMonthReportBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[RpByMonthViewModel::class.java]
        shareViewModel = ViewModelProvider(requireActivity())[ScreenHomeViewModel::class.java]


        updateText()
        setupDate()
        setupObserver()
        setupViewpager()
        binding.tvTotalExpenditure.text


    }

    private fun setupViewpager() {
        val adapterVp = VpReportByMonthAdapter(childFragmentManager,lifecycle)
        binding.vpReport.adapter = adapterVp
        TabLayoutMediator(binding.tabReport,binding.vpReport){ tab,position ->
            when(position){
                0 -> tab.text = "Chi tiêu"
                1 -> tab.text = "Thu nhập"
            }
        }.attach()
    }

    private fun setupObserver() {
        shareViewModel.incomeList.observe(viewLifecycleOwner){ list ->
            viewModel.setIncomeList(list)
        }
        shareViewModel.expList.observe(viewLifecycleOwner){ list ->
            viewModel.setExpenditureList(list)
        }
        viewModel.sumExpenditure.observe(viewLifecycleOwner){ totalExpenditure ->
            viewModel.sumIncome.observe(viewLifecycleOwner){ totalIncome ->
                binding.tvTotalExpenditure.text = "${totalExpenditure.toString()}đ"
                binding.tvTotalIncome.text = "${totalIncome.toString()}đ"
                val total = totalIncome - totalExpenditure
                binding.tvTotalMonth.text = "${total.toString()}đ"
            }
        }

    }

    private fun setupDate() {
        binding.tvMonthDate.setOnClickListener {
            MonthYearDialog(requireContext()){ moth, year ->
                calendar.set(Calendar.MONTH, moth - 1)
                calendar.set(Calendar.YEAR,year)
                updateText()
            }.show()
        }
        binding.btnPre.setOnClickListener {
            calendar.add(Calendar.MONTH,-1)
            updateText()
        }
        binding.btnAfter.setOnClickListener {
            calendar.add(Calendar.MONTH,1)
            updateText()
        }
    }

    private fun updateText() {
        val month = calendar.get(Calendar.MONTH) + 1
        val year = calendar.get(Calendar.YEAR)
        val date = "$month/$year"
        binding.tvMonthDate.text = "Tháng $date"
        fetchData(date)
    }

    private fun fetchData(date: String) {
        viewModel.getTransbyMonth(date) { err ->
            showApiResultToast(false,err)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

    }

}