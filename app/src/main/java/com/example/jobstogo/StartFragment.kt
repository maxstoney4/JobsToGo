package com.example.jobstogo

import android.R.attr.button
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.navigation.fragment.findNavController
import com.example.jobstogo.databinding.FragmentStartBinding


class StartFragment : Fragment() {
    private lateinit var binding: FragmentStartBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //val view = inflater.inflate(R.layout.fragment_start, container, false)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_start, container, false)

        binding.btnStartLogin.setOnClickListener {
            findNavController().navigate(StartFragmentDirections.actionStartFragmentToLoginFragment())
        }
        binding.btnStartRegister.setOnClickListener {
            findNavController().navigate(StartFragmentDirections.actionStartFragmentToRegisterFragment())
        }
        return binding.root
       // binding.btnStartLogin.setOnClickListener(){



        }

    }