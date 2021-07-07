package com.example.jobstogo

import android.os.Bundle
import android.util.Log
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
import com.example.jobstogo.databinding.FragmentJobBinding
import com.google.firebase.firestore.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

private lateinit var recyclerView: RecyclerView
private lateinit var jobArrayList: ArrayList<Job>
private lateinit var myAdapter: JobRecyclerViewAdapter
private lateinit var db: FirebaseFirestore

class JobFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<FragmentJobBinding>(inflater,
            R.layout.fragment_job, container, false)


        recyclerView = binding.recv
        recyclerView.layoutManager = LinearLayoutManager(this.context)
        recyclerView.setHasFixedSize(true)

        jobArrayList = arrayListOf()

        myAdapter = JobRecyclerViewAdapter(jobArrayList)
        recyclerView.adapter = myAdapter

        EventChangeListener()

        //for lines in between
        recyclerView.addItemDecoration(
            DividerItemDecoration(recyclerView.context,
            (recyclerView.layoutManager as LinearLayoutManager).orientation)
        )


        //for clickevent
        myAdapter.setOnItemClickListener(object: JobRecyclerViewAdapter.OnItemClickListener{
            override fun setOnClickListener(pos: Int) {

                findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToJobDetailFragment(jobArrayList[pos].jobid))


            }
        })


        binding.btn2.setOnClickListener{
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToAddJobsFragment())
        }

        return binding.root
    }
    private fun EventChangeListener(){
        db = FirebaseFirestore.getInstance()
        db.collection("jobs").addSnapshotListener(object: EventListener<QuerySnapshot> {
            override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                if (error != null){
                    Log.e("Firestore Error",error.message.toString())
                    return
                }
                for (dc: DocumentChange in value?.documentChanges!!){
                    if (dc.type == DocumentChange.Type.ADDED){
                        var job = Job(dc.document.id,dc.document.getString("vendorid"),dc.document.getString("jobname"),
                            dc.document.getString("joblocation"),dc.document.getString("jobdescription")
                        )
                        jobArrayList.add(job)
                    }
                    if(dc.type == DocumentChange.Type.REMOVED){
                        var job = Job(dc.document.id,dc.document.getString("vendorid"),dc.document.getString("jobname"),
                            dc.document.getString("joblocation"),dc.document.getString("jobdescription")
                        )
                        jobArrayList.remove(job)

                    }
                }
                myAdapter.notifyDataSetChanged()

            }
        })
    }


}