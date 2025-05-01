package com.example.appmoney.ui.common.extensions

import com.example.appmoney.data.model.Category
import com.example.appmoney.data.model.Transaction
import com.example.appmoney.ui.main.feature.transactionhistory.TransactionDetail

fun Transaction.toDetail(category: Category?): TransactionDetail {
    return TransactionDetail(
        idTrans = idTrans,
        amount = amount,
        date = date,
        note = note,
        categoryId = categoryId,
        typeTrans = typeTrans,
        image = category?.image,
        color = category?.color,
        desCat = category?.desCat,
    )
}