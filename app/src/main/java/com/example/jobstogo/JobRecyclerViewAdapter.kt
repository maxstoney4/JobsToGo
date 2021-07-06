package com.example.jobstogo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class JobRecyclerViewAdapter(private val jobList : ArrayList<Job>): RecyclerView.Adapter<JobRecyclerViewAdapter.MyViewHolder>(){

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): JobRecyclerViewAdapter.MyViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.recycleritem_job,parent,false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: JobRecyclerViewAdapter.MyViewHolder, position: Int) {
        val job : Job = jobList[position]
        holder.jobname.text = job.jobname
        holder.joblocation.text = job.joblocation.toString()
    }

    override fun getItemCount(): Int {
        return jobList.size
    }
    public class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val jobname: TextView = itemView.findViewById(R.id.item_tv_jobname)
        val joblocation: TextView = itemView.findViewById(R.id.item_tv_location)

    }
}