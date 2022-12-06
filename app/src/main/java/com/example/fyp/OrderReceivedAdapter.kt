package com.example.fyp

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fyp.Entity.CartDetail
import com.example.fyp.Entity.Order

class OrderReceivedAdapter(private val orders: ArrayList<Order>, context: Context) : RecyclerView.Adapter<OrderReceivedAdapter.OrderReceivedViewHolder>(){
    val context: Context = context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderReceivedViewHolder {
        val itemHolder = LayoutInflater.from(parent.context).inflate(R.layout.orderreceive, parent,false)
        return OrderReceivedViewHolder(itemHolder)
    }

    override fun onBindViewHolder(holder: OrderReceivedViewHolder, position: Int) {
        val orderList: Order = orders[position]

        holder.orderID.text = orderList.orderID
        holder.orderStatus.text = orderList.status

        //Go to specific order page from here based on ONpressed

        holder.itemView.setOnClickListener(object: View.OnClickListener {
            override fun onClick(p0: View?) {
                val intent = Intent(context, Seller_ManageSpecificOrder::class.java)

                var position : String? = ""
                var parseOrderedFoodId : String? = ""

                position = holder.adapterPosition.toString()
                intent.putExtra("orderID",orderList.orderID)
                intent.putExtra("orderDetailID",orderList.orderDetailID)
                intent.putExtra("date",orderList.date)
                intent.putExtra("payment",orderList.paymentID)
                intent.putExtra("status",orderList.status)
                intent.putExtra("ttlPrice",orderList.ttlPrice)
                intent.putExtra("foodstallID",orderList.foodstallID)
                intent.putExtra("userID",orderList.userID)

                p0?.context?.startActivity(intent)

                (p0?.context as Activity).finish()
            }
        })

    }

    override fun getItemCount(): Int {
        return orders.size
    }

    public class OrderReceivedViewHolder(itemHolder: View): RecyclerView.ViewHolder(itemHolder){
        var orderID = itemHolder.findViewById<TextView>(R.id.orderID)
        var orderStatus = itemHolder.findViewById<TextView>(R.id.orderStatus)
    }
}