package com.example.hw

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.main_activity.*
import kotlinx.android.synthetic.main.main_activity.*
import kotlinx.android.synthetic.main.profile_activity.btn


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        btn.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)

            if (isValidForm()){
                intent.putExtra("name", personName.text.toString())
                intent.putExtra("surname", personSurname.text.toString())
                intent.putExtra("city", personCity.text.toString())
                startActivity(intent)
            }

            else{
                Toast.makeText(applicationContext, "Введите все данные", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun isValidForm():Boolean{
        var isValid = true

        val name = personName.text.toString()
        val surname = personSurname.text.toString()
        val city = personCity.text.toString()

        if(name.trim().isEmpty() || surname.trim().isEmpty() || city.trim().isEmpty()){
            isValid = false
        }
        return isValid
    }
}
