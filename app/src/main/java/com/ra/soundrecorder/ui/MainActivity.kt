package com.ra.soundrecorder.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.tabs.TabLayoutMediator
import com.ra.soundrecorder.R
import com.ra.soundrecorder.adapter.SectionPagerAdapter
import com.ra.soundrecorder.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupViewPager()
    }

    private fun setupViewPager() = with(binding) {
        val sectionAdapter = SectionPagerAdapter(this@MainActivity)
        viewPager.adapter = sectionAdapter
        TabLayoutMediator(tabLayout, viewPager) { tab , position ->
            tab.text = resources.getString(TAB_TITLE[position])
        }.attach()
    }

    companion object {
        private val TAB_TITLE = intArrayOf(R.string.recorder_tab, R.string.saved_file_tab)
    }
}