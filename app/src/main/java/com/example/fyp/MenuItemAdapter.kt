package com.example.fyp

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.fyp.Entity.Food
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.io.File

class MenuItemAdapter(private val foodList: ArrayList<Food>) : RecyclerView.Adapter<MenuItemAdapter.MenuItemViewHolder>(){
    private var db = FirebaseFirestore.getInstance();
    var onItemClick : ((Food) -> Unit)? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuItemViewHolder {
        val itemHolder = LayoutInflater.from(parent.context).inflate(R.layout.layout_menuitems, parent, false)
        return MenuItemViewHolder(itemHolder)
    }

    override fun getItemCount(): Int {
        return foodList.size
    }

    @SuppressLint("NotifyDataSetChanged")
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

        holder.itemView.findViewById<Button>(R.id.btnDelete).setOnClickListener {
            //Toast.makeText(it.context, """ ${food.name} """, Toast.LENGTH_SHORT).show()

            foodList.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, foodList.size)
            notifyDataSetChanged()

            db.collection("food").document(food.name.toString()).delete()
                .addOnSuccessListener {
                    Toast.makeText(holder.itemView.context, "This " + food.name.toString() + " has been deleted successfully!",Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { e -> Log.w(TAG, "Error deleting food", e) }

        }

        holder.itemView.findViewById<Button>(R.id.btnEdit).setOnClickListener {
            val intent = Intent(it.context, Seller_EditItems::class.java)
            intent.putExtra("foodName", food.name.toString())
            it.context.startActivity(intent);
        }

    }


    public class MenuItemViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var imgItem = itemView.findViewById<ImageView>(R.id.imgItem)
        var tvItemName = itemView.findViewById<TextView>(R.id.tvItemName)
    }
}