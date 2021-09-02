package com.example.jobstogo

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
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
    var tagone:String = ""
    var tagtwo:String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance();

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_product, container, false)

        val tags = ArrayList<String>()
        tags.add("Elektronik")
        tags.add("Unterhaltung")
        tags.add("Sport")
        tags.add("Haushaltsgerät")
        tags.add("Bekleidung")
        tags.add("Dekoration")
        tags.add("Outdoor")
        tags.add("Kunst")
        tags.add("Verbrauchsgut")
        tags.add("Nahrung")
        tags.add("Fahrzeug")

        //for spinner: https://www.youtube.com/watch?v=g57PFNqQLNs
        //spinnerone
        val spinnerOneAdapter = ArrayAdapter(this.requireContext(),android.R.layout.simple_spinner_item,tags)
        spinnerOneAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerone.adapter = spinnerOneAdapter
        binding.spinnerone.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                tagone = parent?.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }
        //spinnertwo
        val spinnerTwoAdapter = ArrayAdapter(this.requireContext(),android.R.layout.simple_spinner_item,tags)
        spinnerTwoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnertwo.adapter = spinnerTwoAdapter
        binding.spinnertwo.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                tagtwo = parent?.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        binding.button.setOnClickListener {
            if (binding.enterProductname.text.trim().toString().isNotEmpty()
                && binding.enterProductdescription.text.trim().toString().isNotEmpty()
                && binding.enterPrice.text.trim().toString().isNotEmpty()
                && tagone!=tagtwo
            ) {

                //Toast.makeText(this, "Input provided", Toast.LENGTH_LONG).show()
                addProduct()
                Log.d(TAG, tagone+tagtwo)
            } else {
                Toast.makeText(requireContext(), "Input Required", Toast.LENGTH_SHORT).show()
                Log.d(TAG, "wrong input")
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
        products.put("tagone", tagone);
        products.put("tagtwo", tagtwo);

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