package com.example.fitforma.ui.login

import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.fitforma.R
import com.example.fitforma.databinding.ActivityLoginBinding
import com.example.fitforma.ui.main.MainActivity
import com.example.fitforma.ui.register.RegisterActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var passwordTextInputLayout: TextInputLayout
    private lateinit var passwordEditText: TextInputEditText
    private var isPasswordVisible = false

    private lateinit var firebaseAuth : FirebaseAuth


    //private val viewModel by viewModels<LoginViewModel>{
        //not yet implemented
    //}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth  = FirebaseAuth.getInstance()
        binding.textViewSignUp.setOnClickListener{
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        binding.btnLogin.setOnClickListener{
            val email = binding.emailEdt.text.toString()
            val pass = binding.passwordEdt.text.toString()

            if (email.isNotEmpty() && pass.isNotEmpty()) {

                firebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener {
                    if (it.isSuccessful) {
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this, "Wrong Password or Email", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "Empty Fields Are not Allowed !!", Toast.LENGTH_SHORT).show()
            }
        }

        passwordTextInputLayout = binding.passwordTextInputLayout
        passwordEditText = binding.passwordEdt

        passwordTextInputLayout.setEndIconOnClickListener {
            togglePasswordVisibility()
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

    override fun onStart() {
        super.onStart()

        if(firebaseAuth.currentUser != null){
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        // Check if the user is on the login screen (LoginActivity)
        if (isTaskRoot) {
            // If on the login screen, exit the app
            moveTaskToBack(true)
        } else {
            // If not on the login screen, perform the default back action
            super.onBackPressed()
        }
    }
}