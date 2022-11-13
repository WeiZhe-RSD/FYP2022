package com.example.fyp

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fyp.Entity.Food
import com.google.firebase.storage.FirebaseStorage
import java.io.File

class MenuItemAdapter (private val foodList: ArrayList<Food>) : RecyclerView.Adapter<MenuItemAdapter.MenuItemViewHolder>(){
    var onItemClick : ((Food) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuItemViewHolder {
        val itemHolder = LayoutInflater.from(parent.context).inflate(R.layout.layout_menuitems, parent, false)
        return MenuItemViewHolder(itemHolder)
    }

    override fun getItemCount(): Int {
        return foodList.size
    }

    override fun onBindViewHolder(holder: MenuItemViewHolder, position: Int) {
        val food: Food = foodList[position]

        val imgName = food.image.toString()
        val storageRef = FirebaseStorage.getInstance().reference.child("foodimg/$imgName")
        val localfile = File.createTempFile("tempImage", "png")

        storageRef.getFile(localfile).addOnSuccessListener {

            val bitmap = BitmapFactory.decodeFile(localfile.absolutePath)
            holder.imgItem.setImageBitmap(bitmap)
        }

        holder.tvItemName.text = food.name

        holder.itemView.findViewById<Button>(R.id.btnEdit).setOnClickListener {
            onItemClick?.invoke(food)
        }

    }

    public class MenuItemViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var imgItem = itemView.findViewById<ImageView>(R.id.imgItem)
        var tvItemName = itemView.findViewById<TextView>(R.id.tvItemName)
    }
}