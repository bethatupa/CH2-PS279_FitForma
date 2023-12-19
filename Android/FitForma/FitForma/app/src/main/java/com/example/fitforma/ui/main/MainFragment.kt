package com.example.fitforma.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.fitforma.databinding.FragmentMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore

class MainFragment : Fragment() {
    private var _binding: FragmentMainBinding? = null
    private lateinit var firebaseAuth: FirebaseAuth
    private val db = FirebaseFirestore.getInstance()
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
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
                        val age = document.getString("age")
                        val gender = document.getString("gender")
                        val weight = document.getString("weight")
                        val height = document.getString("height")

                        binding.tvName.text = name

                        if (!age.isNullOrEmpty()) {
                            binding.tvBmi.visibility = View.VISIBLE
                            binding.tvBmi.text = "$age"
                        }

                        if (!gender.isNullOrEmpty()) {
                            binding.tvBmr.visibility = View.VISIBLE
                            binding.tvBmr.text = "$gender"
                        }
                    }
                } else {
                    // Handle errors
                }
            }
        }
    }

    private fun calculateBMIValue(weight: Double, height: Double): Double {
        // BMI formula: weight (kg) / (height (m))^2
        val heightInMeters = height / 100.0
        return weight / (heightInMeters * heightInMeters)
    }

    private fun determineBMICategory(bmi: Double): String {
        return when {
            bmi < 18.5 -> "Underweight"
            bmi < 24.9 -> "Normal Weight"
            bmi < 29.9 -> "Overweight"
            else -> "Obesity"
        }
    }

    private fun getWeightAdvice(category: String): String {
        return when (category) {
            "Underweight" -> "You may need to gain weight. Consult with a nutritionist or doctor for a safe weight gain plan."
            "Normal Weight" -> "Your weight is in the normal range. Maintain a healthy diet and regular physical activity."
            "Overweight" -> "You may need to reduce weight. Consult with a nutritionist or doctor for a safe weight loss plan."
            else -> "You may need special attention. Consult with a doctor or nutritionist."
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
