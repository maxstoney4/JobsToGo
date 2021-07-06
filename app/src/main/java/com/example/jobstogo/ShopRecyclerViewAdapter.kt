package com.example.jobstogo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ShopRecyclerViewAdapter(private val productList : ArrayList<Product>): RecyclerView.Adapter<ShopRecyclerViewAdapter.MyViewHolder>(){

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ShopRecyclerViewAdapter.MyViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.recycleritem_shop,parent,false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ShopRecyclerViewAdapter.MyViewHolder, position: Int) {
        val product : Product = productList[position]
        holder.productName.text = product.productname
        holder.productPrice.text = product.productprice.toString()
    }

    override fun getItemCount(): Int {
        return productList.size
    }
    public class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val productName: TextView = itemView.findViewById(R.id.item_tv_productname)
        val productPrice: TextView = itemView.findViewById(R.id.item_tv_price)

    }
}