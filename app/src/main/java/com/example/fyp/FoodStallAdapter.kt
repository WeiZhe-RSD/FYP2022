package com.example.fyp

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fyp.Entity.FoodStall
import com.google.firebase.storage.FirebaseStorage
import java.io.File


class FoodStallAdapter (private val foodstallList: ArrayList<FoodStall>) : RecyclerView.Adapter<FoodStallAdapter.FoodStallViewHolder>(){
    var onItemClick : ((FoodStall) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodStallViewHolder {
        val itemHolder = LayoutInflater.from(parent.context).inflate(R.layout.layout_cafeteria, parent, false)
        return FoodStallViewHolder(itemHolder)

    }

    override fun getItemCount(): Int {
        return foodstallList.size

    }

    override fun onBindViewHolder(holder: FoodStallViewHolder, position: Int) {
        var foodstall: FoodStall = foodstallList[position]

        var imgName = foodstall.image.toString()
        val storageRef = FirebaseStorage.getInstance().reference.child("foodstallimg/$imgName")
        val localfile = File.createTempFile("tempImage", "png")

        storageRef.getFile(localfile).addOnSuccessListener {

            val bitmap = BitmapFactory.decodeFile(localfile.absolutePath)
            holder.imgcafeteria.setImageBitmap(bitmap)
        }

        holder.tvCafeteriaName.text=foodstall.name

        holder.itemView.findViewById<Button>(R.id.btnSelect).setOnClickListener {
            onItemClick?.invoke(foodstall)
        }

    }

    public class FoodStallViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var imgcafeteria = itemView.findViewById<ImageView>(R.id.imgCafeteria)
        var tvCafeteriaName = itemView.findViewById<TextView>(R.id.tvCafeteriaName)


    }
}