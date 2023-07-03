package com.kinopio.eatgo.presentation.store

import StoreMenuFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.kinopio.eatgo.domain.store.Menu

class StoreDetailTabAdapter(fragment: FragmentActivity, private val menuList: List<Menu>) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 3
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> StoreMenuFragment.newInstance(menuList)
            1 -> StoreInfoFragment()
            else -> StoreReviewFragment()
        }
    }
}