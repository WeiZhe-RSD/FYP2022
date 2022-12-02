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
import com.example.fyp.Entity.EwalletTransaction
import com.google.firebase.storage.FirebaseStorage
import java.io.File

class TransactionAdapter (private val cafeteriaList: ArrayList<EwalletTransaction>) : RecyclerView.Adapter<TransactionAdapter.CafeteriaViewHolder>() {
    var onItemClick: ((EwalletTransaction) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CafeteriaViewHolder {
        val itemHolder =
            LayoutInflater.from(parent.context).inflate(R.layout.layout_transaction, parent, false)
        return CafeteriaViewHolder(itemHolder)

    }

    override fun getItemCount(): Int {
        return cafeteriaList.size

    }

    override fun onBindViewHolder(holder: CafeteriaViewHolder, position: Int) {
        var cafeteria: EwalletTransaction = cafeteriaList[position]



        if(cafeteria.type == "Payment"){
            holder.tvTranName.text = "Transfer to " + cafeteria.to.toString()
        }else if(cafeteria.type == "Receive"){
            holder.tvTranName.text = "Receive from " + cafeteria.to.toString()
        }

        holder.tvTranType.text = cafeteria.type
        if(cafeteria.type == "Payment"){
            holder.tvTranAmount.text = "+RM" +cafeteria.amount.toString()

        }else{
            holder.tvTranAmount.text = "-RM" +cafeteria.amount.toString()
        }
        holder.tvTranDate.text = cafeteria.date

        /*holder.itemView.setOnClickListener {
            onItemClick?.invoke(cafeteria)
        }*/

    }

    public class CafeteriaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvTranName = itemView.findViewById<TextView>(R.id.tvTranName)
        var tvTranType = itemView.findViewById<TextView>(R.id.tvTranType)
        var tvTranAmount = itemView.findViewById<TextView>(R.id.tvTranAmount)
        var tvTranDate = itemView.findViewById<TextView>(R.id.tvTranDate)


    }
}