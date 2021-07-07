package com.example.jobstogo

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.jobstogo.databinding.FragmentJobDetailBinding
import com.example.jobstogo.databinding.FragmentShopDetailBinding
import com.google.firebase.firestore.FirebaseFirestore


private lateinit var db: FirebaseFirestore

class JobDetailFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<FragmentJobDetailBinding>(inflater,
            R.layout.fragment_job_detail, container, false)


        val args = JobDetailFragmentArgs.fromBundle(requireArguments())
        db = FirebaseFirestore.getInstance()
        //Log.d(TAG,args.productid)


        val docRef = db.collection("jobs").document(args.jobid)
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    Log.d(ContentValues.TAG, "DocumentSnapshot data: ${document.data}")
                    binding.viewJobname.text= document.getString("jobname")
                    binding.viewLocation.text= "Ort: " + document.getString("joblocation")
                    binding.viewJobdescription.text= document.getString("jobdescription")
                } else {
                    Log.d(ContentValues.TAG, "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d(ContentValues.TAG, "get failed with ", exception)
            }



        return binding.root
    }
}