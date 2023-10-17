package com.example.odrinapp

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.odrinapp.Database.DBhelper

class login : AppCompatActivity() {

    private lateinit var loginbtn: Button
    private lateinit var edituser: EditText
    private lateinit var editpword: EditText
    private lateinit var dbh: DBhelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        loginbtn = findViewById(R.id.loginBtn)
        edituser = findViewById(R.id.editTextTextLoginUser)
        editpword = findViewById(R.id.editTextTextPassword2)
        dbh = DBhelper(this)

        loginbtn.setOnClickListener {

            val useredtx = edituser.text.toString().trim()
            val passedtx = editpword.text.toString().trim()

            if (TextUtils.isEmpty(useredtx) || TextUtils.isEmpty(passedtx)) {
                Toast.makeText(this@login, "Add Username & Password", Toast.LENGTH_SHORT).show()
            } else {
                val checkuser = dbh.checkuserpass(useredtx, passedtx)
                if (checkuser) {
                    Toast.makeText(this@login, "Login Successful", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@login, MainActivity::class.java)
                    startActivity(intent)
                    finish() // Optional: untuk menutup activity login setelah login berhasil
                } else {
                    Toast.makeText(this@login, "Wrong Username & Password", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
