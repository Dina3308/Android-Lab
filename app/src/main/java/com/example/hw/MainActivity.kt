package com.example.hw

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val phone: Phone = Phone("Samsung Exynos 9610 Octa",6,"Samsung",17250, "Vietnam")
        phone.off()
        phone.on()
        phone.getInfo()
    }
}