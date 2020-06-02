package com.example.rapicarmen.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class PageAdapter(fm: FragmentManager, fragment: ArrayList<Fragment>): FragmentPagerAdapter(fm) {

    private var array: ArrayList<Fragment> = fragment



    override fun getItem(position: Int): Fragment {
        return array[position]
    }

    override fun getCount(): Int {
        return array.size
    }
}