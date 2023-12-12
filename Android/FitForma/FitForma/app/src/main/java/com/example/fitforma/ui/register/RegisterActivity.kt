package com.example.fitforma.ui.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.fitforma.R
import com.example.fitforma.ui.login.LoginActivity

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
    }

    fun navigateToLogin(view: android.view.View){
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }
}