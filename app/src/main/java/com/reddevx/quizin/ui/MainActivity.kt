package com.reddevx.quizin.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.reddevx.quizin.databinding.ActivityMainBinding
import com.reddevx.quizin.utils.lightStatusBar
import com.reddevx.quizin.utils.setFullScreen


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