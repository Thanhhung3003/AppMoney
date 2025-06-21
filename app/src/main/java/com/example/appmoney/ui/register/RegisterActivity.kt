package com.example.appmoney.ui.register

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.appmoney.databinding.ActivityRegisterBinding
import com.example.appmoney.ui.login.LoginActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore


class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth
        binding.btnRegister.setOnClickListener {
            createAcc()
        }
        binding.btnBack.setOnClickListener {
            goToLogin()
        }

    }

    private fun createAcc() {
        val email = binding.edtUsername.text.toString().trim()
        val password = binding.edtPassword.text.toString().trim()
        val confirmPassword = binding.edtConfirmPassword.text.toString().trim()
        if (!validateInput(email, password, confirmPassword)) return

        registerUser(email, password)
    }

    private fun validateInput(email: String, password: String, confirmPassword: String): Boolean {
        return when {
            email.isEmpty() -> {
                showToast("Vui lòng nhập email")
                false
            }
            password.isEmpty() -> {
                showToast("Vui lòng nhập mật khẩu")
                false
            }
            confirmPassword.isEmpty() -> {
                showToast("Vui lòng nhập lại mật khẩu")
                false
            }
            password.length < 6 -> {
                showToast("Mật khẩu phải dài hơn 6 ký tự")
                false
            }
            password != confirmPassword -> {
                showToast("Mật khẩu không khớp")
                false
            }
            else -> true
        }
    }

    private fun registerUser( email: String,password: String){
        auth.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful){
                    val user = auth.currentUser
                    createUser(user)
                    goToLogin()
                }else{
                    showToast("Đăng ký thất bại: ${task.exception?.message}")
                }
            }
    }

    private fun createUser(user: FirebaseUser?) {
        user?.let {
            val db = Firebase.firestore
            val userData = hashMapOf(
                "email" to user.email,
                "createAt" to System.currentTimeMillis(),
                "role" to "user"
            )
            db.collection("User").document(user.uid)
                .set(userData)
                .addOnSuccessListener {
                    Toast.makeText(this,"Thành công",Toast.LENGTH_SHORT).show()
                }.addOnFailureListener{
                    Toast.makeText(this,"Thất bại",Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun goToLogin() {
        val i = Intent(this@RegisterActivity,LoginActivity::class.java)
        startActivity(i)
    }

    private fun showToast(mess: String) {
        Toast.makeText(this@RegisterActivity,mess,Toast.LENGTH_SHORT).show()
    }
}