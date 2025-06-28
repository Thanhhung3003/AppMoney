package com.example.appmoney.ui.main.feature.different.changepass

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth

class ChangePasswordViewModel : ViewModel() {
    private val _toastMessage = MutableLiveData<String>()
    val toastMessage: LiveData<String> get() = _toastMessage

    fun changePassword(oldPass: String, newPass: String, confirmPass: String) {
        if (!validateInput(oldPass, newPass, confirmPass)) return

        val user = FirebaseAuth.getInstance().currentUser
        val email = user?.email

        if (email != null) {
            val credential = EmailAuthProvider.getCredential(email, oldPass)
            user.reauthenticate(credential)
                .addOnCompleteListener { reAuthTask ->
                    if (reAuthTask.isSuccessful) {
                        user.updatePassword(newPass)
                            .addOnCompleteListener { updateTask ->
                                if (updateTask.isSuccessful) {
                                    _toastMessage.postValue("Đổi mật khẩu thành công")
                                } else {
                                    _toastMessage.postValue("Đổi mật khẩu thất bại: ${updateTask.exception?.message}")
                                }
                            }
                    } else {
                        _toastMessage.postValue("Mật khẩu cũ không đúng")
                    }
                }
        } else {
            _toastMessage.postValue("Không tìm thấy email người dùng")
        }
    }

    private fun validateInput(oldPass: String, newPass: String, confirmPass: String): Boolean {
        return when {
            oldPass.isEmpty() -> {
                _toastMessage.value = "Vui lòng nhập mật khẩu cũ"
                false
            }

            newPass.isEmpty() -> {
                _toastMessage.value = "Vui lòng nhập mật khẩu mới"
                false
            }

            confirmPass.isEmpty() -> {
                _toastMessage.value = "Vui lòng nhập lại mật khẩu mới"
                false
            }

            newPass.length < 6 -> {
                _toastMessage.value = "Mật khẩu phải dài hơn 6 ký tự"
                false
            }

            newPass != confirmPass -> {
                _toastMessage.value = "Mật khẩu không khớp"
                false
            }

            else -> true
        }
    }
}