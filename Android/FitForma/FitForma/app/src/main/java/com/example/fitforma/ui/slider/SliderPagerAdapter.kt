package com.example.fitforma.ui.slider

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.fitforma.ui.slider.fragment.SliderFragment1
import com.example.fitforma.ui.slider.fragment.SliderFragment2
import com.example.fitforma.ui.slider.fragment.SliderFragment3
import com.example.fitforma.ui.slider.fragment.SliderFragment4

class SliderPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> SliderFragment1()
            1 -> SliderFragment2()
            2 -> SliderFragment3()
            3 -> SliderFragment4()
            else -> throw IllegalArgumentException("Invalid position: $position")
        }
    }

    override fun getCount(): Int {
        return 4 // Number of fragments
    }
}