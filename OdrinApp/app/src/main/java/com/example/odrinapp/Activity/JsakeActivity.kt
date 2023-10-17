package com.example.odrinapp

import android.app.LoaderManager
import android.content.ContentValues
import android.content.CursorLoader
import android.content.Intent
import android.content.Loader
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.odrinapp.Database.OrderContract

class JsakeActivity : AppCompatActivity(), LoaderManager.LoaderCallbacks<Cursor> {

    private lateinit var imageView: ImageView
    private lateinit var plusquantity: ImageButton
    private lateinit var minusquantity: ImageButton
    private lateinit var quantitynumber: TextView
    private lateinit var drinnkName: TextView
    private lateinit var coffeePrice: TextView
    private lateinit var addToppings: CheckBox
    private lateinit var addExtraCream: CheckBox
    private lateinit var addtoCart: Button
    private var quantity = 0
    private var mCurrentCartUri: Uri? = null
    private var hasAllRequiredValues = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)

        imageView = findViewById(R.id.imageViewInfo)
        plusquantity = findViewById(R.id.addquantity)
        minusquantity = findViewById(R.id.subquantity)
        quantitynumber = findViewById(R.id.quantity)
        drinnkName = findViewById(R.id.drinkNameinInfo)
        coffeePrice = findViewById(R.id.coffeePrice)
        addToppings = findViewById(R.id.addToppings)
        addtoCart = findViewById(R.id.addtocart)
        addExtraCream = findViewById(R.id.addCream)

        // setting the name of drink
        drinnkName.text = "Sake"
        imageView.setImageResource(R.drawable.jsake)

        addtoCart.setOnClickListener {
            val intent = Intent(this@JsakeActivity, SummaryActivity::class.java)
            startActivity(intent)
            // once this button is clicked we want to save our values in the database and send those values
            // right away to summary activity where we display the order info
            saveCart()
        }

        plusquantity.setOnClickListener {
            // coffee price
            val basePrice = 5
            quantity++
            displayQuantity()
            val coffeePrice = basePrice * quantity
            val setnewPrice = coffeePrice.toString()
            this.coffeePrice.text = setnewPrice

            // checkBoxes functionality
            val ifCheckBox = calculatePrice(addExtraCream, addToppings)
            this.coffeePrice.text = "$ $ifCheckBox"
        }

        minusquantity.setOnClickListener {
            val basePrice = 5
            // because we don't want the quantity to go below 0
            if (quantity == 0) {
                Toast.makeText(this@JsakeActivity, "Can't decrease quantity below 0", Toast.LENGTH_SHORT).show()
            } else {
                quantity--
                displayQuantity()
                val coffeePrice = basePrice * quantity
                val setnewPrice = coffeePrice.toString()
                this.coffeePrice.text = setnewPrice

                // checkBoxes functionality
                val ifCheckBox = calculatePrice(addExtraCream, addToppings)
                this.coffeePrice.text = "$ $ifCheckBox"
            }
        }
    }

    private fun saveCart() {
        // getting the values from our views
        val name = drinnkName.text.toString()
        val price = coffeePrice.text.toString()
        val quantity = quantitynumber.text.toString()

        val values = ContentValues().apply {
            put(OrderContract.OrderEntry.COLUMN_NAME, name)
            put(OrderContract.OrderEntry.COLUMN_PRICE, price)
            put(OrderContract.OrderEntry.COLUMN_QUANTITY, quantity)

            if (addExtraCream.isChecked) {
                put(OrderContract.OrderEntry.COLUMN_CREAM, "Has Cream: YES")
            } else {
                put(OrderContract.OrderEntry.COLUMN_CREAM, "Has Cream: NO")
            }

            if (addToppings.isChecked) {
                put(OrderContract.OrderEntry.COLUMN_HASTOPPING, "Has Toppings: YES")
            } else {
                put(OrderContract.OrderEntry.COLUMN_HASTOPPING, "Has Toppings: NO")
            }
        }

        if (mCurrentCartUri == null) {
            val newUri = contentResolver.insert(OrderContract.OrderEntry.CONTENT_URI, values)
            if (newUri == null) {
                Toast.makeText(this@JsakeActivity, "Failed to add to Cart", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this@JsakeActivity, "Success adding to Cart", Toast.LENGTH_SHORT).show()
            }
        }

        hasAllRequiredValues = true
    }

    private fun calculatePrice(addExtraCream: CheckBox, addToppings: CheckBox): Int {
        var basePrice = 5

        if (addExtraCream.isChecked) {
            // add the cream cost $2
            basePrice += 2
        }

        if (addToppings.isChecked) {
            // topping cost is $3
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

        return CursorLoader(this, mCurrentCartUri, projection, null, null, null)
    }

    override fun onLoadFinished(loader: Loader<Cursor>, cursor: Cursor?) {
        if (cursor == null || cursor.count < 1) {
            return
        }

        if (cursor.moveToFirst()) {
            val nameIndex = cursor.getColumnIndex(OrderContract.OrderEntry.COLUMN_NAME)
            val priceIndex = cursor.getColumnIndex(OrderContract.OrderEntry.COLUMN_PRICE)
            val quantityIndex = cursor.getColumnIndex(OrderContract.OrderEntry.COLUMN_QUANTITY)

            val nameofdrink = cursor.getString(nameIndex)
            val priceofdrink = cursor.getString(priceIndex)
            val quantityofdrink = cursor.getString(quantityIndex)

            drinnkName.text = nameofdrink
            coffeePrice.text = priceofdrink
            quantitynumber.text = quantityofdrink
        }
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {
        drinnkName.text = ""
        coffeePrice.text = ""
        quantitynumber.text = ""
    }

    companion object {
        private const val LOADER_ID = 1
    }
}
