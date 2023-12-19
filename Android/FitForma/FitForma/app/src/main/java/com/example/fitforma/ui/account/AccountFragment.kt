package com.example.fitforma.ui.account

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.fitforma.databinding.FragmentAccountBinding
import com.example.fitforma.ui.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore

class AccountFragment : Fragment() {
    private var _binding: FragmentAccountBinding? = null
    private lateinit var firebaseAuth: FirebaseAuth
    private val db = FirebaseFirestore.getInstance()
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAccountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        firebaseAuth = FirebaseAuth.getInstance()
        val user = firebaseAuth.currentUser

        if (user != null) {
            // Get the user document reference based on the user's UID
            val userRef = db.collection("users").document(user.uid)

            // Retrieve the user's data
            userRef.get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val document: DocumentSnapshot? = task.result
                    if (document != null && document.exists()) {
                        val name = document.getString("name")
                        val email = document.getString("email")
                        val age = document.getString("age")
                        val gender = document.getString("gender")
                        val weight = document.getString("weight")
                        val height = document.getString("height")

                        binding.usernameTextView.text = name
                        binding.emailTextView.text = email

                        if (!age.isNullOrEmpty()) {
                            binding.ageTextView.visibility = View.VISIBLE
                            binding.ageTextView.text = "Age: $age"
                        }

                        if (!gender.isNullOrEmpty()) {
                            binding.genderTextView.visibility = View.VISIBLE
                            binding.genderTextView.text = "Age: $gender"
                        }

                        if (!weight.isNullOrEmpty()) {
                            binding.weightTextView.visibility = View.VISIBLE
                            binding.weightTextView.text = "Weight: $weight"
                        }

                        if (!height.isNullOrEmpty()) {
                            binding.heightTextView.visibility = View.VISIBLE
                            binding.heightTextView.text = "Height: $height"
                        }
                    }
                } else {
                    // Handle errors
                }
            }
        }

        binding.logoutBtn.setOnClickListener {
            logout()
        }
    }

    private fun logout() {
        firebaseAuth.signOut()
        val i = Intent(context, LoginActivity::class.java)
        startActivity(i)
        activity?.finish()
    }
}
