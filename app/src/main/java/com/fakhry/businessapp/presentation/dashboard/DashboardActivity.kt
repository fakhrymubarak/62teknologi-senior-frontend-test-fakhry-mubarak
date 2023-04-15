package com.fakhry.businessapp.presentation.dashboard

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.fakhry.businessapp.core.utils.components.viewBinding
import com.fakhry.businessapp.databinding.ActivityDashboardBinding

class DashboardActivity : AppCompatActivity() {
    private val binding by viewBinding(ActivityDashboardBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}