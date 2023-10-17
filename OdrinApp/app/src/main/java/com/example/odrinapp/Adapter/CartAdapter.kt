package com.example.odrinapp

import android.content.Context
import android.database.Cursor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CursorAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.odrinapp.Database.OrderContract

class CartAdapter(context: Context, cursor: Cursor?, private var selectedImageId: Int) : CursorAdapter(context, cursor, 0) {

    fun setImageId(imageId: Int) {
        selectedImageId = imageId
        notifyDataSetChanged()
    }

    override fun newView(context: Context, cursor: Cursor, parent: ViewGroup): View {
        return LayoutInflater.from(context).inflate(R.layout.cartlist, parent, false)
    }

    override fun bindView(view: View, context: Context, cursor: Cursor) {
        val drinkImage: ImageView = view.findViewById(R.id.drinkImageinOrderSummary)
        val drinkName: TextView = view.findViewById(R.id.drinkNameinOrderSummary)
        val price: TextView = view.findViewById(R.id.priceinOrderSummary)
        val yesCream: TextView = view.findViewById(R.id.hasCream)
        val yesTopping: TextView = view.findViewById(R.id.hasTopping)
        val quantity: TextView = view.findViewById(R.id.quantityinOrderSummary)

        val nameIndex = cursor.getColumnIndex(OrderContract.OrderEntry.COLUMN_NAME)
        val priceIndex = cursor.getColumnIndex(OrderContract.OrderEntry.COLUMN_PRICE)
        val quantityIndex = cursor.getColumnIndex(OrderContract.OrderEntry.COLUMN_QUANTITY)
        val hasCreamIndex = cursor.getColumnIndex(OrderContract.OrderEntry.COLUMN_CREAM)
        val hasToppingIndex = cursor.getColumnIndex(OrderContract.OrderEntry.COLUMN_HASTOPPING)
        val imageIndex = cursor.getColumnIndex(OrderContract.OrderEntry.COLUMN_IMAGE)

        val drinkNameValue = cursor.getString(nameIndex)
        val priceValue = cursor.getString(priceIndex)
        val quantityValue = cursor.getString(quantityIndex)
        val hasCreamValue = cursor.getString(hasCreamIndex)
        val hasToppingValue = cursor.getString(hasToppingIndex)
        val imageValue = cursor.getInt(imageIndex)

        drinkName.text = drinkNameValue
        price.text = priceValue
        yesCream.text = hasCreamValue
        yesTopping.text = hasToppingValue
        quantity.text = quantityValue

        // Menggunakan selectedImageId untuk mengubah gambar dengan Glide
        Glide.with(context)
            .load(selectedImageId)
            .into(drinkImage)
    }
}
