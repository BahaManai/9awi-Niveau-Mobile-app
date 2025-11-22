package com.example.kawi_niveau_mobile_app.ui.auth

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.kawi_niveau_mobile_app.R
import com.example.kawi_niveau_mobile_app.data.network.Resource
import com.example.kawi_niveau_mobile_app.databinding.FragmentLoginBinding
import com.example.kawi_niveau_mobile_app.ui.base.BaseFragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>() {

    private val viewModel: AuthViewModel by viewModels()
    private lateinit var googleSignInClient: GoogleSignInClient

    private val googleSignInLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        Log.d("LoginFragment", "Google sign in result received - Result code: ${result.resultCode}")
        if (result.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            handleGoogleSignInResult(task)
        } else if (result.resultCode == Activity.RESULT_CANCELED) {
            Log.d("LoginFragment", "Google sign in cancelled by user")
            Toast.makeText(requireContext(), "Connexion annulée", Toast.LENGTH_SHORT).show()
        } else {
            Log.e("LoginFragment", "Google sign in failed with result code: ${result.resultCode}")
            Toast.makeText(requireContext(), "Erreur lors de la connexion", Toast.LENGTH_SHORT).show()
        }
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentLoginBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupGoogleSignIn()

        binding.loginButton.setOnClickListener {
            val email = binding.emailInput.text.toString().trim()
            val password = binding.passwordInput.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                viewModel.login(email, password)
            } else {
                Toast.makeText(requireContext(), "Remplissez tous les champs", Toast.LENGTH_SHORT).show()
            }
        }

        binding.registerLink.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        binding.googleSignInButton.setOnClickListener {
            signInWithGoogle()
        }

        observeViewModel()
    }

    private fun setupGoogleSignIn() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("428009874445-uirq408arbih2pstc2225h67faophn0j.apps.googleusercontent.com")
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
    }

    private fun signInWithGoogle() {
        Log.d("LoginFragment", "Google sign in initiated")
        val signInIntent = googleSignInClient.signInIntent
        googleSignInLauncher.launch(signInIntent)
    }

    private fun handleGoogleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            Log.d("LoginFragment", "Handling Google sign in result")
            val account = completedTask.getResult(ApiException::class.java)
            val idToken = account?.idToken
            
            Log.d("LoginFragment", "Account: ${account?.email}")
            Log.d("LoginFragment", "ID Token: ${if (idToken != null) idToken.take(50) + "..." else "null"}")
            
            if (idToken != null) {
                Log.d("LoginFragment", "Calling viewModel.loginWithGoogle")
                viewModel.loginWithGoogle(idToken)
            } else {
                Log.e("LoginFragment", "ID Token is null")
                Toast.makeText(requireContext(), "Échec de l'authentification Google - Token null", Toast.LENGTH_SHORT).show()
            }
        } catch (e: ApiException) {
            Log.e("LoginFragment", "Google sign in failed - Status code: ${e.statusCode}", e)
            val errorMessage = when (e.statusCode) {
                10 -> "Erreur de configuration (SHA-1). Vérifiez la console Google Cloud."
                12500 -> "Connexion annulée"
                12501 -> "Échec de la connexion"
                7 -> "Erreur réseau"
                else -> "Erreur Google Sign-In: ${e.statusCode}"
            }
            Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_LONG).show()
        }
    }

    private fun observeViewModel() {
        viewModel.loginResult.observe(viewLifecycleOwner) { result ->
            Log.d("LoginFragment", "Login result received: ${result.javaClass.simpleName}")
            binding.progressBar.visibility = if (result is Resource.Loading) View.VISIBLE else View.GONE
            when (result) {
                is Resource.Success -> {
                    Log.d("LoginFragment", "Login success - Token: ${if (result.data.token != null) "present" else "null"}")
                    if (result.data.token != null) {
                        Log.d("LoginFragment", "Navigating to home")
                        findNavController().navigate(R.id.action_loginFragment_to_homeActivity)
                    } else {
                        Log.e("LoginFragment", "Token is null in success response")
                        result.data.message?.let {
                            Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
                        }
                    }
                }
                is Resource.Error -> {
                    Log.e("LoginFragment", "Login error: ${result.message}")
                    Toast.makeText(requireContext(), "Erreur: ${result.message}", Toast.LENGTH_LONG).show()
                }
                else -> {
                    Log.d("LoginFragment", "Login loading...")
                }
            }
        }
    }
}