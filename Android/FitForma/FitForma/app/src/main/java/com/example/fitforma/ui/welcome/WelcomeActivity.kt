package com.example.fitforma.ui.welcome

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.fitforma.R
import com.example.fitforma.ui.login.LoginActivity

class WelcomeActivity : AppCompatActivity() {
    private val delayMillis: Long = 2000 //seconds

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        // Use a Handler to delay the transition to the login page
        Handler().postDelayed({
            startActivity(Intent(this, LoginActivity::class.java))
            finish() // close the welcome activity so the user can't go back to it
        }, delayMillis)
    }
}