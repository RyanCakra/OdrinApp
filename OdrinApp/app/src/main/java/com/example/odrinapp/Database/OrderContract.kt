package com.example.odrinapp.Database

import android.net.Uri
import android.provider.BaseColumns

object OrderContract {

    const val CONTENT_AUTHORITY = "com.example.odrinapp"
    val BASE_URI: Uri = Uri.parse("content://$CONTENT_AUTHORITY")
    const val PATH = "orderig"

    object OrderEntry : BaseColumns {
        val CONTENT_URI: Uri = Uri.withAppendedPath(BASE_URI, PATH)
        const val TABLE_NAME = "orderig"
        const val _ID = BaseColumns._ID
        const val COLUMN_NAME = "name"
        const val COLUMN_QUANTITY = "quantity"
        const val COLUMN_PRICE = "price"
        const val COLUMN_HASTOPPING = "hastoppings"
        const val COLUMN_CREAM = "hascream"
        const val COLUMN_IMAGE = "image" // Tambahkan kolom untuk gambar
    }
}
