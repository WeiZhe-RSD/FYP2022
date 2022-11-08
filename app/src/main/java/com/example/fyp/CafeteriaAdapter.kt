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
import com.google.firebase.storage.FirebaseStorage
import java.io.File

class CafeteriaAdapter (private val cafeteriaList: ArrayList<Cafeteria>) : RecyclerView.Adapter<CafeteriaAdapter.CafeteriaViewHolder>(){
    var onItemClick : ((Cafeteria) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CafeteriaViewHolder {
        val itemHolder = LayoutInflater.from(parent.context).inflate(R.layout.layout_cafeteria, parent, false)
        return CafeteriaViewHolder(itemHolder)

    }

    override fun getItemCount(): Int {
        return cafeteriaList.size

    }

    override fun onBindViewHolder(holder: CafeteriaViewHolder, position: Int) {
        var cafeteria: Cafeteria = cafeteriaList[position]

        var imgName = cafeteria.image.toString()
        val storageRef = FirebaseStorage.getInstance().reference.child("cafeteriaimg/$imgName")
        val localfile = File.createTempFile("tempImage", "png")

        storageRef.getFile(localfile).addOnSuccessListener {

            val bitmap = BitmapFactory.decodeFile(localfile.absolutePath)
            holder.imgcafeteria.setImageBitmap(bitmap)
        }

        holder.tvCafeteriaName.text=cafeteria.name

        holder.itemView.findViewById<Button>(R.id.btnSelect).setOnClickListener {
            onItemClick?.invoke(cafeteria)
        }

    }

    public class CafeteriaViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var imgcafeteria = itemView.findViewById<ImageView>(R.id.imgItem)
        var tvCafeteriaName = itemView.findViewById<TextView>(R.id.tvCafeteriaName)


    }
}