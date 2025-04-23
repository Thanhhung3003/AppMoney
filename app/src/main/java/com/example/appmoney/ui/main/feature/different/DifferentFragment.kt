package com.example.appmoney.ui.main.feature.different

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.appmoney.databinding.FragmentDifferentBinding
import com.example.appmoney.ui.login.LoginActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

class DifferentFragment : Fragment() {

    private var _binding : FragmentDifferentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDifferentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnLogout.setOnClickListener {
            Firebase.auth.signOut()
            val i = Intent(requireContext(),LoginActivity::class.java)
            startActivity(i)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}