package com.example.jobstogo

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.jobstogo.databinding.FragmentShopBinding
import com.google.firebase.auth.FirebaseAuth
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
    //https://www.youtube.com/watch?v=Ly0xwWlUpVM <-- Video: Firebase to RecyclerView

    private lateinit var recyclerView: RecyclerView
    private lateinit var productArrayList: ArrayList<Product>
    private lateinit var myAdapter: ShopRecyclerViewAdapter
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth;


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

        //for lines in between
        recyclerView.addItemDecoration(DividerItemDecoration(recyclerView.context,
            (recyclerView.layoutManager as LinearLayoutManager).orientation))

        //for clickevent
        myAdapter.setOnItemClickListener(object: ShopRecyclerViewAdapter.OnItemClickListener{
            override fun setOnClickListener(pos: Int) {

                //Log.d(TAG, productArrayList[pos].toString())

                findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToShopDetailFragment(productArrayList[pos].productid))
            }
        })

        //for firestore
        EventChangeListener()

        //floating action button
        binding.btn.setOnClickListener{

            auth = FirebaseAuth.getInstance();
            if (auth.currentUser != null) {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToAddProductFragment())
        } else { Toast.makeText(requireContext(), "Please Login first", Toast.LENGTH_SHORT).show()
        }
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


                        var product = Product(dc.document.id,dc.document.getString("vendorid"),dc.document.getString("productname"),
                            dc.document.get("productprice") as Double?,dc.document.getString("productdescription")
                        )
                        productArrayList.add(product)
                    }
                    if(dc.type == DocumentChange.Type.REMOVED){
                        var product = Product(dc.document.id,dc.document.getString("vendorid"),dc.document.getString("productname"),
                            dc.document.get("productprice") as Double?,dc.document.getString("productdescription")
                        )
                        productArrayList.remove(product)

                    }
                }
                myAdapter.notifyDataSetChanged()
            }
        })
    }


}
