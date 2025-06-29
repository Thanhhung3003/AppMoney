package com.example.appmoney.ui.common.helper

import com.example.appmoney.data.model.Category
import com.example.appmoney.data.model.CategoryColor
import com.example.appmoney.data.model.CategoryImage

object DefaultCategoryProvider {
    fun getDefaultIncomeCategories(): List<Category> = listOf(
        Category(
            image = CategoryImage.SALARY,
            color = CategoryColor.BLUE,
            desCat = "Lương",
            timeCreate = System.currentTimeMillis()
        ),
        Category(
            image = CategoryImage.GIFT,
            color = CategoryColor.GREEN,
            desCat = "Thưởng",
            timeCreate = System.currentTimeMillis()
        )
        ,
        Category(
            image = CategoryImage.INVESTMENT,
            color = CategoryColor.YELLOW,
            desCat = "Đầu tư",
            timeCreate = System.currentTimeMillis()
        ),
        Category(
            image = CategoryImage.REFUND,
            color = CategoryColor.RED,
            desCat = "Quá hạn",
            timeCreate = System.currentTimeMillis()
        ),
        Category(
            image = CategoryImage.RENT,
            color = CategoryColor.PINK,
            desCat = "Cho thuê",
            timeCreate = System.currentTimeMillis()
        ),
        Category(
            image = CategoryImage.BOOK,
            color = CategoryColor.ORANGE,
            desCat = "Nhuận bút",
            timeCreate = System.currentTimeMillis()
        ),
        Category(
            image = CategoryImage.EDUCATION,
            color = CategoryColor.BLACK,
            desCat = "Gia sư",
            timeCreate = System.currentTimeMillis()
        )
    )

    fun getDefaultExpenditureCategories(): List<Category> = listOf(
        Category(
            image = CategoryImage.BASKET,
            color = CategoryColor.RED,
            desCat = "Mua sắm",
            timeCreate = System.currentTimeMillis()
        ),
        Category(
            image = CategoryImage.FUELSTATION,
            color = CategoryColor.ORANGE,
            desCat = "Xăng xe",
            timeCreate = System.currentTimeMillis()
        ),
        Category(
            image = CategoryImage.TSHIRT,
            color = CategoryColor.PURPLE,
            desCat = "Thời trang",
            timeCreate = System.currentTimeMillis()
        ),
        Category(
            image = CategoryImage.BALL,
            color = CategoryColor.PINK,
            desCat = "Thể thao",
            timeCreate = System.currentTimeMillis()
        ),
        Category(
            image = CategoryImage.GAME,
            color = CategoryColor.TEAL,
            desCat = "Game",
            timeCreate = System.currentTimeMillis()
        ),
        Category(
            image = CategoryImage.ELECTRIC,
            color = CategoryColor.YELLOW,
            desCat = "Điện",
            timeCreate = System.currentTimeMillis()
        ),
        Category(
            image = CategoryImage.MEDICAL,
            color = CategoryColor.GREEN,
            desCat = "Sức khỏe",
            timeCreate = System.currentTimeMillis()
        )
    )
}