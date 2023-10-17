package com.example.odrinapp

import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.widget.Button
import android.widget.ListView
import android.widget.RadioGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.loader.app.LoaderManager
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader
import com.example.odrinapp.Database.OrderContract

class SummaryActivity : AppCompatActivity(), LoaderManager.LoaderCallbacks<Cursor> {

    private lateinit var mAdapter: CartAdapter
    private val LOADER = 0
    private lateinit var leftPaymentRadioGroup: RadioGroup
    private lateinit var rightPaymentRadioGroup: RadioGroup

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_summary)

        val clearDataButton = findViewById<Button>(R.id.clearthedatabase)
        clearDataButton.setOnClickListener {
            val rowsDeleted = contentResolver.delete(OrderContract.OrderEntry.CONTENT_URI, null, null)
            if (rowsDeleted > 0) {
                Toast.makeText(this, "Data cleared successfully", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Failed to clear data", Toast.LENGTH_SHORT).show()
            }
        }

        val confirmButton = findViewById<Button>(R.id.checkout)
        confirmButton.setOnClickListener {
            val selectedLeftRadioButtonId = leftPaymentRadioGroup.checkedRadioButtonId
            val selectedRightRadioButtonId = rightPaymentRadioGroup.checkedRadioButtonId

            if (selectedLeftRadioButtonId == -1 && selectedRightRadioButtonId == -1) {
                Toast.makeText(this, "Please select a payment method", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val selectedLeftRadioButton = findViewById<RadioButton>(selectedLeftRadioButtonId)
            val selectedRightRadioButton = findViewById<RadioButton>(selectedRightRadioButtonId)

            val selectedLeftPaymentMethod = selectedLeftRadioButton?.text.toString()
            val selectedRightPaymentMethod = selectedRightRadioButton?.text.toString()

            val paymentMethod = if (selectedLeftRadioButtonId != -1) {
                selectedLeftPaymentMethod
            } else {
                selectedRightPaymentMethod
            }

            Toast.makeText(this, "Order Successful with payment method: $paymentMethod", Toast.LENGTH_SHORT).show()
        }

        mAdapter = CartAdapter(this, null, 0)
        val listView = findViewById<ListView>(R.id.list)
        listView.adapter = mAdapter

        leftPaymentRadioGroup = findViewById(R.id.leftPaymentRadioGroup)
        rightPaymentRadioGroup = findViewById(R.id.rightPaymentRadioGroup)

        leftPaymentRadioGroup.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId != -1) {
                rightPaymentRadioGroup.clearCheck()
            }
        }

        rightPaymentRadioGroup.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId != -1) {
                leftPaymentRadioGroup.clearCheck()
            }
        }

        // Mendapatkan ID gambar dari Intent
        val imageId = intent.getIntExtra("selected_image_id", 0)
        mAdapter.setImageId(imageId) // Menetapkan ID gambar ke CartAdapter

        supportLoaderManager.restartLoader(LOADER, null, this)
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
        val projection = arrayOf(
            OrderContract.OrderEntry._ID,
            OrderContract.OrderEntry.COLUMN_NAME,
            OrderContract.OrderEntry.COLUMN_PRICE,
            OrderContract.OrderEntry.COLUMN_QUANTITY,
            OrderContract.OrderEntry.COLUMN_CREAM,
            OrderContract.OrderEntry.COLUMN_HASTOPPING,
            OrderContract.OrderEntry.COLUMN_IMAGE
        )

        return CursorLoader(
            this,
            OrderContract.OrderEntry.CONTENT_URI,
            projection,
            null,
            null,
            null
        )
    }

    override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor?) {
        mAdapter.swapCursor(data)
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {
        mAdapter.swapCursor(null)
    }
}
