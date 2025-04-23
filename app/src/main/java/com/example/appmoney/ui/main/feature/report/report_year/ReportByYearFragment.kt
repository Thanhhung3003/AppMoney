package com.example.appmoney.ui.main.feature.report.report_year

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.appmoney.R

/**
 * A simple [Fragment] subclass.
 * Use the [ReportByYearFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ReportByYearFragment : Fragment() {



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_year_report, container, false)
    }

}