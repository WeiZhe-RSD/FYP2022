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
import com.example.fyp.Entity.CartDetail
import com.google.firebase.storage.FirebaseStorage
import java.io.File

class CartDetailAdapter (private val cafeteriaList: ArrayList<CartDetail>) : RecyclerView.Adapter<CartDetailAdapter.CafeteriaViewHolder>(){
    var onItemClick : ((CartDetail) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CafeteriaViewHolder {
        val itemHolder = LayoutInflater.from(parent.context).inflate(R.layout.layout_cart, parent, false)
        return CafeteriaViewHolder(itemHolder)

    }

    override fun getItemCount(): Int {
        return cafeteriaList.size

    }

    override fun onBindViewHolder(holder: CafeteriaViewHolder, position: Int) {
        var cafeteria: CartDetail = cafeteriaList[position]

        holder.tvCartPrice.text = "RM "+cafeteria.subtotal.toString()
        holder.tvCartQtt.text = cafeteria.quantity.toString()

        var imgName = cafeteria.name + ".png"
        val storageRef = FirebaseStorage.getInstance().reference.child("foodimg/$imgName")
        val localfile = File.createTempFile("tempImage", "png")

        storageRef.getFile(localfile).addOnSuccessListener {

            val bitmap = BitmapFactory.decodeFile(localfile.absolutePath)
            holder.imgcafeteria.setImageBitmap(bitmap)
        }

        holder.tvCafeteriaName.text=cafeteria.name

        holder.itemView.findViewById<Button>(R.id.btnCartEdit).setOnClickListener {
            onItemClick?.invoke(cafeteria)
        }

    }

    class CafeteriaViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var imgcafeteria = itemView.findViewById<ImageView>(R.id.imgCart)
        var tvCafeteriaName = itemView.findViewById<TextView>(R.id.tvCartName)
        var tvCartPrice = itemView.findViewById<TextView>(R.id.tvCartPrice)
        var tvCartQtt = itemView.findViewById<TextView>(R.id.tvCartQtt)

    }
}