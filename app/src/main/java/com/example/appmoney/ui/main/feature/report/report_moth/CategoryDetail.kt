package com.example.appmoney.ui.main.feature.report.report_moth

import com.example.appmoney.data.model.CategoryColor
import com.example.appmoney.data.model.CategoryImage

data class CategoryDetail(
    val image: CategoryImage? = CategoryImage.NULL,
    val color: CategoryColor? = CategoryColor.BLACK,
    val desCat: String? = "Trống",
    val amount: Long = 0,
    val percent: Float = 0f
) {
    companion object {
        fun default(amount: Long, percent: Float): CategoryDetail {
            return CategoryDetail(
                image = CategoryImage.NULL,
                color = CategoryColor.BLACK,
                desCat = "Trống",
                amount = amount,
                percent = percent
            )
        }
    }
}