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
import com.example.fyp.Entity.Payment
import com.google.firebase.storage.FirebaseStorage
import java.io.File

class PaymentAdapter (private val cafeteriaList: ArrayList<Payment>) : RecyclerView.Adapter<PaymentAdapter.CafeteriaViewHolder>(){
    var onItemClick : ((Payment) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CafeteriaViewHolder {
        val itemHolder = LayoutInflater.from(parent.context).inflate(R.layout.layout_orderhistory, parent, false)
        return CafeteriaViewHolder(itemHolder)

    }

    override fun getItemCount(): Int {
        return cafeteriaList.size

    }

    override fun onBindViewHolder(holder: CafeteriaViewHolder, position: Int) {
        var cafeteria: Payment = cafeteriaList[position]


        holder.tvHisotyName.text=cafeteria.paymentID
        holder.tvHisotyDate.text=cafeteria.date


        holder.itemView.findViewById<Button>(R.id.btnHistorySelect).setOnClickListener {
            onItemClick?.invoke(cafeteria)
        }

    }

    class CafeteriaViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var tvHisotyName = itemView.findViewById<TextView>(R.id.tvHisotyName)
        var tvHisotyDate = itemView.findViewById<TextView>(R.id.tvHisotyDate)


    }
}