package com.example.fitforma.ui.account

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.fitforma.databinding.ActivityAccountBinding
import com.google.firebase.auth.FirebaseAuth

class AccountActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAccountBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}