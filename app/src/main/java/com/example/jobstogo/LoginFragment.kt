package com.example.jobstogo

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.example.jobstogo.databinding.FragmentLoginBinding
import com.example.jobstogo.databinding.FragmentStartBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private var counter = 5
    var test: String? = null
    private lateinit var auth: FirebaseAuth;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance();


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)


       binding.btnLogin.setOnClickListener {
            if (binding.etEmailAddress.text.trim().toString().isNotEmpty() && binding.etPassword.text.trim().toString().isNotEmpty()) {
                signInUser(binding.etEmailAddress.text.trim().toString(), binding.etPassword.text.trim().toString())
            } else {
                Toast.makeText(requireContext(), "Input Required", Toast.LENGTH_SHORT).show()

            }
        }

        return binding.root
        // binding.btnStartLogin.setOnClickListener(){


    }
    fun signInUser(email:String, password: String) {

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToHomeFragment())

                } else {
                    Toast.makeText(requireContext(), "Error" + task.exception, Toast.LENGTH_SHORT).show()
                    counter--
                    binding.tvinfo.text = "No of attempts remaining $counter"
                    if (counter == 0) {
                        binding.btnLogin.isEnabled = false
                    }
                }
            }

    }

}
