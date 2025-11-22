package com.example.kawi_niveau_mobile_app.ui.home

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.kawi_niveau_mobile_app.R
import com.example.kawi_niveau_mobile_app.data.UserPreferences
import com.example.kawi_niveau_mobile_app.databinding.ActivityHomeBinding
import com.example.kawi_niveau_mobile_app.ui.auth.AuthActivity
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    @Inject
    lateinit var userPreferences: UserPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment_home) as NavHostFragment
        navController = navHostFragment.navController

        // Définir les destinations de premier niveau (celles qui ont le bouton menu)
        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.homeFragment, R.id.nav_courses, R.id.nav_progress, R.id.nav_profile),
            drawerLayout
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        // Gérer les clics sur les éléments du menu qui ne sont pas de la navigation
        navView.setNavigationItemSelectedListener { menuItem ->
            if (menuItem.itemId == R.id.nav_logout) {
                logout()
                true // Indique que l'événement a été géré
            } else {
                // Laisser NavigationUI gérer la navigation
                // Fermer le tiroir
                drawerLayout.closeDrawers()
                // Naviguer vers la destination
                navController.navigate(menuItem.itemId)
                true
            }
        }

        loadUserInfo()
    }

    private fun loadUserInfo() {
        val headerView = binding.navView.getHeaderView(0)
        val nameTextView = headerView.findViewById<TextView>(R.id.textView_name)
        val emailTextView = headerView.findViewById<TextView>(R.id.textView_email)

        lifecycleScope.launch {
            // TODO: Récupérer les vraies informations utilisateur
            nameTextView.text = "Utilisateur Anonyme"
            emailTextView.text = "email@example.com"
        }
    }

    private fun logout() {
        lifecycleScope.launch {
            userPreferences.clearToken()
            val intent = Intent(this@HomeActivity, AuthActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        // Gère l'ouverture/fermeture du menu avec le bouton hamburger
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}