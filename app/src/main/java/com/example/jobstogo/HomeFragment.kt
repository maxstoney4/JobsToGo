package com.example.jobstogo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.jobstogo.databinding.FragmentHomeBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator



/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<FragmentHomeBinding>(inflater,
            R.layout.fragment_home, container, false)

        var viewPager: ViewPager2 = binding.viewpager
        var adapter = ExampleStateAdapter(childFragmentManager,lifecycle)
        viewPager.adapter = adapter

        var tabLayout: TabLayout = binding.tablayout
        var names:ArrayList<String> = arrayListOf("Productshop", "Jobs")
        TabLayoutMediator(tabLayout,viewPager){tab,position -> tab.text= names[position]}.attach()


        return binding.root
    }

}