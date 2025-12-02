package com.example.kawi_niveau_mobile_app.ui.formateur

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
import com.example.kawi_niveau_mobile_app.data.UserPreferences
import com.example.kawi_niveau_mobile_app.databinding.ActivityFormateurHomeBinding
import com.example.kawi_niveau_mobile_app.ui.auth.AuthActivity
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class FormateurHomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFormateurHomeBinding
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    @Inject
    lateinit var userPreferences: UserPreferences
    
    @Inject
    lateinit var userRepository: com.example.kawi_niveau_mobile_app.data.repository.UserRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormateurHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarFormateurHome.toolbar)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment_formateur) as NavHostFragment
        navController = navHostFragment.navController

        // Destinations de premier niveau pour le formateur
        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.formateurDashboardFragment, R.id.nav_formateur_cours, R.id.nav_formateur_profile),
            drawerLayout
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        // Gérer le logout
        navView.setNavigationItemSelectedListener { menuItem ->
            if (menuItem.itemId == R.id.nav_formateur_logout) {
                logout()
                true
            } else {
                drawerLayout.closeDrawers()
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
                    nameTextView.text = if (fullName.isNotEmpty()) fullName else "Formateur"
                    emailTextView.text = profile.email
                    
                    profile.profileImage?.let { filename ->
                        val imageUrl = "${BuildConfig.API_BASE_URL}images/users/$filename"
                        Glide.with(this@FormateurHomeActivity)
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
                    android.util.Log.e("FormateurHomeActivity", "Error loading profile: ${result.message}")
                    nameTextView.text = "Formateur"
                    emailTextView.text = "email@example.com"
                    profileImageView.setImageResource(R.drawable.ic_profile_placeholder)
                }
                else -> {}
            }
        }
    }

    private fun logout() {
        lifecycleScope.launch {
            userPreferences.clearToken()
            val intent = Intent(this@FormateurHomeActivity, AuthActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}
