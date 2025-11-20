package com.example.kawi_niveau_mobile_app.ui.auth

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.kawi_niveau_mobile_app.R
import com.example.kawi_niveau_mobile_app.data.network.Resource
import com.example.kawi_niveau_mobile_app.databinding.FragmentRegisterBinding
import com.example.kawi_niveau_mobile_app.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : BaseFragment<FragmentRegisterBinding>() {

    private val viewModel: AuthViewModel by viewModels()

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentRegisterBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.registerButton.setOnClickListener {
            if (validateInput()) {
                val username = binding.usernameInput.text.toString().trim()
                val email = binding.emailInput.text.toString().trim()
                val password = binding.passwordInput.text.toString()
                viewModel.register(username, email, password)
            }
        }

        binding.loginLink.setOnClickListener {
            findNavController().navigateUp()
        }

        observeViewModel()
    }

    private fun validateInput(): Boolean {
        val username = binding.usernameInput.text.toString().trim()
        val email = binding.emailInput.text.toString().trim()
        val password = binding.passwordInput.text.toString()

        if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(requireContext(), "Remplissez tous les champs", Toast.LENGTH_SHORT).show()
            return false
        }

        if (username.length < 3) {
            Toast.makeText(requireContext(), "Le nom d'utilisateur doit contenir au moins 3 caractères", Toast.LENGTH_SHORT).show()
            return false
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(requireContext(), "Format d'email invalide", Toast.LENGTH_SHORT).show()
            return false
        }

        if (password.length < 6) {
            Toast.makeText(requireContext(), "Le mot de passe doit contenir au moins 6 caractères", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    private fun observeViewModel() {
        viewModel.registerResult.observe(viewLifecycleOwner) { result ->
            binding.progressBar.visibility = if (result is Resource.Loading) View.VISIBLE else View.GONE
            when (result) {
                is Resource.Success -> {
                    result.data.message?.let {
                        Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
                    }
                    findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
                }
                is Resource.Error -> {
                    Toast.makeText(requireContext(), result.message, Toast.LENGTH_LONG).show()
                }
                else -> {}
            }
        }
    }
}