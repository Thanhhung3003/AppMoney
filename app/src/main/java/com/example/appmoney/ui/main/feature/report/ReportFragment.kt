package com.example.appmoney.ui.main.feature.report

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.appmoney.databinding.FragmentReportBinding
import com.example.appmoney.ui.main.main_screen.ScreenHomeViewModel
import com.google.android.material.tabs.TabLayoutMediator


class ReportFragment : Fragment() {
    private var _binding : FragmentReportBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReportBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupVPReport()

    }

    private fun setupVPReport() {
        val adapter = VpReportAdapter(childFragmentManager, lifecycle)
        binding.vpReport.adapter = adapter
        TabLayoutMediator(binding.tabDateReport, binding.vpReport) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = "Hàng tháng"
                }

                1 -> {
                    tab.text = "Hàng năm"
                }
            }
        }.attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}