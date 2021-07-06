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
import com.google.firebase.firestore.DocumentSnapshot
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

    private lateinit var rv:RecyclerView
    private lateinit var layoutManager:LinearLayoutManager
    private lateinit var adapter:ShopRecyclerViewAdapter

    //content:
    private lateinit var content:ArrayList<Product>
    val db = Firebase.firestore
    val placeRef = db.collection("products")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<FragmentShopBinding>(inflater,
            R.layout.fragment_shop, container, false)

        initContent()
        //initrecyclerview
        rv = binding.rv
        layoutManager = LinearLayoutManager(this.context,RecyclerView.VERTICAL,false)
        adapter = ShopRecyclerViewAdapter(content)
        rv.layoutManager = layoutManager
        rv.adapter = adapter

        rv.addItemDecoration(DividerItemDecoration(rv.context,layoutManager.orientation))

        //for clickevent
        adapter.setOnItemClickListener(object: ShopRecyclerViewAdapter.OnItemClickListener{
            override fun setOnClickListener(pos: Int) {
                //to do
                findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToShopDetailFragment())


            }
        })
        binding.btn.setOnClickListener{
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToAddProductFragment())
        }
        return binding.root
    }

    private fun initContent(){
        content = ArrayList()

        var test: String
        db.collection("products")
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        Log.d(TAG, "${document.id} => ${document.data}")

                        content.add(Product(document.id,"test","50$"))
                        test=document.id
                        Log.d(TAG, test)
                        //Log.d(TAG, " das ist ein test log um zu zeigen das diese zeile hier noch funktioniert")
                    }
                    Log.d(TAG, content.toString())
                }
                .addOnFailureListener { exception ->
                    Log.w(TAG, "Error getting documents.", exception)
                }

        /*
        content.add(Product(content.size+1,"ProductA","50 $"))
        content.add(Product(content.size+1,"ProductB","40 $"))
        content.add(Product(content.size+1,"ProductC","30 $"))
        content.add(Product(content.size+1,"ProductD","20 $"))
        content.add(Product(content.size+1,"ProductE","10 $"))
        content.add(Product(content.size+1,"ProductF","60 $"))
        content.add(Product(content.size+1,"ProductG","70 $"))
        content.add(Product(content.size+1,"ProductH","80 $"))
        content.add(Product(content.size+1,"ProductI","90 $"))
        content.add(Product(content.size+1,"ProductJ","100 $"))
        content.add(Product(content.size+1,"ProductK","145 $"))
         */
    }
}