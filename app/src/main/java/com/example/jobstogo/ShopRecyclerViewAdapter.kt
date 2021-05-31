package com.example.jobstogo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ShopRecyclerViewAdapter(var content:ArrayList<Product>): RecyclerView.Adapter<ShopRecyclerViewAdapter.ViewHolder>() {

    private lateinit var mListener:OnItemClickListener      //for clickevent

    class ViewHolder(itemView: View,var mListener:OnItemClickListener): RecyclerView.ViewHolder(itemView), View.OnClickListener {      //enthält alle viewelemente die für die einträge abgerufen werden
        var tvProductname: TextView = itemView.findViewById(R.id.item_tv_productname)
        var tvPrice: TextView = itemView.findViewById(R.id.item_tv_price)

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycleritem_shop,parent,false)
        return ViewHolder(view,mListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvProductname.text = content[position].productname
        holder.tvPrice.text = content[position].price

    }

    override fun getItemCount(): Int {
        return content.size
    }

    interface OnItemClickListener{          //for clickevent
        fun setOnClickListener(pos:Int)
    }
    fun setOnItemClickListener(mlistener:OnItemClickListener){
        this.mListener = mlistener
    }
}