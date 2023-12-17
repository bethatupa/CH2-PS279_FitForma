package com.example.fitforma.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.fitforma.R
import com.example.fitforma.databinding.ActivityMainBinding
import com.example.fitforma.ui.account.AccountFragment
import com.example.fitforma.ui.search.SearchFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bottomNavigationView = binding.bottomNav
        bottomNavigationView.setOnItemSelectedListener{ bottom_navigation_menu ->
            when(bottom_navigation_menu.itemId){
                R.id.navigation_home -> {
                    replaceFragment(MainFragment())
                    true
                }
                R.id.navigation_search -> {
                    replaceFragment(SearchFragment())
                    true
                }
                R.id.navigation_account -> {
                    replaceFragment(AccountFragment())
                    true
                }
                else -> false
            }
        }
        replaceFragment(MainFragment())
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.container, fragment).commit()
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        moveTaskToBack(true)
        super.onBackPressed()
    }

}