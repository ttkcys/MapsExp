package com.yusudhanlates.ekipprojesi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.yusudhanlates.ekipprojesi.databinding.ActivityMainPageBinding

class MainPage : AppCompatActivity() {

    private lateinit var binding: ActivityMainPageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainPageBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}