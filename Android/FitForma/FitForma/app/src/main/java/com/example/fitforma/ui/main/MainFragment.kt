package com.example.fitforma.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.fitforma.R
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
                        val age = document.getLong("age")
                        val gender = document.getString("gender")
                        val weight = document.getDouble("weight")
                        val height = document.getDouble("height")

                        // Display user's name
                        binding.tvName.text = name

                        // Check if weight and height are available
                        if (weight != null && height != null) {
                            // Calculate BMI and BMR
                            val bmi = calculateBMIValue(weight, height)
                            val bmr = calculateBMR(weight, height, age, gender)

                            // Display BMI and BMR in the UI
                            if(bmi < 18.5) {
                                binding.tvPerson.visibility = View.VISIBLE

                            }
                            binding.tvBmi.visibility = View.VISIBLE
                            binding.tvBmi.text = String.format("BMI: %.2f", bmi)

                            binding.tvBmr.visibility = View.VISIBLE
                            binding.tvBmr.text = String.format("BMR: %.2f", bmr)

                            val bmiCategory = determineBMICategory(bmi)
                            setBmiImage(bmiCategory)
                            setAdvice(bmiCategory)
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

    private fun calculateBMR(weight: Double, height: Double, age: Long?, gender: String?): Double {
        // BMR formula:
        // For men: BMR = 88.362 + (13.397 * weight in kg) + (4.799 * height in cm) - (5.677 * age in years)
        // For women: BMR = 447.593 + (9.247 * weight in kg) + (3.098 * height in cm) - (4.330 * age in years)

        if (age != null && gender != null) {
            val weightInKg = weight
            val heightInCm = height

            return when (gender) {
                "male" -> 88.362 + (13.397 * weightInKg) + (4.799 * heightInCm) - (5.677 * age)
                "female" -> 447.593 + (9.247 * weightInKg) + (3.098 * heightInCm) - (4.330 * age)
                else -> 0.0 // Handle other genders as needed
            }
        }
        return 0.0
    }

    private fun determineBMICategory(bmi: Double): String {
        return when {
            bmi < 18.5 -> "Underweight"
            bmi < 24.9 -> "Normal Weight"
            bmi < 29.9 -> "Overweight"
            else -> "Obesity"
        }
    }

    private fun setBmiImage(bmiCategory: String) {
        // Set the appropriate image based on the BMI category
        val imageResource = when (bmiCategory) {
            "Underweight" -> {
                R.drawable.person_skinny_black
            }

            "Normal Weight" -> {
                R.drawable.person_normal_black
            }

            "Overweight" -> {
                R.drawable.person_fat_black
            }

            else -> R.drawable.person_fat_black
        }

        binding.tvPerson.setImageResource(imageResource as Int)
    }

    private fun setAdvice(bmiCategory: String) {
        // Set the advice based on the BMI category
        val adviceText = when (bmiCategory) {
            "Underweight" -> "You may need to gain weight. Consult with a nutritionist or doctor for a safe weight gain plan."
            "Normal Weight" -> "Your weight is in the normal range. Maintain a healthy diet and regular physical activity."
            "Overweight" -> "You may need to reduce weight. Consult with a nutritionist or doctor for a safe weight loss plan."
            else -> "You may need special attention. Consult with a doctor or nutritionist."
        }

        binding.tvSaranDesc.visibility = View.VISIBLE
        binding.tvSaranDesc.text = adviceText
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
