package com.fakhry.businessapp.presentation

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.fakhry.businessapp.core.utils.components.viewBinding
import com.fakhry.businessapp.databinding.ActivityMainBinding
import com.fakhry.businessapp.presentation.dashboard.DashboardActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val binding by viewBinding(ActivityMainBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        splashScreen.setKeepOnScreenCondition { true }
        toDashboard()
    }

    private fun toDashboard() {
        lifecycleScope.launch {
            delay(2000)
            startActivity(Intent(this@MainActivity, DashboardActivity::class.java))
            finish()
        }
    }
}