package com.example.jobstogo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.jobstogo.databinding.FragmentHomeBinding
import com.example.jobstogo.databinding.FragmentJobBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [JobFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class JobFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<FragmentJobBinding>(inflater,
            R.layout.fragment_job, container, false)



        return binding.root
    }
}