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
import com.bumptech.glide.Glide
import com.example.kawi_niveau_mobile_app.BuildConfig
import com.example.kawi_niveau_mobile_app.R
import com.example.kawi_niveau_mobile_app.data.local.dao.UserDao
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
    lateinit var userDao: UserDao
    
    @Inject
    lateinit var userRepository: com.example.kawi_niveau_mobile_app.data.repository.UserRepository

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
        val profileImageView = headerView.findViewById<ImageView>(R.id.imageView_profile)

        lifecycleScope.launch {
            val result = userRepository.getProfile()
            when (result) {
                is com.example.kawi_niveau_mobile_app.data.network.Resource.Success -> {
                    val profile = result.data
                    val fullName = "${profile.firstName ?: ""} ${profile.lastName ?: ""}".trim()
                    nameTextView.text = if (fullName.isNotEmpty()) fullName else "Utilisateur"
                    emailTextView.text = profile.email
                    
                    // Charger la photo de profil avec Glide
                    profile.profileImage?.let { filename ->
                        val imageUrl = "${BuildConfig.API_BASE_URL}images/users/$filename"
                        Glide.with(this@HomeActivity)
                            .load(imageUrl)
                            .placeholder(R.drawable.ic_profile_placeholder)
                            .error(R.drawable.ic_profile_placeholder)
                            .circleCrop()
                            .into(profileImageView)
                    } ?: run {
                        profileImageView.setImageResource(R.drawable.ic_profile_placeholder)
                    }
                }
                is com.example.kawi_niveau_mobile_app.data.network.Resource.Error -> {
                    android.util.Log.e("HomeActivity", "Error loading profile: ${result.message}")
                    nameTextView.text = "Utilisateur"
                    emailTextView.text = "email@example.com"
                    profileImageView.setImageResource(R.drawable.ic_profile_placeholder)
                }
                else -> {}
            }
        }
    }

    private fun logout() {
        lifecycleScope.launch {
            userDao.clearUser()
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