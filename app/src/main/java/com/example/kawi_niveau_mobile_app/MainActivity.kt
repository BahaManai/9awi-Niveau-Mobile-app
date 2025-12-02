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
                // Aller à HomeActivity
                android.util.Log.d("MainActivity", "User is authenticated, navigating to Home")
                startActivity(Intent(this, HomeActivity::class.java))
            } else {
                // Aller à AuthActivity
                android.util.Log.d("MainActivity", "User not authenticated, navigating to Auth")
                startActivity(Intent(this, AuthActivity::class.java))
            }
            finish()
        }
    }
}
