package com.example.jobstogo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.jobstogo.databinding.FragmentHomeBinding
import com.example.jobstogo.databinding.FragmentShopBinding

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
        return binding.root
    }

    private fun initContent(){
        content = ArrayList()
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
    }
}