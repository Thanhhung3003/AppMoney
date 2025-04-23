package com.example.appmoney.ui.common.helper

import android.os.Bundle
import java.io.Serializable

object BundleHelper {

    private val data = mutableMapOf<String, Serializable>()

    fun addParam(key: String, value: Serializable): BundleHelper {
        data[key] = value
        return this
    }

    fun build(): Bundle {
        val datas = data.toList()
        data.clear()
        val bundle = Bundle()
        datas.onEach {
            bundle.putSerializable(it.first, it.second)
        }
        return bundle
    }
}