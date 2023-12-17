package com.example.fitforma.ui.slider

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import androidx.viewpager.widget.ViewPager
import com.example.fitforma.R
import com.google.android.material.tabs.TabLayout

class PersonInfoActivity : AppCompatActivity() {
    private lateinit var viewPager: ViewPager
    private lateinit var tabLayout: TabLayout
    private lateinit var btnPrevious: ImageView
    private lateinit var btnNext: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_person_info)

        viewPager = findViewById(R.id.viewPager)
        tabLayout = findViewById(R.id.tabLayout)
        btnPrevious = findViewById(R.id.btnPrevious)
        btnNext = findViewById(R.id.btnNext)

        val adapter = SliderPagerAdapter(supportFragmentManager)
        viewPager.adapter = adapter
        tabLayout.setupWithViewPager(viewPager)

        btnPrevious.setOnClickListener {
            viewPager.currentItem = viewPager.currentItem - 1
        }

        btnNext.setOnClickListener {
            viewPager.currentItem = viewPager.currentItem + 1
        }

        // Add a listener to enable/disable buttons based on the current page
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int, positionOffset: Float, positionOffsetPixels: Int
            ) {
                // Not needed for this example
            }

            override fun onPageSelected(position: Int) {
                updateButtonVisibility(position)
            }

            override fun onPageScrollStateChanged(state: Int) {
                // Not needed for this example
            }
        })

        // Set initial button visibility
        updateButtonVisibility(0)

        // Customize tabs with custom views
        for (i in 0 until tabLayout.tabCount) {
            val customView = LayoutInflater.from(this).inflate(R.layout.custom_tab, null)
            val dot = customView.findViewById<ImageView>(R.id.tabDot)
            tabLayout.getTabAt(i)?.customView = customView
        }

        // Set up initial dot visibility
        updateDotVisibility(0)
    }

    private fun updateButtonVisibility(currentPage: Int) {
        btnPrevious.visibility = if (currentPage == 0) View.INVISIBLE else View.VISIBLE
        btnNext.visibility = if (currentPage == 3) View.INVISIBLE else View.VISIBLE
    }

    private fun updateDotVisibility(currentPage: Int) {
        for (i in 0 until tabLayout.tabCount) {
            val dot = tabLayout.getTabAt(i)?.customView?.findViewById<ImageView>(R.id.tabDot)
            dot?.isSelected = (i == currentPage)
        }
    }
}