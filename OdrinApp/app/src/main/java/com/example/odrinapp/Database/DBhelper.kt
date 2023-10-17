package com.example.odrinapp.Database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBhelper (context: Context): SQLiteOpenHelper(context, "Userdata", null, 1) {
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("create table Userdata (username TEXT primary key, email TEXT, password TEXT)")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("drop table if exists Userdata")
    }

    fun insertdata(username: String, email:String, password:String): Boolean {
        val db = this.writableDatabase
        val cv = ContentValues()
        cv.put("username",  username)
        cv.put("email", email)
        cv.put("password", password)
        val result = db.insert("Userdata", null, cv)
        if (result==-1 .toLong()){
            return false
        }
        return true
    }

    fun checkuserpass(username: String, password: String): Boolean {
        val db = this.writableDatabase
        val query = "select * from Userdata where username= '$username' and password= '$password'"
        val cursor = db.rawQuery(query, null)
        if (cursor.count<=0){
            cursor.close()
            return false
        }

        cursor.close()
        return true
    }
}