package com.example.appmoney.ui.main.feature.transactionhistory

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.appmoney.data.model.Category
import com.example.appmoney.data.repository.Repository
import com.example.appmoney.ui.common.extensions.toDetail
import com.example.appmoney.ui.common.helper.TimeHelper
import com.google.firebase.Timestamp
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class HistoryTransViewModel: ViewModel() {

    private val repository = Repository()

    private var allTransactions = mutableListOf<TransactionDetail>()

    private val _transactionsDetail = MutableLiveData<List<TransactionDetail>>()
    val transactionsDetail: LiveData<List<TransactionDetail>> = _transactionsDetail

    fun getTransByMonth(input: String,listCategory: List<Category>,onFailure: (String) -> Unit){
        try {
            val sdf = SimpleDateFormat("MM/yyyy", Locale.getDefault())
            val date = sdf.parse(input) ?: return

            val dateStart = TimeHelper.getStartOfMonth(date)
            val dateEnd = TimeHelper.getEndOfMonth(date)

            repository.getTransByTimes(dateStart,dateEnd,
                onSuccess = { transactions ->
                val transactionDetail = transactions.map { trans ->
                    val matchedCategory = listCategory.find { it.idCat == trans.categoryId }
                    trans.toDetail(matchedCategory)
                }
                    _transactionsDetail.value = transactionDetail
                    allTransactions = transactionDetail.toMutableList()
            },onFailure)
        }catch (e: ParseException){
            Log.e("ViewModel", "Sai định dạng tháng (MM/yyyy)")
        }

    }
    fun delTrans(idTrans: String,onSuccess:()->Unit,onFailure: (String) -> Unit){
        repository.delTrans(idTrans,onSuccess,onFailure)
    }
    fun filterTrans(query:String){
        if (query.isBlank()){
            _transactionsDetail.value = allTransactions
        }else{
            val result = allTransactions.filter {
                it.date.lowercase().contains(query.lowercase()) ||
                it.desCat?.lowercase()?.contains(query.lowercase()) ?: false ||
                it.amount.toString().contains(query)||
                it.note.lowercase().contains(query.lowercase())
            }
            _transactionsDetail.value = result
        }
    }
}
