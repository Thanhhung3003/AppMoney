package com.example.appmoney.ui.main.feature.report

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.appmoney.ui.main.feature.report.report_moth.ReportByMonthFragment
import com.example.appmoney.ui.main.feature.report.report_year.ReportByYearFragment

class VpReportAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                ReportByMonthFragment()
            }

            else -> {
                ReportByYearFragment()
            }
        }
    }
}
