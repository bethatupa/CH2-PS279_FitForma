package com.example.fitforma.ui.register

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.fitforma.databinding.ActivityAfterRegisBinding
import com.example.fitforma.ui.main.MainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AfterRegisActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAfterRegisBinding
    private lateinit var firebaseAuth : FirebaseAuth
    private var db = Firebase.firestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAfterRegisBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        val user = firebaseAuth.currentUser

        if(user != null){
            val userRef = db.collection("users").document(user.uid)
            userRef.get().addOnCompleteListener { task ->
                if(task.isSuccessful){
                    val document: DocumentSnapshot? = task.result
                    if (document != null && document.exists()) {
                        val weight = document.getDouble("weight")
                        val height = document.getDouble("height")

                        //display bmi category
                        if (weight != null && height != null) {
                            val bmi = calculateBMIValue(weight, height)

                            val bmiCategory = determineBMICategory(bmi)
                            setBMICat(bmiCategory)
                            setAdvice(bmiCategory)
                        }
                    }
                }
            }
        }

        binding.nextBtn.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
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

    private fun setBMICat(bmiCategory: String){
        val setCat = when (bmiCategory) {
            "Underweight" -> "Underweight"
            "Normal Weight" -> "Normal Weight"
            "Overweight" -> "Overweight"
            else -> ":("
        }
        // Show appropriate advice based on the BMI category
        binding.tvBmiCategory.text = setCat
    }

    @SuppressLint("SetTextI18n")
    private fun setAdvice(bmiCategory: String) {
        // Set the advice based on the BMI category
        when (bmiCategory) {
            "Underweight" -> {
                binding.tvAdvice.visibility = View.VISIBLE
                binding.tvAdvice.text = "You need to gain your weight"
            }
            "Normal Weight" -> {
                binding.tvAdvice.visibility = View.VISIBLE
                binding.tvAdvice.text = "You can Reduce/Maintain/Gain your weight"
            }
            "Overweight" -> {
                binding.tvAdvice.visibility = View.VISIBLE
                binding.tvAdvice.text = "You need to reduce your weight"
            }
        }
    }
}