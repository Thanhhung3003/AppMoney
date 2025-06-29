package com.example.appmoney.ui.main.feature.different.changepass

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.appmoney.databinding.FragmentChangePasswordBinding
import com.example.appmoney.ui.main.main_screen.AppScreen
import com.example.appmoney.ui.main.main_screen.navigateFragment


class ChangePasswordFragment : Fragment() {
    private var _binding: FragmentChangePasswordBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: ChangePasswordViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChangePasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(ChangePasswordViewModel::class.java)

        binding.btnChangePassword.setOnClickListener {
            val oldPass = binding.edtCurrentPassword.text.toString().trim()
            val newPass = binding.edtNewPassword.text.toString().trim()
            val confirmPass = binding.edtConfirmNewPassword.text.toString().trim()
            viewModel.changePassword(oldPass, newPass, confirmPass)
            requireActivity().navigateFragment(AppScreen.Different)
        }
        viewModel.toastMessage.observe(viewLifecycleOwner) { message ->
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        }
        binding.btnBackToProfile.setOnClickListener {
            requireActivity().navigateFragment(AppScreen.Different)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}



