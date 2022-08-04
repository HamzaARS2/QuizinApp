package com.example.myquizapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myquizapp.databinding.ActivityMainBinding
import com.example.myquizapp.utils.lightStatusBar
import com.example.myquizapp.utils.setFullScreen


class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setFullScreen(window)
        lightStatusBar(window, false)
    }


}