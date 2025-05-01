package com.example.appmoney.ui.main.feature.report.report_year

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.appmoney.data.model.Category
import com.example.appmoney.data.repository.Repository
import com.example.appmoney.ui.common.helper.TransactionSummary
import com.example.appmoney.ui.common.helper.TimeHelper
import com.google.firebase.Timestamp
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class RpByYearViewModel: ViewModel() {

    private val repository = Repository()

    private val _monthData = MutableLiveData<List<MonthData>>()
    val monthData: LiveData<List<MonthData>> get() = _monthData

    fun getTransByYear(
        date: String,
        onFailure: (String) -> Unit
    ){
        try {
            val sdf = SimpleDateFormat("yyyy", Locale.getDefault())
            val dateTime = sdf.parse(date) ?: return

            val dateStart = TimeHelper.getStartOfYear(dateTime)
            val dateEnd = TimeHelper.getEndOfYear(dateTime)

            repository.getTransByTimes(dateStart,dateEnd, onSuccess = { transactions ->
                val list = transactions.groupBy {
                    val cal = Calendar.getInstance()
                    try {
                        val dateObj = TimeHelper.stringToDate(it.date)
                        cal.time = dateObj
                        cal.get(Calendar.MONTH) + 1
                    } catch (e: Exception) {
                        Log.e("RpByYearViewModel", "Lỗi parse date: ${it.date}")
                        0
                    }
                }
                val mothData = (1..12).map { month ->
                    val mothTrans = list[month] ?: emptyList()
                    val income = mothTrans.filter { it.typeTrans == "Income" }.sumOf { it.amount }
                    val expenditure = mothTrans.filter { it.typeTrans == "Expenditure" }.sumOf { it.amount }
                    MonthData(month,income,expenditure)
                }
                _monthData.value = mothData

            },onFailure)

        } catch (e: ParseException) {
            Log.e("ViewModel", "Sai định dạng tháng (MM/yyyy)")
        }
    }

}


