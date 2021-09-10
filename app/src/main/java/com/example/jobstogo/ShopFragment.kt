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
    private lateinit var ratingArrayList: ArrayList<Rating>
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
        ratingArrayList = arrayListOf()

        myAdapter = ShopRecyclerViewAdapter(productArrayList)
        recyclerView.adapter = myAdapter

        //get ratings into ratingArrayList
        db = FirebaseFirestore.getInstance()
        db.collection("ratings").get().addOnSuccessListener { documents ->
            for (document in documents) {
                //Log.d(TAG,"${document.id} => ${document.data}")
                var rating = Rating(document.getString("productid"),document.getString("userid"),
                    document.get("value") as Double?)
                ratingArrayList.add(rating)
            }
        }
            .addOnFailureListener{ exception ->
                Log.w(TAG,"Error getting documents: ", exception)
            }
        //for lines in between
        recyclerView.addItemDecoration(DividerItemDecoration(recyclerView.context,
            (recyclerView.layoutManager as LinearLayoutManager).orientation))

        //for clickevent
        myAdapter.setOnItemClickListener(object: ShopRecyclerViewAdapter.OnItemClickListener{
            override fun setOnClickListener(pos: Int) {

                //Log.d(TAG, productArrayList[pos].toString())
                //Log.d(TAG, productArrayList.toString())

                // calculate recommendations here and give productid´s as parameters to shopdetailfragment
                //Log.d(TAG, ratingArrayList.toString())

                //use list with productid and score
                var scoreArray = mutableListOf<score>()
                var cleanRatingArray = mutableListOf<ratingcleaner>()
                var average:Double=0.0

                // check tags in productArrayList and fill scoreArray
                for(product in productArrayList){
                    var score:Double=0.0
                    if(product.tagone==productArrayList[pos].tagone||product.tagtwo==productArrayList[pos].tagone){
                        score=score+5
                    }
                    if(product.tagone==productArrayList[pos].tagtwo||product.tagtwo==productArrayList[pos].tagtwo){
                        score=score+5
                    }
                    if(product.productid!=productArrayList[pos].productid){
                        var temp = score(product.productid,score)
                        scoreArray.add(temp)
                    }
                }
                //Log.d(TAG, scoreArray.toString())
                // check ratings and number of ratings
                for(rating in ratingArrayList){
                    if (cleanRatingArray.isEmpty()){                                              //first item added
                        var temp = ratingcleaner(rating.productid,rating.value,1)
                        cleanRatingArray.add(temp)

                    } else {                                                                       //list not empty
                        var containedInList:Boolean=false
                        with(cleanRatingArray.listIterator()){
                        forEach{
                            if(it.productid==rating.productid){                 //list contrains product
                                it.appearances= it.appearances?.plus(1)
                                it.score = it.score?.plus(rating.value!!)
                                containedInList=true
                            }
                        }
                    }
                        if(!containedInList){                                       //list does not contain product
                            var temptwo = ratingcleaner(rating.productid,rating.value,1)
                            cleanRatingArray.add(temptwo)
                        }
                        containedInList=false
                    }
                }
                // calculate average amount of ratings
                var count=0
                for(rating in cleanRatingArray){
                    average+= rating.appearances!!
                    count+=1
                }
                average /= count
                //Log.d(TAG,average.toString())

                //get k to ajdust appearances
                var k=(0.5-0.25)/average

                // insert cleanRatingArray ratings into scoreArray
                for(tagscore in scoreArray){
                    for(ratescore in cleanRatingArray){
                        if(tagscore.productid==ratescore.productid){
                            var y=k* ratescore.appearances!! +0.25
                            if(y>1){
                                y= 1.0
                            }
                            //Log.d(TAG,"rating = ("+ratescore.score+"/"+ratescore.appearances+")*"+y)
                            tagscore.score = tagscore.score?.plus((ratescore.score!! / ratescore.appearances!!)*y)
                        }
                    }
                }
               // sort scoreArray
                scoreArray.sortByDescending { it.score }

                Log.d(TAG,"+++++++++++++++++++")
                for (score in scoreArray){
                    Log.d(TAG,score.toString())
                }
                Log.d(TAG,"+++++++++++++++++++")

                findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToShopDetailFragment(productArrayList[pos].productid,scoreArray[0].productid.toString(),scoreArray[1].productid.toString(),scoreArray[2].productid.toString()))
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
                            dc.document.get("productprice") as Double?,dc.document.getString("productdescription"),
                            dc.document.getString("tagone"),dc.document.getString("tagtwo")
                        )
                        productArrayList.add(product)
                    }
                    if(dc.type == DocumentChange.Type.REMOVED){
                        var product = Product(dc.document.id,dc.document.getString("vendorid"),dc.document.getString("productname"),
                            dc.document.get("productprice") as Double?,dc.document.getString("productdescription"),
                            dc.document.getString("tagone"),dc.document.getString("tagtwo")
                        )
                        productArrayList.remove(product)

                    }
                }
                myAdapter.notifyDataSetChanged()
            }
        })
    }


}
