package com.example.fitforma.ui.login

import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import com.example.fitforma.R
import com.example.fitforma.databinding.ActivityLoginBinding
import com.example.fitforma.ui.register.RegisterActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var passwordTextInputLayout: TextInputLayout
    private lateinit var passwordEditText: TextInputEditText
    private var isPasswordVisible = false


    //private val viewModel by viewModels<LoginViewModel>{
    //not yet implemented
    //}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        passwordTextInputLayout = findViewById(R.id.passwordTextInputLayout)
        passwordEditText = findViewById(R.id.passwordEditText)

        passwordTextInputLayout.setEndIconOnClickListener {
            togglePasswordVisibility()
        }

        setupAction()
    }

    private fun togglePasswordVisibility() {
        isPasswordVisible = !isPasswordVisible

        val drawable: Drawable? = if (isPasswordVisible) {
            ContextCompat.getDrawable(this, R.drawable.hide_pass)
        } else {
            ContextCompat.getDrawable(this, R.drawable.show_pass)
        }

        passwordTextInputLayout.endIconDrawable = drawable
        passwordEditText.inputType =
            if (isPasswordVisible) android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            else android.text.InputType.TYPE_CLASS_TEXT or android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD

        // Move the cursor to the end of the text after changing the input type
        passwordEditText.text?.let { passwordEditText.setSelection(it.length) }
    }

    private fun setupAction(){
        binding.btnLogin.setOnClickListener{
            val username = binding.editTextUsername.text.toString()
            val password = binding.passwordEditText.text.toString()
            when{
                username == "" ->{
                    binding.editTextUsername.error = getString(R.string.err_username)
                }
                password == "" ->{
                    binding.passwordEditText.error = getString(R.string.err_pass)
                }
                else->{
                    //not yet implemented
                }
            }
        }
    }

    fun navigateToRegister(view: android.view.View){
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }


}