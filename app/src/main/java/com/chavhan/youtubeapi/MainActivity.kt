package com.chavhan.youtubeapi

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.chavhan.youtubeapi.adapter.FragmentAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val tabLyout = findViewById<TabLayout>(R.id.tablet_layout)
        val pager = findViewById<ViewPager2>(R.id.viewPager)



        val adapter = FragmentAdapter(supportFragmentManager, lifecycle )
        pager.adapter = adapter

        tabLyout.addTab(tabLyout.newTab().setText("Home"))
        tabLyout.addTab(tabLyout.newTab().setText("Watched"))
        tabLyout.addTab(tabLyout.newTab().setText("Favourite"))

        tabLyout.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                pager.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })

        pager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                tabLyout.selectTab(tabLyout.getTabAt(position))
            }
        })


    }
}