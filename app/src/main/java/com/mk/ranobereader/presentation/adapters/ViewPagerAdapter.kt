package com.mk.ranobereader.presentation.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.mk.ranobereader.presentation.ranobeInfoScreen.fragment.ChaptersListFragment
import com.mk.ranobereader.presentation.ranobeInfoScreen.fragment.RanobeInformationFragment

private const val NUM_TABS = 2

public class ViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int {
        return NUM_TABS
    }

    override fun createFragment(position: Int): Fragment {
        when (position) {
            0 -> RanobeInformationFragment()
            1 -> return ChaptersListFragment()
        }
        return RanobeInformationFragment()
    }
}
