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
import com.example.jobstogo.databinding.FragmentAddProductBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ShopDetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddProductFragment : Fragment() {
    private lateinit var binding: FragmentAddProductBinding
    private lateinit var auth: FirebaseAuth;
    private lateinit var userID: String;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance();

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_product, container, false)


        binding.button.setOnClickListener {
            if (binding.enterProductname.text.trim().toString().isNotEmpty()
                && binding.enterProductdescription.text.trim().toString().isNotEmpty()
                && binding.enterPrice.text.trim().toString().isNotEmpty()
            ) {

                //Toast.makeText(this, "Input provided", Toast.LENGTH_LONG).show()
                addProduct()
            } else {
                Toast.makeText(requireContext(), "Input Required", Toast.LENGTH_SHORT).show()
            }
        }

            return binding.root
        }

    fun addProduct(){

        userID = auth.currentUser!!.uid //Speicher die UserID von dem User der sich gerade registriert

        val db = Firebase.firestore

        val products: HashMap<String, Any> = HashMap<String, Any>() // Eine HashMap um die user daten zu speichern. EIne HashMap speichert die Daten in so eine Art
        //KeyPairs so dass z.b unter dem gewählten key "name" der EditText von Name darunter gespeichert wird und so ist dann der EditText Name per "name" abrufbar.
        products.put("productname", binding.enterProductname.text.trim().toString()); //trim um leerzeichen zu entfernen.
        products.put("productdescription", binding.enterProductdescription.text.trim().toString());
        products.put("productprice", binding.enterPrice.text.trim().toString().toDouble());
        products.put("vendorid", userID);

        db.collection("products").add(products).addOnSuccessListener {
            Log.d(
                "",
                "onSuccesss: user Profile is created for $userID"
            )

            binding.button.setOnClickListener{
                findNavController().navigate(AddProductFragmentDirections.actionAddProductFragmentToHomeFragment())
            }

        }
    }
}