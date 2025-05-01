package com.example.appmoney.ui.common.helper

import com.example.appmoney.data.model.Category
import com.example.appmoney.data.model.Transaction
import com.example.appmoney.ui.main.feature.report.report_moth.CategoryDetail
import com.example.appmoney.ui.common.extensions.toDetailCategory

class TransactionSummary {
    fun calculateTotalAmount (transaction : List<Transaction>):Long{
        return transaction.sumOf { it.amount }
    }
    fun summarizeTransaction(
        transaction: List<Transaction>,
        categoryMap : Map<String,Category>,
        totalAmount: Long
    ) : List<CategoryDetail>{
        return transaction.groupBy { it.categoryId }
            .mapNotNull { (categoryId, transList) ->
                val amount = transList.sumOf { it.amount }
                if(amount >0){
                    val percent = if(totalAmount > 0) (amount* 100f)/totalAmount else 0f
                    val category = categoryMap[categoryId]
                    category?.toDetailCategory(amount,percent) ?: CategoryDetail.default(amount,percent)
                }else{
                    null
                }
            }
    }
}