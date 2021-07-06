package com.example.jobstogo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ShopRecyclerViewAdapter(private val productList : ArrayList<Product>): RecyclerView.Adapter<ShopRecyclerViewAdapter.MyViewHolder>(){

    private lateinit var mListener: ShopRecyclerViewAdapter.OnItemClickListener      //for clickevent

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ShopRecyclerViewAdapter.MyViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.recycleritem_shop,parent,false)
        return MyViewHolder(itemView,mListener)
    }

    override fun onBindViewHolder(holder: ShopRecyclerViewAdapter.MyViewHolder, position: Int) {
        val product : Product = productList[position]
        holder.productName.text = product.productname
        holder.productPrice.text = product.productprice.toString()
    }

    override fun getItemCount(): Int {
        return productList.size
    }
    class MyViewHolder(itemView: View,var mListener:OnItemClickListener): RecyclerView.ViewHolder(itemView), View.OnClickListener{
        val productName: TextView = itemView.findViewById(R.id.item_tv_productname)
        val productPrice: TextView = itemView.findViewById(R.id.item_tv_price)

        //for clickevent
        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            if(mListener != null){
                mListener.setOnClickListener(adapterPosition)
            }
        }

    }
    interface OnItemClickListener{          //for clickevent
        fun setOnClickListener(pos:Int)
    }
    fun setOnItemClickListener(mlistener:OnItemClickListener){
        this.mListener = mlistener
    }
}