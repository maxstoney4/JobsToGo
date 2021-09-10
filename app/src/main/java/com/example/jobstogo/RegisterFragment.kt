package com.example.jobstogo

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.example.jobstogo.databinding.FragmentLoginBinding
import com.example.jobstogo.databinding.FragmentRegisterBinding
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore


class RegisterFragment : Fragment() {
    private lateinit var binding: FragmentRegisterBinding
    private lateinit var auth: FirebaseAuth;
    private lateinit var fStore: FirebaseFirestore;
    private lateinit var userID: String;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_register, container, false)


        binding.btnRegister.setOnClickListener {
            if (binding.etRegisterEmail.text.trim().toString()
                    .isNotEmpty() && binding.etRegisterPassword.text.trim().toString().isNotEmpty()
            ) {

                //Toast.makeText(this, "Input provided", Toast.LENGTH_LONG).show()
                createUser(
                    binding.etRegisterEmail.text.trim().toString(), binding.etRegisterPassword.text.trim().toString()
                )
            } else {
                Toast.makeText(requireContext(), "Input Required", Toast.LENGTH_SHORT).show()
            }



            // binding.btnStartLogin.setOnClickListener(){


        }
        return binding.root
    }

    fun createUser(email: String, password: String) {
        var array= listOf<String>(" ")

        //Create a user with these fields on the server
        auth.createUserWithEmailAndPassword(email, password)
            //Listen if the registration is successful or not
            .addOnCompleteListener() { task ->
                if (task.isSuccessful) {
                    Toast.makeText(requireContext(), "Registered Successfully", Toast.LENGTH_SHORT)
                        .show()
                    Log.e("Task Message", "Successful ...")
                    userID = auth.currentUser!!.uid //Speicher die UserID von dem User der sich gerade registriert
                    var documentReference: DocumentReference = fStore.collection("users").document(userID) //erstelle eine Tabelle für die User
                    val users: HashMap<String, Any> = HashMap<String, Any>() // Eine HashMap um die user daten zu speichern. EIne HashMap speichert die Daten in so eine Art
                                                                            //KeyPairs so dass z.b unter dem gewählten key "name" der EditText von Name darunter gespeichert wird und so ist dann der EditText Name per "name" abrufbar.
                    users.put("name", binding.etRegisterName.text.trim().toString()); //trim um leerzeichen zu entfernen.
                    users.put("email", binding.etRegisterEmail.text.trim().toString());
                    users.put("phone", binding.etRegisterPhone.text.trim().toString());
                    users.put("ratedproducts", array)

                    documentReference.set(users).addOnSuccessListener {
                        Log.d(
                            "",
                            "onSuccesss: user Profile is created for $userID"
                        )
                    }



                    findNavController().navigate(RegisterFragmentDirections.actionRegisterFragmentToLoginFragment())
                } else {
                    Log.e("Task Message", "Failed ..." + task.exception)
                    Toast.makeText(requireContext(), "ERROR: " + task.exception, Toast.LENGTH_SHORT)
                        .show()

                }
            }
        auth.signOut() // Temporäre Lösung! Weil beim Registrieren der User direkt als SignedIn gespeichert wird
        // Und dadurch wäre der Login danach Sinnlos.
    }
}
