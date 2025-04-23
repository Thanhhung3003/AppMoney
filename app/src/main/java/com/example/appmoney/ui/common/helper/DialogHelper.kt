package com.example.appmoney.ui.common.helper

import android.content.Context
import androidx.appcompat.app.AlertDialog

object DialogHelper {
    fun showRequestDialog(
        context: Context,
        title: String = "",
        message: String = "",
        onConfirm: () -> Unit = {},
        onCancel: () -> Unit = {}
    ) {
        AlertDialog.Builder(context).apply {
            setTitle(title)
            setMessage(message)
            setPositiveButton("Xóa") { _, _ ->
                onConfirm()
            }
            setNeutralButton("Hủy") { _, _ ->
                onCancel()
            }
            setCancelable(false)
        }.show()
    }
}