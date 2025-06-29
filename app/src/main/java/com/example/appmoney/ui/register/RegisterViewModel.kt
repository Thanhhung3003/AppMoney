package com.example.appmoney.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.appmoney.data.repository.Repository
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.auth.User

class RegisterViewModel : ViewModel() {
    private val repo = Repository()

    private val _registerSuccess = MutableLiveData<Boolean>()
    val registerSuccess: LiveData<Boolean> get() = _registerSuccess

    private val _err = MutableLiveData<String?>()
    val err: LiveData<String?> = _err

    fun clearError() {
        _err.value = null
    }

    fun registerUser(
        email: String,
        password: String
    ) {
        repo.registerUser(email, password, onSuccess = { user ->
            createUserAndAddDefaultCategories(user)
        }, onFailure={ error ->
            _err.value = error
        })
    }

    fun createUserAndAddDefaultCategories(user: FirebaseUser) {
        repo.createUser(
            user, onSuccess = {
                repo.addDefaultCategoriesForNewUser(
                    onSuccess = {
                        _registerSuccess.value = true
                    },
                    onFailure = { error ->
                        _err.value = "Lỗi thêm danh mục mặc định: $error"
                    })
            },
            onFailure = { error ->
                _err.value = "Lỗi tạo User: $error"
            })
    }

}