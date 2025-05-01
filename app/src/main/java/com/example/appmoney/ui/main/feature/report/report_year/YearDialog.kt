package com.example.appmoney.ui.main.feature.report.report_year

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import com.example.appmoney.databinding.YearDialogBinding
import java.util.Calendar

class YearDialog(
    private val context: Context,
    private val showYearDialog: (year:Int) -> Unit
) {
    private val calendar = Calendar.getInstance()
    fun show(){
        val binding  = YearDialogBinding.inflate(LayoutInflater.from(context))
        val dialog = AlertDialog.Builder(context).setView(binding.root).create()

        val currentYear = calendar.get(Calendar.YEAR)

        binding.apply {
            npYear.minValue = 2000
            npYear.maxValue = 2050
            npYear.value = currentYear

            btnOk.setOnClickListener {
                val year = npYear.value
                showYearDialog(year)
                dialog.dismiss()
            }
            btnCancel.setOnClickListener { dialog.dismiss() }
        }
        dialog.show()
    }
}