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

class FoodAdapter (private val foodList: ArrayList<Food>) : RecyclerView.Adapter<FoodAdapter.FoodViewHolder>(){
    var onItemClick : ((Food) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
        val itemHolder = LayoutInflater.from(parent.context).inflate(R.layout.layout_menu, parent, false)
        return FoodViewHolder(itemHolder)

    }

    override fun getItemCount(): Int {
        return foodList.size

    }

    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
        var food: Food = foodList[position]

        var imgName = food.image.toString()
        val storageRef = FirebaseStorage.getInstance().reference.child("foodimg/$imgName")
        val localfile = File.createTempFile("tempImage", "png")

        storageRef.getFile(localfile).addOnSuccessListener {

            val bitmap = BitmapFactory.decodeFile(localfile.absolutePath)
            holder.imgFood.setImageBitmap(bitmap)
        }

        holder.tvFoodName.text=food.name

        holder.itemView.findViewById<Button>(R.id.btnSelects).setOnClickListener {
            onItemClick?.invoke(food)
        }

    }

    public class FoodViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var imgFood = itemView.findViewById<ImageView>(R.id.imgFood)
        var tvFoodName = itemView.findViewById<TextView>(R.id.tvFoodName)


    }
}