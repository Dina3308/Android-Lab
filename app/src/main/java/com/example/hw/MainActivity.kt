package com.example.hw

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_third.*


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        navigateTo(FirstFragment())
    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { menuItem ->
        when (menuItem.itemId) {
            R.id.firstFragment -> {
                navigateTo(FirstFragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.secondFragment -> {
                navigateTo(SecondFragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.thirdFragment -> {
                navigateTo(ThirdFragment())
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }
    
    private fun navigateTo(fragment: Fragment){
        supportFragmentManager.beginTransaction().replace(R.id.container, fragment, fragment.javaClass.simpleName).commit()
    }    
}
