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
import com.example.fyp.Entity.User
import com.google.firebase.storage.FirebaseStorage
import java.io.File

class EatTogetherAdapter (private val cafeteriaList: ArrayList<User>) : RecyclerView.Adapter<EatTogetherAdapter.CafeteriaViewHolder>(){
    var onItemClick : ((User) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CafeteriaViewHolder {
        val itemHolder = LayoutInflater.from(parent.context).inflate(R.layout.layout_eattogether, parent, false)
        return CafeteriaViewHolder(itemHolder)

    }

    override fun getItemCount(): Int {
        return cafeteriaList.size

    }

    override fun onBindViewHolder(holder: CafeteriaViewHolder, position: Int) {
        var cafeteria: User = cafeteriaList[position]


        holder.tvEatTogetherNAme.text=cafeteria.email.toString()

        holder.itemView.findViewById<Button>(R.id.btnInvite).setOnClickListener {
            onItemClick?.invoke(cafeteria)
        }

    }

    public class CafeteriaViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var tvEatTogetherNAme = itemView.findViewById<TextView>(R.id.tvEatTogetherNAme)


    }
}