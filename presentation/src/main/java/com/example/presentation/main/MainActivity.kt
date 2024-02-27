package com.example.presentation.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.presentation.core.CustomViewModel
import com.example.presentation.core.ProvideViewModel
import com.example.presentation.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), ProvideViewModel {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel = viewModel(MainViewModel::class.java)

        viewModel.liveData().observe(this) {
            it.show(binding.root.id, supportFragmentManager)
        }

        viewModel.init(isFirstRun = savedInstanceState == null)
    }

    override fun <T : CustomViewModel> viewModel(clazz: Class<out T>): T {
        return (application as ProvideViewModel).viewModel(clazz)
    }
}

