package com.example.appmoney.ui.common.helper

import android.widget.Toast
import androidx.fragment.app.Fragment

fun Fragment.showApiResultToast(isSuccess: Boolean = true, message: String? = "", duration: Int = Toast.LENGTH_SHORT) {
    val msg = if (isSuccess) "Thành công" else "Thất bại: $message"
    requireActivity().runOnUiThread {
        Toast.makeText(context, msg, duration).show()
    }
}