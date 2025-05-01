package com.example.appmoney.ui.main.feature.report.report_moth

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.appmoney.data.model.Category
import com.example.appmoney.data.repository.Repository
import com.example.appmoney.ui.common.helper.TransactionSummary
import com.example.appmoney.ui.common.helper.TimeHelper
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale

class RpByMonthViewModel: ViewModel() {

    private val repository = Repository()
    private val transSummary = TransactionSummary()

    private val _incomeCatList = MutableLiveData<List<Category>>()
    val incomeCatList: LiveData<List<Category>> get() = _incomeCatList

    private val _expCatList = MutableLiveData<List<Category>>()
    val expCatList: LiveData<List<Category>> get() = _expCatList

    private val _incomeDetailList = MutableLiveData<List<CategoryDetail>>()
    val incomeDetailList : LiveData<List<CategoryDetail>> get() = _incomeDetailList

    private val _expDetailList = MutableLiveData<List<CategoryDetail>>()
    val expDetailList : LiveData<List<CategoryDetail>> get() = _expDetailList

    private val _sumIncome = MutableLiveData<Long>()
    val sumIncome : LiveData<Long> get() = _sumIncome

    private val _sumExpenditure = MutableLiveData<Long>()
    val sumExpenditure : LiveData<Long> get() = _sumExpenditure

    fun setIncomeList(category: List<Category>){
        _incomeCatList.value = category
    }
    fun setExpenditureList(category: List<Category>){
        _expCatList.value = category
    }

    fun getTransByMonth(date: String, onFailure: (String) -> Unit) {
        try {
            val sdf = SimpleDateFormat("MM/yyyy", Locale.getDefault())
            val dateTime = sdf.parse(date) ?: return

            val dateStart = TimeHelper.getStartOfMonth(dateTime)
            val dateEnd = TimeHelper.getEndOfMonth(dateTime)

            repository.getTransByTimes(dateStart, dateEnd, onSuccess = { transactions ->
                val incomeTransList = transactions.filter { it.typeTrans == "Income" }
                val expTransList = transactions.filter { it.typeTrans == "Expenditure" }

                val totalIncome = transSummary.calculateTotalAmount(incomeTransList)
                val totalExpenditure = transSummary.calculateTotalAmount(expTransList)

                _sumIncome.value = totalIncome
                _sumExpenditure.value = totalExpenditure

                val incomeCatsMap = incomeCatList.value?.associateBy { it.idCat } ?: emptyMap()
                val expCatsMap = expCatList.value?.associateBy { it.idCat } ?: emptyMap()

                _incomeDetailList.value = transSummary.summarizeTransaction(incomeTransList,incomeCatsMap,totalIncome)
                _expDetailList.value = transSummary.summarizeTransaction(expTransList,expCatsMap,totalExpenditure)
            }, onFailure)
        } catch (e: ParseException) {
            Log.e("ViewModel", "Sai định dạng tháng (MM/yyyy)")
        }
    }
}

