package com.example.jobstogo

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.jobstogo.databinding.FragmentShopBinding
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ShopFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ShopFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var productArrayList: ArrayList<Product>
    private lateinit var myAdapter: ShopRecyclerViewAdapter
    private lateinit var db: FirebaseFirestore

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<FragmentShopBinding>(inflater,
            R.layout.fragment_shop, container, false)

        recyclerView = binding.rv
        recyclerView.layoutManager = LinearLayoutManager(this.context)
        recyclerView.setHasFixedSize(true)

        productArrayList = arrayListOf()

        myAdapter = ShopRecyclerViewAdapter(productArrayList)
        recyclerView.adapter = myAdapter

        EventChangeListener()

        binding.btn.setOnClickListener{
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToAddProductFragment())
        }
        return binding.root
    }
    private fun EventChangeListener(){
        db = FirebaseFirestore.getInstance()
        db.collection("products").addSnapshotListener(object: EventListener<QuerySnapshot>{
            override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
               if (error != null){
                    Log.e("Firestore Error",error.message.toString())
                   return
                   }
                for (dc:DocumentChange in value?.documentChanges!!){
                    if (dc.type == DocumentChange.Type.ADDED){
                        productArrayList.add(dc.document.toObject(Product::class.java))

                    }
                }
                myAdapter.notifyDataSetChanged()
            }
        })
    }


}
