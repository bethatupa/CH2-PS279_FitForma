package com.example.fitforma.ui.account

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.fitforma.databinding.FragmentAccountBinding
import com.example.fitforma.ui.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth

class AccountFragment : Fragment() {
    private var _binding: FragmentAccountBinding? = null
    lateinit var firebaseAuth: FirebaseAuth
    private val binding get() = _binding !!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
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

        if (user != null){
            binding.usernameTextView.text = user.displayName  //blm nyambung ke firestore
            binding.emailTextView.text = user.email
        }

        binding.logoutBtn.setOnClickListener{
            logout()
        }
    }

    private fun logout() {
        firebaseAuth = FirebaseAuth.getInstance()
        firebaseAuth.signOut()
        val i = Intent(context, LoginActivity::class.java)
        startActivity(i)
        activity?.finish()
    }
}