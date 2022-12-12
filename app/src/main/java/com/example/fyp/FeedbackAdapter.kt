package com.example.fyp

import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fyp.Entity.Cafeteria
import com.example.fyp.Entity.Ewallet
import com.example.fyp.Entity.Feedback
import com.example.fyp.Entity.User
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.io.File

class FeedbackAdapter (private val cafeteriaList: ArrayList<Feedback>) : RecyclerView.Adapter<FeedbackAdapter.CafeteriaViewHolder>(){
    var onItemClick : ((Feedback) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CafeteriaViewHolder {
        val itemHolder = LayoutInflater.from(parent.context).inflate(R.layout.layout_feedback, parent, false)
        return CafeteriaViewHolder(itemHolder)

    }

    override fun getItemCount(): Int {
        return cafeteriaList.size

    }

    override fun onBindViewHolder(holder: CafeteriaViewHolder, position: Int) {
        var cafeteria: Feedback = cafeteriaList[position]
        var userObj:User

        var db : FirebaseFirestore = FirebaseFirestore.getInstance()
        db.collection("user")
            .whereEqualTo("id", cafeteria.customerID)
            .get()
            .addOnSuccessListener { documentssx ->

                for (documentsssx in documentssx) {
                    userObj = documentsssx.toObject(User::class.java)
                    holder.feedbackName.text = userObj.name
                }


            }

        holder.feedbackRatin.rating = cafeteria.rating.toString().toFloat()
        holder.feedbackDescrip.text = cafeteria.description


    }

    public class CafeteriaViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var feedbackName = itemView.findViewById<TextView>(R.id.feedbackName)
        var feedbackRatin = itemView.findViewById<RatingBar>(R.id.feedbackRatin)
        var feedbackDescrip = itemView.findViewById<TextView>(R.id.feedbackDescrip)



    }
}