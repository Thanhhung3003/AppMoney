package com.example.appmoney.ui.main.feature.report.report_moth

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.appmoney.data.model.Category
import com.example.appmoney.data.model.CategoryColor
import com.example.appmoney.data.model.CategoryImage
import com.example.appmoney.data.repository.Repository
import com.example.appmoney.ui.main.feature.report.CategoryDetail
import com.google.firebase.Timestamp
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class RpByMonthViewModel: ViewModel() {

    private val repository = Repository()

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

    fun getTransbyMonth(date: String, onFailure: (String) -> Unit) {
        try {
            val sdf = SimpleDateFormat("MM/yyyy", Locale.getDefault())
            val date = sdf.parse(date) ?: return
            val calendar = Calendar.getInstance()
            calendar.time = date

            calendar.set(Calendar.DAY_OF_MONTH, 1)
            calendar.set(Calendar.HOUR_OF_DAY, 0)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)
            calendar.set(Calendar.MILLISECOND, 0)
            val startDay = Timestamp(calendar.time)

            calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH))
            calendar.set(Calendar.HOUR_OF_DAY, 23)
            calendar.set(Calendar.MINUTE, 59)
            calendar.set(Calendar.SECOND, 59)
            calendar.set(Calendar.MILLISECOND, 999)
            val endDay = Timestamp(calendar.time)

            repository.getTransByTimes(startDay, endDay, onSuccess = { transactions ->
                val incomeTransList = transactions.filter { it.typeTrans == "Income" }
                val expTransList = transactions.filter { it.typeTrans == "Expenditure" }

                val totalIncome = incomeTransList.sumOf { it.amount }
                val totalExpenditure = expTransList.sumOf { it.amount }

                _sumIncome.value = totalIncome
                _sumExpenditure.value = totalExpenditure

                val incomeCatsMap = incomeCatList.value?.associateBy { it.idCat } ?: emptyMap()
                val expCatsMap = expCatList.value?.associateBy { it.idCat } ?: emptyMap()

                val defaultCategoryDetail = CategoryDetail(
                    image = CategoryImage.NULL,
                    color = CategoryColor.BLACK,
                    desCat = "Trống",
                    amount = 0L,
                    percent = 0f
                )

                val incomeDetail = incomeTransList
                    .groupBy { it.categoryId }
                    .mapNotNull { (catId, transList) ->
                        val amount = transList.sumOf { it.amount }
                        if (amount > 0) {
                            val percent = if (totalIncome > 0) (amount * 100f) / totalIncome else 0f
                            val cat = incomeCatsMap[catId]
                            cat?.toDetailCategory(amount, percent) ?: defaultCategoryDetail.copy(
                                amount = amount,
                                percent = percent
                            )
                        } else null
                    }

                val expDetail = expTransList
                    .groupBy { it.categoryId }
                    .mapNotNull { (catId, transList) ->
                        val amount = transList.sumOf { it.amount }
                        if (amount > 0) {
                            val percent =
                                if (totalExpenditure > 0) (amount * 100f) / totalExpenditure else 0f
                            val cat = expCatsMap[catId]
                            cat?.toDetailCategory(amount, percent) ?: defaultCategoryDetail.copy(
                                amount = amount,
                                percent = percent
                            )
                        } else null
                    }
                _incomeDetailList.value = incomeDetail
                _expDetailList.value = expDetail
            }, onFailure)
        } catch (e: ParseException) {
            Log.e("ViewModel", "Sai định dạng tháng (MM/yyyy)")
        }
    }

}
fun Category.toDetailCategory(amount: Long,percent: Float): CategoryDetail {
    return CategoryDetail(
        image = image ,
        color = color ,
        desCat = desCat ,
        amount = amount,
        percent = percent)
}
