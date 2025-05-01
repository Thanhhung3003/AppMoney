package com.example.appmoney.ui.common.extensions

import com.example.appmoney.data.model.Category
import com.example.appmoney.ui.main.feature.report.report_moth.CategoryDetail


fun Category.toDetailCategory(amount: Long, percent: Float): CategoryDetail {
    return CategoryDetail(
        image = image,
        color = color,
        desCat = desCat,
        amount = amount,
        percent = percent
    )
}
