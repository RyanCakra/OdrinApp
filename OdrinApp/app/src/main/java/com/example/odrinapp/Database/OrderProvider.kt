package com.example.odrinapp.Database

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.content.UriMatcher
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.net.Uri

class OrderProvider : ContentProvider() {

    companion object {
        const val ORDER = 100
    }

    private lateinit var mHelper: OrderHelper

    private val sUriMatcher = UriMatcher(UriMatcher.NO_MATCH)

    init {
        sUriMatcher.addURI(OrderContract.CONTENT_AUTHORITY, OrderContract.PATH, ORDER)
    }

    override fun onCreate(): Boolean {
        context?.let {
            mHelper = OrderHelper(it)
        }
        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<String>?,
        selection: String?,
        selectionArgs: Array<String>?,
        sortOrder: String?
    ): Cursor? {
        val match = sUriMatcher.match(uri)
        val database = mHelper.readableDatabase
        return when (match) {
            ORDER -> {
                val cursor = database.query(
                    OrderContract.OrderEntry.TABLE_NAME,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    sortOrder
                )
                cursor.setNotificationUri(context?.contentResolver, uri)
                cursor
            }
            else -> throw IllegalArgumentException("Can't query")
        }
    }

    override fun getType(uri: Uri): String? {
        return null
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val match = sUriMatcher.match(uri)
        return when (match) {
            ORDER -> {
                values?.let {
                    insertCart(uri, it)
                }
            }
            else -> throw IllegalArgumentException("Can't insert data")
        }
    }

    private fun insertCart(uri: Uri, values: ContentValues): Uri? {
        val name = values.getAsString(OrderContract.OrderEntry.COLUMN_NAME)
        if (name == null) {
            throw IllegalArgumentException("Name is required")
        }

        val quantity = values.getAsInteger(OrderContract.OrderEntry.COLUMN_QUANTITY)
        if (quantity == null) {
            throw IllegalArgumentException("Quantity is required")
        }

        val price = values.getAsString(OrderContract.OrderEntry.COLUMN_PRICE)
        if (price == null) {
            throw IllegalArgumentException("Price is required")
        }

        val hasCream = values.getAsBoolean(OrderContract.OrderEntry.COLUMN_CREAM) ?: false
        val hasToppings = values.getAsBoolean(OrderContract.OrderEntry.COLUMN_HASTOPPING) ?: false

        // Set default value for image if not provided
        val image = values.getAsString(OrderContract.OrderEntry.COLUMN_IMAGE) ?: ""

        // Create a new ContentValues object with the updated values
        val updatedValues = ContentValues(values)
        updatedValues.put(OrderContract.OrderEntry.COLUMN_IMAGE, image)

        val database = mHelper.writableDatabase
        val id = database.insert(OrderContract.OrderEntry.TABLE_NAME, null, updatedValues).toInt()

        if (id == -1) {
            return null
        }

        context?.contentResolver?.notifyChange(uri, null)

        val updatedUri = ContentUris.withAppendedId(uri, id.toLong())
        context?.contentResolver?.update(updatedUri, updatedValues, null, null)

        return updatedUri
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        val database = mHelper.writableDatabase
        val match = sUriMatcher.match(uri)
        val rowsDeleted: Int
        when (match) {
            ORDER -> {
                rowsDeleted = database.delete(
                    OrderContract.OrderEntry.TABLE_NAME,
                    selection,
                    selectionArgs
                )
            }
            else -> throw IllegalArgumentException("Cannot delete")
        }

        if (rowsDeleted != 0) {
            context?.contentResolver?.notifyChange(uri, null)
        }
        return rowsDeleted
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<String>?): Int {
        return 0
    }
}
