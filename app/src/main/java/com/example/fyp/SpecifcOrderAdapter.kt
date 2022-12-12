package com.example.fyp

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fyp.Entity.OrderContentDetail
import com.example.fyp.Entity.OrderDetail

class SpecifcOrderAdapter (private val specificOrder: ArrayList<OrderContentDetail>) : RecyclerView.Adapter<SpecifcOrderAdapter.SpecifcOrderViewHolder>(){

    public class SpecifcOrderViewHolder(itemHolder: View): RecyclerView.ViewHolder(itemHolder) {
        var foodid = itemHolder.findViewById<TextView>(R.id.orderID)
        var orderquantity = itemHolder.findViewById<TextView>(R.id.orderQuantity)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpecifcOrderViewHolder {
        val itemHolder = LayoutInflater.from(parent.context).inflate(R.layout.specific_order_rec,parent, false)
        return SpecifcOrderViewHolder(itemHolder)
    }

    override fun onBindViewHolder(holder: SpecifcOrderViewHolder, position: Int) {
        val specificOrd: OrderContentDetail = specificOrder[position]

        holder.foodid.text = specificOrd.foodID
        holder.orderquantity.text = specificOrd.quantity.toString()
    }

    override fun getItemCount(): Int {
        return specificOrder.size
    }
}