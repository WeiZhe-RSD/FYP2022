package com.example.fyp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fyp.Entity.CartDetail

class OrderReceivedAdapter(private val cartList: ArrayList<CartDetail>) : RecyclerView.Adapter<OrderReceivedAdapter.OrderReceivedViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderReceivedViewHolder {
        val itemHolder = LayoutInflater.from(parent.context).inflate(R.layout.orderreceive, parent,false)
        return OrderReceivedViewHolder(itemHolder)
    }

    override fun onBindViewHolder(holder: OrderReceivedViewHolder, position: Int) {
        val orderList: CartDetail = cartList[position]

        holder.orderNumber.text = orderList.cartID
        holder.orderStatus.text = orderList.status
    }

    override fun getItemCount(): Int {
        return cartList.size
    }

    public class OrderReceivedViewHolder(itemHolder: View): RecyclerView.ViewHolder(itemHolder){
        var orderNumber = itemHolder.findViewById<TextView>(R.id.orderNumber)
        var orderStatus = itemHolder.findViewById<TextView>(R.id.orderStatus)
    }
}