package com.example.odrinapp

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class welcome : AppCompatActivity() {

    private lateinit var bregis: Button
    private lateinit var blogin: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        bregis = findViewById(R.id.btnregiss)
        blogin = findViewById(R.id.btnloginn)

        bregis.setOnClickListener{
            val intent = Intent(this, signup::class.java)
            startActivity(intent)
        }

        blogin.setOnClickListener{
            val intent = Intent(this, login::class.java)
            startActivity(intent)
        }
    }
}