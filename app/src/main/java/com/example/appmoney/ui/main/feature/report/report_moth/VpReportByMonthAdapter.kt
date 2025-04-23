package com.example.appmoney.ui.main.feature.report.report_moth

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.appmoney.ui.main.feature.report.report_moth.report_exp.RpExpByMonthFragment
import com.example.appmoney.ui.main.feature.report.report_moth.report_income.RpIncomeByMonthFragment

class VpReportByMonthAdapter(fragmentManager: FragmentManager,lifecycle: Lifecycle): FragmentStateAdapter(fragmentManager,lifecycle) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> RpExpByMonthFragment()
            else ->RpIncomeByMonthFragment()
        }
    }
}