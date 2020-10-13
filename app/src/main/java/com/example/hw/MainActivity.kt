package com.example.hw

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fragments = arrayListOf<Fragment>(
            HomeFragment(),
            CallFragment(),
            MessageFragment(),
            NoteFragment(),
            ProfileFragment()
        )

        home.setOnClickListener {
            it.isSelected = true
            call.isSelected = false
            note.isSelected = false
            message.isSelected = false
            profile.isSelected = false
            replaceFragment(fragments[0])
        }
        call.setOnClickListener {
            it.isSelected = true
            home.isSelected = false
            note.isSelected = false
            message.isSelected = false
            profile.isSelected = false
            replaceFragment(fragments[1])
        }
        message.setOnClickListener {
            it.isSelected = true
            home.isSelected = false
            note.isSelected = false
            call.isSelected = false
            profile.isSelected = false
            replaceFragment(fragments[2])
        }
        note.setOnClickListener {
            it.isSelected = true
            home.isSelected = false
            call.isSelected = false
            message.isSelected = false
            profile.isSelected = false
            replaceFragment(fragments[3])
        }
        profile.setOnClickListener {
            it.isSelected = true
            home.isSelected = false
            note.isSelected = false
            message.isSelected = false
            call.isSelected = false
            replaceFragment(fragments[4])
        }
    }

    private fun replaceFragment(fragment: Fragment?) {
        val transaction = supportFragmentManager.beginTransaction()
        if (fragment != null) {
            transaction.replace(R.id.frameLayout, fragment)
            transaction.commit()
        }
    }
}