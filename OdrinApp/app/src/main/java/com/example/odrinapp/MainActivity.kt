package com.example.odrinapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.odrinapp.Model
import com.example.odrinapp.OrderAdapter
import com.example.odrinapp.R

class MainActivity : AppCompatActivity() {

    private lateinit var modelList: List<Model>
    private lateinit var recyclerView: RecyclerView
    private lateinit var mAdapter: OrderAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Membuat daftar model
        modelList = listOf(
            // Italian
            Model("Strawberry Granita", getString(R.string.Granita), R.drawable.igranita),
            Model("Maraschino", getString(R.string.Maraschino), R.drawable.imaraschino),
            Model("Risretto", getString(R.string.Ristretto), R.drawable.irisretto),
            // Spanyol
            Model("Chocolate Caliente", getString(R.string.ChocoCaliente), R.drawable.schococ),
            Model("Mango Agua Fresca", getString(R.string.MangoAguaF), R.drawable.smango),
            Model("Sangria", getString(R.string.Sangria), R.drawable.ssangria),
            // Jepang
            Model("Aojiru", getString(R.string.Aojiru), R.drawable.jaojiru),
            Model("Shochu", getString(R.string.Shochu), R.drawable.jshochu),
            Model("Sake", getString(R.string.Sake), R.drawable.jsake)
        )

        // Inisialisasi RecyclerView
        recyclerView = findViewById(R.id.recyclerView)

        // Mengatur GridLayoutManager dengan 2 kolom
        val layoutManager = GridLayoutManager(this, 2)
        recyclerView.layoutManager = layoutManager

        // Mengatur adapter
        mAdapter = OrderAdapter(this, modelList)
        recyclerView.adapter = mAdapter
    }
}
