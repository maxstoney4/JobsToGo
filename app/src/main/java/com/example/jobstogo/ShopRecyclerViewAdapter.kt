package com.example.jobstogo

import android.content.ContentValues
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class ShopRecyclerViewAdapter(private val productList : ArrayList<Product>): RecyclerView.Adapter<ShopRecyclerViewAdapter.MyViewHolder>(){

    private lateinit var mListener: ShopRecyclerViewAdapter.OnItemClickListener      //for clickevent
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth;

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ShopRecyclerViewAdapter.MyViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.recycleritem_shop,parent,false)
        return MyViewHolder(itemView,mListener)
    }

    override fun onBindViewHolder(holder: ShopRecyclerViewAdapter.MyViewHolder, position: Int) {
        val product : Product = productList[position]
        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance();
        holder.productName.text = product.productname
        holder.productPrice.text = product.productprice.toString()
        holder.deleteproduct.setOnClickListener() {

            if (auth.currentUser?.uid.toString() == productList[position].vendorid.toString()) {
                db.collection("jobs").document(productList[position].productid)
                    .delete()
                    .addOnSuccessListener { Log.d(ContentValues.TAG, "DocumentSnapshot successfully deleted!") }
                    .addOnFailureListener { e -> Log.w(ContentValues.TAG, "Error deleting document", e) }


                Log.d(ContentValues.TAG, productList[position].vendorid.toString())
                Log.d(ContentValues.TAG, auth.currentUser?.uid.toString())
            }else{
                Toast.makeText(holder.itemView.context, "USER NOT AUTHORIZED", Toast.LENGTH_SHORT).show()
                Log.d(ContentValues.TAG, productList[position].vendorid.toString())
                Log.d(ContentValues.TAG, auth.currentUser?.uid.toString())
            }

        }
    }

    override fun getItemCount(): Int {
        return productList.size
    }
    class MyViewHolder(itemView: View,var mListener:OnItemClickListener): RecyclerView.ViewHolder(itemView), View.OnClickListener{
        val productName: TextView = itemView.findViewById(R.id.item_tv_productname)
        val productPrice: TextView = itemView.findViewById(R.id.item_tv_price)
        val deleteproduct: ImageView = itemView.findViewById(R.id.btndeleteProduct)


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