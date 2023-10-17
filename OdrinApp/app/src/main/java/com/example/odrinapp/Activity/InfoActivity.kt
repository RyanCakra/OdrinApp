package com.example.odrinapp

import android.content.ContentValues
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.loader.app.LoaderManager
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader
import com.bumptech.glide.Glide
import com.example.odrinapp.Database.OrderContract

class InfoActivity : AppCompatActivity(), LoaderManager.LoaderCallbacks<Cursor> {

    private lateinit var imageView: ImageView
    private lateinit var plusquantity: ImageButton
    private lateinit var minusquantity: ImageButton
    private lateinit var quantitynumber: TextView
    private lateinit var drinkName: TextView
    private lateinit var coffeePrice: TextView
    private lateinit var addToppings: CheckBox
    private lateinit var addExtraCream: CheckBox
    private lateinit var addToCart: Button
    private var quantity: Int = 0
    private var mCurrentCartUri: Uri? = null
    private var hasAllRequiredValues: Boolean = false
    private var selectedImageId: Int = 0 // Variable to store the selected image ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)

        imageView = findViewById(R.id.imageViewInfo)
        plusquantity = findViewById(R.id.addquantity)
        minusquantity = findViewById(R.id.subquantity)
        quantitynumber = findViewById(R.id.quantity)
        drinkName = findViewById(R.id.drinkNameinInfo)
        coffeePrice = findViewById(R.id.coffeePrice)
        addToppings = findViewById(R.id.addToppings)
        addToCart = findViewById(R.id.addtocart)
        addExtraCream = findViewById(R.id.addCream)

        drinkName.text = "Granita"

        // Menggunakan Glide untuk memuat gambar dari URI
        Glide.with(this)
            .load(R.drawable.igranita)
            .into(imageView)

        addToCart.setOnClickListener {
            saveCart()
            val intent = Intent(this@InfoActivity, SummaryActivity::class.java)
            intent.putExtra("selected_image_id", R.drawable.igranita) // Store the selected drink image ID
            startActivity(intent)
        }

        plusquantity.setOnClickListener {
            val basePrice = 5
            quantity++
            displayQuantity()
            val price = basePrice * quantity
            coffeePrice.text = "$ $price"

            val totalCoffeePrice = calculatePrice(addExtraCream.isChecked, addToppings.isChecked)
            coffeePrice.text = "$ $totalCoffeePrice"
        }

        minusquantity.setOnClickListener {
            val basePrice = 5
            if (quantity == 0) {
                Toast.makeText(this@InfoActivity, "Cannot decrease quantity below 0", Toast.LENGTH_SHORT).show()
            } else {
                quantity--
                displayQuantity()
                val price = basePrice * quantity
                coffeePrice.text = "$ $price"

                val totalCoffeePrice = calculatePrice(addExtraCream.isChecked, addToppings.isChecked)
                coffeePrice.text = "$ $totalCoffeePrice"
            }
        }

        mCurrentCartUri = intent.data
        selectedImageId = intent.getIntExtra("selected_image_id", 0)
        imageView.setImageResource(selectedImageId)

        if (mCurrentCartUri != null) {
            supportLoaderManager.initLoader(0, null, this) // Initialize the LoaderManager
        }
    }

    private fun saveCart() {
        val name = drinkName.text.toString()
        val price = coffeePrice.text.toString()
        val quantity = quantitynumber.text.toString()

        val values = ContentValues()
        values.put(OrderContract.OrderEntry.COLUMN_NAME, name)
        values.put(OrderContract.OrderEntry.COLUMN_PRICE, price)
        values.put(OrderContract.OrderEntry.COLUMN_QUANTITY, quantity)
        values.put(
            OrderContract.OrderEntry.COLUMN_CREAM,
            if (addExtraCream.isChecked) "Has Cream: YES" else "Has Cream: NO"
        )
        values.put(
            OrderContract.OrderEntry.COLUMN_HASTOPPING,
            if (addToppings.isChecked) "Has Toppings: YES" else "Has Toppings: NO"
        )
        values.put(OrderContract.OrderEntry.COLUMN_IMAGE, selectedImageId)

        if (mCurrentCartUri != null) {
            val updatedRows = contentResolver.update(mCurrentCartUri!!, values, null, null)
            if (updatedRows > 0) {
                Toast.makeText(this, "Cart updated successfully", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Failed to update cart", Toast.LENGTH_SHORT).show()
            }
        } else {
            val newUri = contentResolver.insert(OrderContract.OrderEntry.CONTENT_URI, values)
            if (newUri == null) {
                Toast.makeText(this, "Failed to add to cart", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Item added to cart successfully", Toast.LENGTH_SHORT).show()
            }
        }

        hasAllRequiredValues = true
    }

    private fun calculatePrice(hasExtraCream: Boolean, hasToppings: Boolean): Int {
        var basePrice = 5

        if (hasExtraCream) {
            basePrice += 2
        }

        if (hasToppings) {
            basePrice += 3
        }

        return basePrice * quantity
    }

    private fun displayQuantity() {
        quantitynumber.text = quantity.toString()
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
        val projection = arrayOf(
            OrderContract.OrderEntry._ID,
            OrderContract.OrderEntry.COLUMN_NAME,
            OrderContract.OrderEntry.COLUMN_PRICE,
            OrderContract.OrderEntry.COLUMN_QUANTITY,
            OrderContract.OrderEntry.COLUMN_CREAM,
            OrderContract.OrderEntry.COLUMN_HASTOPPING
        )

        return CursorLoader(this, mCurrentCartUri!!, projection, null, null, null)
    }

    override fun onLoadFinished(loader: Loader<Cursor>, cursor: Cursor?) {
        if (cursor == null || cursor.count < 1) {
            return
        }

        if (cursor.moveToFirst()) {
            val nameColumnIndex = cursor.getColumnIndex(OrderContract.OrderEntry.COLUMN_NAME)
            val priceColumnIndex = cursor.getColumnIndex(OrderContract.OrderEntry.COLUMN_PRICE)
            val quantityColumnIndex = cursor.getColumnIndex(OrderContract.OrderEntry.COLUMN_QUANTITY)
            val creamColumnIndex = cursor.getColumnIndex(OrderContract.OrderEntry.COLUMN_CREAM)
            val toppingColumnIndex = cursor.getColumnIndex(OrderContract.OrderEntry.COLUMN_HASTOPPING)

            val name = cursor.getString(nameColumnIndex)
            val price = cursor.getString(priceColumnIndex)
            val quantity = cursor.getString(quantityColumnIndex)
            val hasCream = cursor.getString(creamColumnIndex)
            val hasTopping = cursor.getString(toppingColumnIndex)

            drinkName.text = name
            coffeePrice.text = price
            quantitynumber.text = quantity

            addExtraCream.isChecked = hasCream == "Has Cream: YES"
            addToppings.isChecked = hasTopping == "Has Toppings: YES"
        }
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {
        drinkName.text = ""
        coffeePrice.text = ""
        quantitynumber.text = ""
        addExtraCream.isChecked = false
        addToppings.isChecked = false
    }
}
