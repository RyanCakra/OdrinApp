package com.example.odrinapp.Database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class OrderHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "ord.db"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val SQL_TABLE = "CREATE TABLE " + OrderContract.OrderEntry.TABLE_NAME + " (" +
                OrderContract.OrderEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                OrderContract.OrderEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                OrderContract.OrderEntry.COLUMN_QUANTITY + " TEXT NOT NULL, " +
                OrderContract.OrderEntry.COLUMN_PRICE + " TEXT NOT NULL, " +
                OrderContract.OrderEntry.COLUMN_HASTOPPING + " TEXT NOT NULL, " +
                OrderContract.OrderEntry.COLUMN_CREAM + " TEXT NOT NULL, " +
                OrderContract.OrderEntry.COLUMN_IMAGE + " TEXT NOT NULL);"

        db.execSQL(SQL_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Implement migration logic here if needed
    }
}