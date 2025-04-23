package com.example.appmoney.ui.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.appmoney.databinding.ActivityLoginBinding
import com.example.appmoney.ui.forgotpass.ForgotPasswordActivity
import com.example.appmoney.ui.main.main_screen.ScreenHomeActivity
import com.example.appmoney.ui.register.RegisterActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class LoginActivity : AppCompatActivity() {
    private lateinit var binding : ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        binding.btnLogin.setOnClickListener {
            loginAcc()
        }
        binding.btnRegister.setOnClickListener {
            val i = Intent(this, RegisterActivity::class.java)
            startActivity(i)
        }
        binding.btnForgotPass.setOnClickListener {
            val i = Intent(this, ForgotPasswordActivity::class.java)
            startActivity(i)
        }
    }

    private fun loginAcc() {
        val email = binding.edtUsername.text.toString().trim()
        val password = binding.edtPassword.text.toString().trim()
        if (email.isEmpty()||password.isEmpty()){
            Toast.makeText(this@LoginActivity,"Bạn chưa nhập đủ thông tin",Toast.LENGTH_SHORT).show()
            return
        }else{
            auth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful){
                        val user = auth.currentUser
                        goToScreenHome()
                    }else {
                        Toast.makeText(this@LoginActivity, "Authentication failed.", Toast.LENGTH_SHORT,).show()
                    }
                }
        }
    }

    private fun goToScreenHome() {
        var i = Intent(this, ScreenHomeActivity::class.java)
        startActivity(i)
    }

}