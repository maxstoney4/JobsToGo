package com.example.jobstogo

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.jobstogo.databinding.FragmentJobBinding
import com.example.jobstogo.databinding.FragmentShopDetailBinding
import com.google.firebase.firestore.FirebaseFirestore

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
    private lateinit var db: FirebaseFirestore

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<FragmentShopDetailBinding>(inflater,
            R.layout.fragment_shop_detail, container, false)

        val args = ShopDetailFragmentArgs.fromBundle(requireArguments())
        db = FirebaseFirestore.getInstance()
        //Log.d(TAG,args.productid)


        val docRef = db.collection("products").document(args.productid)
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    Log.d(TAG, "DocumentSnapshot data: ${document.data}")
                    binding.viewProductname.text= document.getString("productname")
                    binding.viewPrice.text= document.getDouble("productprice").toString()
                    binding.viewProductdescription.text= document.getString("productdescription")
                } else {
                    Log.d(TAG, "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "get failed with ", exception)
            }


        return binding.root
    }
}