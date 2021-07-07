package com.example.jobstogo

import android.content.ContentValues.TAG
import android.nfc.Tag
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class JobRecyclerViewAdapter(private val jobList : ArrayList<Job>): RecyclerView.Adapter<JobRecyclerViewAdapter.MyViewHolder>(){

    private lateinit var mListener: JobRecyclerViewAdapter.OnItemClickListener
    private lateinit var db: FirebaseFirestore
    //for clickevent

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): JobRecyclerViewAdapter.MyViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.recycleritem_job,parent,false)
        return MyViewHolder(itemView, mListener)
    }

    override fun onBindViewHolder(holder: JobRecyclerViewAdapter.MyViewHolder, position: Int) {
        val job : Job = jobList[position]
        db = FirebaseFirestore.getInstance()
        holder.jobname.text = job.jobname
        holder.joblocation.text = job.joblocation.toString()
        holder.deletejob.setOnClickListener(){

            db.collection("jobs").document(jobList[position].jobid)
                .delete()
                .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully deleted!") }
                .addOnFailureListener { e -> Log.w(TAG, "Error deleting document", e) }


            Log.d(TAG,jobList[position].toString())
        }

    }

    override fun getItemCount(): Int {
        return jobList.size
    }
    public class MyViewHolder(itemView: View, var mListener:OnItemClickListener): RecyclerView.ViewHolder(itemView) , View.OnClickListener{
        val jobname: TextView = itemView.findViewById(R.id.item_tv_jobname)
        val joblocation: TextView = itemView.findViewById(R.id.item_tv_location)
        val deletejob: ImageView = itemView.findViewById(R.id.btndeleteJob)


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