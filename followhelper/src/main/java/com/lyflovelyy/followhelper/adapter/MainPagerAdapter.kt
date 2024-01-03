package com.lyflovelyy.followhelper.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.lyflovelyy.followhelper.fragment.HomeFragment
import com.lyflovelyy.followhelper.fragment.PraiseFragment

/**
 * @Author      : liuyufei
 * @Date        : on 2023-08-29 06:55.
 * @Description :
 */
class MainPagerAdapter : FragmentStateAdapter {
    private val fragmentList: MutableList<Fragment> = ArrayList()

    constructor(fragmentActivity: FragmentActivity) : super(fragmentActivity) {
        fragmentList.add(HomeFragment())
        fragmentList.add(PraiseFragment())
    }

    fun getCurrFragment(position: Int): Fragment {
        return fragmentList[position]
    }

    override fun getItemCount(): Int {
        return fragmentList.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
    }

}