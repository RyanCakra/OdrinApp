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

class signup : AppCompatActivity() {

    private lateinit var uname: EditText
    private lateinit var umail: EditText
    private lateinit var pword: EditText
    private lateinit var signupbtn: Button
    private lateinit var db: DBhelper


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        uname = findViewById(R.id.editTextTextRegisName)
        umail = findViewById(R.id.editTextTextRegisEmail)
        pword = findViewById(R.id.editTextTextPassword)
        signupbtn = findViewById(R.id.regisBtn)
        db = DBhelper(this )

        signupbtn.setOnClickListener{
            val unametext = uname.text.toString()
            val umailtext = umail.text.toString()
            val pwordtext = pword.text.toString()
            val savedata = db.insertdata(unametext, umailtext ,pwordtext)

            if (TextUtils.isEmpty(unametext) || TextUtils.isEmpty(umailtext) || TextUtils.isEmpty(pwordtext)){
                    Toast.makeText(this, "Add Username, Email, & Password", Toast.LENGTH_SHORT).show()
            }
            else{
                if (savedata==true) {
                    Toast.makeText(this, "Signup Successfully", Toast.LENGTH_SHORT).show()
                    val intent = Intent(applicationContext, login::class.java)
                    startActivity(intent)
                }
                else{
                    Toast.makeText(this, "User Exist", Toast.LENGTH_SHORT).show()

                }
            }
        }
    }
}