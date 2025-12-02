package com.example.kawi_niveau_mobile_app

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import com.example.kawi_niveau_mobile_app.ui.auth.AuthActivity
import com.example.kawi_niveau_mobile_app.ui.auth.AuthViewModel
import com.example.kawi_niveau_mobile_app.ui.home.HomeActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    
    private val authViewModel: AuthViewModel by viewModels()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Vérifier la session utilisateur
        authViewModel.checkSession()
        
        // Observer l'état d'authentification
        authViewModel.isAuthenticated.observe(this) { isAuthenticated ->
            if (isAuthenticated) {
                // Vérifier le rôle de l'utilisateur
                authViewModel.authenticatedUser.observe(this) { user ->
                    user?.let {
                        when (it.role) {
                            "ETUDIANT" -> {
                                android.util.Log.d("MainActivity", "User is ETUDIANT, navigating to HomeActivity")
                                startActivity(Intent(this, HomeActivity::class.java))
                                finish()
                            }
                            "FORMATEUR" -> {
                                android.util.Log.d("MainActivity", "User is FORMATEUR, navigating to FormateurHomeActivity")
                                startActivity(Intent(this, com.example.kawi_niveau_mobile_app.ui.formateur.FormateurHomeActivity::class.java))
                                finish()
                            }
                            else -> {
                                android.util.Log.e("MainActivity", "Unknown role: ${it.role}")
                                startActivity(Intent(this, AuthActivity::class.java))
                                finish()
                            }
                        }
                    }
                }
            } else {
                // Aller à AuthActivity
                android.util.Log.d("MainActivity", "User not authenticated, navigating to Auth")
                startActivity(Intent(this, AuthActivity::class.java))
                finish()
            }
        }
    }
}
