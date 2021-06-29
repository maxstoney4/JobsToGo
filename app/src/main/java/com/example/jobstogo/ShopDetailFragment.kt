package com.example.jobstogo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.jobstogo.databinding.FragmentJobBinding
import com.example.jobstogo.databinding.FragmentShopDetailBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ShopDetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ShopDetailFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<FragmentShopDetailBinding>(inflater,
            R.layout.fragment_shop_detail, container, false)



        return binding.root
    }
}