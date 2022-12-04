package com.educards.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class FragmentAdapter(var fm: FragmentManager, var framents: List<Fragment>, var titles: List<String>): FragmentStatePagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return framents[position]
    }

    override fun getCount(): Int {
        return framents.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return titles!![position]
    }

}