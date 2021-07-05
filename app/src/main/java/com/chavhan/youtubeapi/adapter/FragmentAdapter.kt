package com.chavhan.youtubeapi.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.chavhan.youtubeapi.fragments.FavouriteFragment
import com.chavhan.youtubeapi.fragments.HomeFragment
import com.chavhan.youtubeapi.fragments.PlaylistFragment

open class FragmentAdapter(fm:FragmentManager, lc: Lifecycle): FragmentStateAdapter(fm,lc) {
    override fun createFragment(position: Int): Fragment {

        return when(position){
            1->{
                PlaylistFragment()
            }
            2->{
                FavouriteFragment()
            }
            else ->{
                HomeFragment()
            }
        }
    }

    override fun getItemCount(): Int {
       return 3
    }
}