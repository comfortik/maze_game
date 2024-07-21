package com.example.bleservice

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.bleservice.features.main.ui.MainFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container_view, MainFragment()).commit()
    }
}