package com.stardust.auojs.inrt.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.stardust.auojs.inrt.ui.home.HomeFragment
import com.stardust.auojs.inrt.ui.mine.MineFragment
import com.stardust.auojs.inrt.ui.praise.PraiseFragment
import com.stardust.auojs.inrt.ui.spread.SpreadFragment

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
        fragmentList.add(SpreadFragment())
        fragmentList.add(MineFragment())
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