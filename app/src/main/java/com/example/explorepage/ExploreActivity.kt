package com.example.explorepage

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.example.explorepage.aFragments.PagerAdapter
import com.google.android.material.tabs.TabLayout

class ExploreActivity : AppCompatActivity() {
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager2
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_explore)

        tabLayout = findViewById(R.id.tabLayout)
        viewPager = findViewById(R.id.ViewPager)

        val pagerAdapter = PagerAdapter(supportFragmentManager, lifecycle)
        viewPager.adapter = pagerAdapter
        tabLayout.addTab(tabLayout.newTab().setText(R.string.shops))
        tabLayout.addTab(tabLayout.newTab().setText(R.string.dishes))

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                viewPager.currentItem = tab!!.position

    }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                tabLayout.selectTab(tabLayout.getTabAt(position))
            }
        })
    }
}