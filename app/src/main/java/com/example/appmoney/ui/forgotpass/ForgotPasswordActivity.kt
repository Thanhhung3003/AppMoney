package com.example.appmoney.ui.forgotpass

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.appmoney.R
import com.example.appmoney.databinding.ActivityForgotPasswordBinding
import com.example.appmoney.ui.login.LoginActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class ForgotPasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityForgotPasswordBinding
    private lateinit var auth : FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        binding.btnBack.setOnClickListener {
            goToLogin()
        }
        binding.btnReset.setOnClickListener {
            val email = binding.edtEmail.text.toString().trim()
            if (email.isBlank()){
                Toast.makeText(this,"Vui lòng nhập Email",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            auth.sendPasswordResetEmail(email)
                .addOnSuccessListener {
                   goToLogin()
                }.addOnFailureListener { err ->
                    Toast.makeText(this,"${err.message}",Toast.LENGTH_SHORT).show()
                }
        }

    }

    private fun goToLogin(){
        val i = Intent(this,LoginActivity::class.java)
        startActivity(i)
    }
}