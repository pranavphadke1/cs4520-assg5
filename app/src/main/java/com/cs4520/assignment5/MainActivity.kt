package com.cs4520.assignment5

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.cs4520.assignment5.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var activity_main_binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity_main_binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activity_main_binding.root)
    }
}