package com.example.kawi_niveau_mobile_app

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import com.example.kawi_niveau_mobile_app.ui.auth.AuthActivity

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startActivity(Intent(this, AuthActivity::class.java))
        finish()
    }
}
