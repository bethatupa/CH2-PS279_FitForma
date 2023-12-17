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

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var passwordTextInputLayout: TextInputLayout
    private lateinit var confpasswordTextInputLayout:TextInputLayout
    private lateinit var passwordEditText: TextInputEditText
    private lateinit var confpasswordEditText: TextInputEditText
    private var isPasswordVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        binding.textViewSignIn.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        passwordTextInputLayout = binding.passwordTextInputLayout
        passwordEditText = binding.passwordEdt
        confpasswordTextInputLayout = binding.confpasswordTextInputLayout
        confpasswordEditText = binding.confpasswordEdt

        passwordTextInputLayout.setEndIconOnClickListener {
            togglePasswordVisibility()
        }

        confpasswordTextInputLayout.setEndIconOnClickListener{
            toggleConfPasswordVisibility()
        }

        binding.btnRegis.setOnClickListener{
            val name = binding.nameEdt.text.toString()
            val email = binding.emailEdt.text.toString()
            val age = binding.ageEdt.text.toString()
            val weight = binding.weightEdt.text.toString()
            val height = binding.heightEdt.text.toString()
            val pass = binding.passwordEdt.text.toString()
            val confpass = binding.confpasswordEdt.text.toString()

            if(name.isNotEmpty() && email.isNotEmpty() && pass.isNotEmpty() && confpass.isNotEmpty() && age.isNotEmpty() && weight.isNotEmpty() && height.isNotEmpty()){
                if(pass == confpass){
                    firebaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener{
                        if(it.isSuccessful){
                            val intent = Intent(this, LoginActivity::class.java)
                            Toast.makeText(this, "Please login", Toast.LENGTH_SHORT).show()
                            startActivity(intent)
                        }else   {
                            Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                        }
                    }
                } else{
                    Toast.makeText(this, "Password is not matching", Toast.LENGTH_SHORT).show()
                }
            }else {
                Toast.makeText(this, "Empty fields are not allowed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun togglePasswordVisibility() {
        isPasswordVisible = !isPasswordVisible

        val drawable: Drawable? = if (isPasswordVisible) {
            ContextCompat.getDrawable(this, R.drawable.visibility_off)
        } else {
            ContextCompat.getDrawable(this, R.drawable.visibility)
        }

        passwordTextInputLayout.endIconDrawable = drawable
        passwordEditText.inputType =
            if (isPasswordVisible) android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            else android.text.InputType.TYPE_CLASS_TEXT or android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD

        // Move the cursor to the end of the text after changing the input type
        passwordEditText.text?.let { passwordEditText.setSelection(it.length) }
    }

    private fun toggleConfPasswordVisibility() {
        isPasswordVisible = !isPasswordVisible

        val drawable: Drawable? = if (isPasswordVisible) {
            ContextCompat.getDrawable(this, R.drawable.visibility_off)
        } else {
            ContextCompat.getDrawable(this, R.drawable.visibility)
        }

        confpasswordTextInputLayout.endIconDrawable = drawable
        confpasswordEditText.inputType =
            if (isPasswordVisible) android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            else android.text.InputType.TYPE_CLASS_TEXT or android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD

        // Move the cursor to the end of the text after changing the input type
        confpasswordEditText.text?.let { confpasswordEditText.setSelection(it.length) }
    }
}