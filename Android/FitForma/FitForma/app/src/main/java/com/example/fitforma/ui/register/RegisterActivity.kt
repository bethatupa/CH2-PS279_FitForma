package com.example.fitforma.ui.register

import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.fitforma.R
import com.example.fitforma.databinding.ActivityRegisterBinding
import com.example.fitforma.ui.login.LoginActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private var db = Firebase.firestore

    private lateinit var passwordTextInputLayout: TextInputLayout
    private lateinit var confpasswordTextInputLayout: TextInputLayout
    private lateinit var passwordEditText: TextInputEditText
    private lateinit var confpasswordEditText: TextInputEditText
    private var isPasswordVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        passwordTextInputLayout = binding.passwordTextInputLayout
        passwordEditText = binding.passwordEdt
        confpasswordTextInputLayout = binding.confpasswordTextInputLayout
        confpasswordEditText = binding.confpasswordEdt

        binding.textViewSignIn.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        passwordTextInputLayout.setEndIconOnClickListener {
            togglePasswordVisibility()
        }

        confpasswordTextInputLayout.setEndIconOnClickListener {
            toggleConfPasswordVisibility()
        }

        binding.btnRegis.setOnClickListener {
            val name = binding.nameEdt.text.toString()
            val email = binding.emailEdt.text.toString()
            val age = binding.ageEdt.text.toString()
            val weight = binding.weightEdt.text.toString()
            val height = binding.heightEdt.text.toString()
            val pass = binding.passwordEdt.text.toString()
            val confpass = binding.confpasswordEdt.text.toString()
            if (name.isNotEmpty() && email.isNotEmpty() && pass.isNotEmpty() && confpass.isNotEmpty() && age.isNotEmpty() && weight.isNotEmpty() && height.isNotEmpty()) {
                if (pass == confpass) {
                    registerAcc(name, email, pass, age, weight, height)
                } else {
                    Toast.makeText(this, "Password is not matching", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Empty fields are not allowed", Toast.LENGTH_SHORT).show()
            }
        }
    }
//TODO benerin koneksi firestore
    private fun registerAcc(
        name: String,
        email: String,
        pass: String,
        age: String,
        weight: String,
        height: String
    ) {
        firebaseAuth.createUserWithEmailAndPassword(email, pass)
            .addOnCompleteListener(this) {
                if (it.isSuccessful) {
                    val userMap = hashMapOf(
                        "name" to name,
                        "email" to email,
                        "age" to age,
                        "weight" to weight,
                        "height" to height
                    )
                    val userId = firebaseAuth.currentUser!!.uid
                    db.collection("users").document(userId).set(userMap)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Welcome", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener {
                            Toast.makeText(this, "Failed to save data", Toast.LENGTH_SHORT).show()
                        }
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(
                        this,
                        "Registration failed: ${it.exception?.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private fun togglePasswordVisibility() {
        isPasswordVisible = !isPasswordVisible
        updatePasswordVisibility(passwordTextInputLayout, passwordEditText)
    }

    private fun toggleConfPasswordVisibility() {
        isPasswordVisible = !isPasswordVisible
        updatePasswordVisibility(confpasswordTextInputLayout, confpasswordEditText)
    }

    private fun updatePasswordVisibility(
        textInputLayout: TextInputLayout,
        textInputEditText: TextInputEditText
    ) {
        val drawable: Drawable? = if (isPasswordVisible) {
            ContextCompat.getDrawable(this, R.drawable.visibility_off)
        } else {
            ContextCompat.getDrawable(this, R.drawable.visibility)
        }

        textInputLayout.endIconDrawable = drawable
        textInputEditText.inputType =
            if (isPasswordVisible) android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            else android.text.InputType.TYPE_CLASS_TEXT or android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD
        textInputEditText.text?.let { textInputEditText.setSelection(it.length) }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        moveTaskToBack(true)
        super.onBackPressed()
    }
}
