package com.example.fyp

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fyp.Entity.Food
import com.example.fyp.Entity.Order
import com.example.fyp.Entity.Report

class ReportAdapter (private val dataList: ArrayList<Report>) : RecyclerView.Adapter<ReportAdapter.ReportViewHolder>(){
    var onItemClick : ((Food) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReportViewHolder {
        val itemHolder = LayoutInflater.from(parent.context).inflate(R.layout.layout_report_row, parent, false)
        return ReportViewHolder(itemHolder)

    }

    override fun getItemCount(): Int {
        return dataList.size

    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ReportViewHolder, position: Int) {
        val data: Report = dataList[position]


        holder.tvFoodName.text = data.foodName
        holder.tvDate.text = data.date
        holder.tvQty.text = "${data.quantity}"
        holder.tvSubtotal.text = "RM ${data.subtotal}0"
    }

    class ReportViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var tvFoodName: TextView = itemView.findViewById(R.id.tvFoodName_report)
        var tvDate: TextView = itemView.findViewById(R.id.tvDate_report)
        var tvQty: TextView = itemView.findViewById(R.id.tvQty_report)
        var tvSubtotal: TextView = itemView.findViewById(R.id.tvSubtotal_report)
    }
}