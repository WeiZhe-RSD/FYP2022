package com.example.fyp

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fyp.Entity.Cafeteria
import com.example.fyp.Entity.Order
import com.google.firebase.storage.FirebaseStorage
import java.io.File

class OrderAdapter (private val cafeteriaList: ArrayList<Order>) : RecyclerView.Adapter<OrderAdapter.CafeteriaViewHolder>(){
    var onItemClick : ((Order) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CafeteriaViewHolder {
        val itemHolder = LayoutInflater.from(parent.context).inflate(R.layout.layout_orderhistory, parent, false)
        return CafeteriaViewHolder(itemHolder)

    }

    override fun getItemCount(): Int {
        return cafeteriaList.size

    }

    override fun onBindViewHolder(holder: CafeteriaViewHolder, position: Int) {
        var cafeteria: Order = cafeteriaList[position]


        holder.tvCafeteriaName.text=cafeteria.date

        holder.itemView.findViewById<Button>(R.id.btnHistorySelect).setOnClickListener {
            onItemClick?.invoke(cafeteria)
        }

    }

    public class CafeteriaViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var tvCafeteriaName = itemView.findViewById<TextView>(R.id.tvHisotyDate)


    }
}