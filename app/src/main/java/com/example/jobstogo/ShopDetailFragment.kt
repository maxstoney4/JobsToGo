package com.example.jobstogo

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.example.jobstogo.databinding.FragmentJobBinding
import com.example.jobstogo.databinding.FragmentShopDetailBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ShopDetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ShopDetailFragment : Fragment() {
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth;
    private lateinit var userID: String;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance();
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<FragmentShopDetailBinding>(inflater,
            R.layout.fragment_shop_detail, container, false)

        val args = ShopDetailFragmentArgs.fromBundle(requireArguments())
        db = FirebaseFirestore.getInstance()
        var userarray=arrayListOf<String>()

        //Log.d(TAG,args.productid)
        Log.d(TAG,args.recommendationone)
        Log.d(TAG,args.recommendationtwo)
        Log.d(TAG,args.recommendationthree)


        //get product document
        val docRef = db.collection("products").document(args.productid)
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    //Log.d(TAG, "DocumentSnapshot data: ${document.data}")
                    binding.viewProductname.text= document.getString("productname")
                    binding.viewPrice.text= "Preis: " + document.getDouble("productprice").toString().plus("€")
                    binding.viewProductdescription.text= document.getString("productdescription")
                } else {
                    Log.d(TAG, "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "get failed with ", exception)
            }
        //rec one
        val oneRef = db.collection("products").document(args.recommendationone)
        oneRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    //Log.d(TAG, "DocumentSnapshot data: ${document.data}")
                    binding.tvRecOne.text= document.getString("productname")
                    binding.tvRecOnePrice.text= "Preis: " + document.getDouble("productprice").toString().plus("€")
                } else {
                    Log.d(TAG, "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "get failed with ", exception)
            }
        //rec two
        val twoRef = db.collection("products").document(args.recommendationtwo)
        twoRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    //Log.d(TAG, "DocumentSnapshot data: ${document.data}")
                    binding.tvRecTwo.text= document.getString("productname")
                    binding.tvRecTwoPrice.text= "Preis: " + document.getDouble("productprice").toString().plus("€")
                } else {
                    Log.d(TAG, "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "get failed with ", exception)
            }
        //rec three
        val threeRef = db.collection("products").document(args.recommendationthree)
        threeRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    //Log.d(TAG, "DocumentSnapshot data: ${document.data}")
                    binding.tvRecThree.text= document.getString("productname")
                    binding.tvRecThreePrice.text= "Preis: " + document.getDouble("productprice").toString().plus("€")
                } else {
                    Log.d(TAG, "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "get failed with ", exception)
            }

        //if abfrage sonst fehler bei USERID
        if (auth.currentUser != null) {
            userID = auth.currentUser!!.uid
            //get user document to see if user has already rated product
            val URef = db.collection("users").document(userID)
            URef.get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        Log.d(TAG, "DocumentSnapshot data: ${document.data}")
                        userarray = document.get("ratedproducts") as ArrayList<String>

                    } else {
                        Log.d(TAG, "No such document")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d(TAG, "get failed with ", exception)
                }
        }

        /*
        binding.ratingBar.setOnRatingBarChangeListener{ratingBar,rating,fromUser->
            Log.d(TAG, rating.toString())
        }
         */
        //rating
        binding.submitbtn.setOnClickListener {
            Log.d(TAG, binding.ratingBar.rating.toString())
            Log.d(TAG, "Userarray $userarray")

            //ist user angemeldet?
            if (auth.currentUser != null) {

                //hat user schon mal gerated?
                var hasrated:Boolean=false
                for (rating in userarray){
                    if(rating==args.productid){
                        hasrated=true
                    }
                }

                if (!hasrated){
                    val rating: HashMap<String, Any> = HashMap<String, Any>()
                    rating.put("productid", args.productid)
                    rating.put("userid", userID)
                    rating.put("value", binding.ratingBar.rating.toString().toDouble());

                    db.collection("ratings").add(rating).addOnSuccessListener {
                        Log.d(TAG, "rating created")
                    }

                    var userRef = db.collection("users").document(userID)
                    userRef.update("ratedproducts",FieldValue.arrayUnion(args.productid))

                    userarray.add(args.productid)
                }

            } else {
                Toast.makeText(requireContext(), "Please Login first", Toast.LENGTH_SHORT).show()
            }
        }
        return binding.root
    }
}