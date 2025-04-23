package com.example.appmoney.ui.common.helper

import android.util.Log

object TabObject {

    var tabPosition: Int = 0
        private set

    fun changeTab(tab: Int) {
        tabPosition = tab
        Log.d("tabPosition", tabPosition.toString())
    }
}
