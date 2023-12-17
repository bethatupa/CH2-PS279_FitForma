package com.example.fitforma.ui.food

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.fitforma.R
import com.example.fitforma.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //TODO connect search with detail
    }
}