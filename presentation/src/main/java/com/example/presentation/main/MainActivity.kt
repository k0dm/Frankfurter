package com.example.presentation.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import com.example.presentation.core.ProvideViewModel
import com.example.presentation.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), ProvideViewModel {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel = viewModel(MainViewModel::class.java)

        viewModel.init(isFirstRun = savedInstanceState == null)
    }

    override fun <T : ViewModel> viewModel(clazz: Class<out T>): T {
        TODO("Not yet implemented")
    }
}

