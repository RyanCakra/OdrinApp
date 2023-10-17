package com.example.odrinapp

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class OrderAdapter(private val context: Context, private val modelList: List<Model>) :
    RecyclerView.Adapter<OrderAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.listitem, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val nameofDrink = modelList[position].nDrinkName
        val descriptionofdrink = modelList[position].nDrinkDetail
        val images = modelList[position].nDrinkPhoto

        holder.nDrinkName.text = nameofDrink
        holder.nDrinkDescription.text = descriptionofdrink
        holder.imageView.setImageResource(images)
    }

    override fun getItemCount(): Int {
        return modelList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        val nDrinkName: TextView = itemView.findViewById(R.id.coffeeName)
        val nDrinkDescription: TextView = itemView.findViewById(R.id.description)
        val imageView: ImageView = itemView.findViewById(R.id.coffeeImage)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            val position = adapterPosition

            if (position == 0) {
                val intent = Intent(context, InfoActivity::class.java)
                context.startActivity(intent)
            }

            if (position == 1) {
                val intent2 = Intent(context, ImaraActivity::class.java)
                context.startActivity(intent2)
            }

            if (position == 2) {
                val intent3 = Intent(context, IresrettoActivity::class.java)
                context.startActivity(intent3)
            }
            if (position == 3) {
                val intent4 = Intent(context, SchocoActivity::class.java)
                context.startActivity(intent4)
            }
            if (position == 4) {
                val intent5 = Intent(context, SmangoActivity::class.java)
                context.startActivity(intent5)
            }
            if (position == 5) {
                val intent6 = Intent(context, SsangriaActivity::class.java)
                context.startActivity(intent6)
            }
            if (position == 6) {
                val intent7 = Intent(context, JaojiruActivity::class.java)
                context.startActivity(intent7)
            }
            if (position == 7) {
                val intent8 = Intent(context, JshochuActivity::class.java)
                context.startActivity(intent8)
            }
            if (position == 8) {
                val intent9 = Intent(context, JsakeActivity::class.java)
                context.startActivity(intent9)
            }
        }
    }
}
