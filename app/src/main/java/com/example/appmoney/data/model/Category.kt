package com.example.appmoney.data.model

import com.example.appmoney.R
import java.io.Serializable

interface Selectable {
    val isSelected: Boolean
}

enum class CategoryType(var viewType: Int) {
    ITEM (0),
    BUTTON(1)
}
data class Category(
    val idCat: String = "",
    val image: CategoryImage = CategoryImage.BUS,
    val color: CategoryColor = CategoryColor.BLACK,
    val desCat: String = "",
    val categoryType: CategoryType = CategoryType.ITEM,
    val timeCreate :Long = 0L,
    override val isSelected: Boolean = false
): Selectable, Serializable {}

enum class CategoryImage(val resource: Int) {
    BUS(R.drawable.bus),
    BOOK(R.drawable.book),
    EDIT(R.drawable.right),
    BALL(R.drawable.americanfootball),
    BASKET(R.drawable.basket),
    GAME(R.drawable.game),
    NULL(R.drawable.resource_null),

    ;
}

data class CategoryImageWrapper(
    val categoryImage: CategoryImage,
    override val isSelected: Boolean
): Selectable

enum class CategoryColor(val resource: Int) {
    BLUE(R.color.colorBlue),
    BLACK(R.color.black),
    GREEN(R.color.colorGreen),
    YELLOW(R.color.colorYellow),
    RED(R.color.colorRed),
    ORANGE(R.color.colorOrange),
    PURPLE(R.color.colorPurple),
    PINK(R.color.colorPink),
    TEAL(R.color.colorTeal);

}
data class CategoryColorWrapper(
    val categoryColor: CategoryColor,
    override val isSelected: Boolean
): Selectable



