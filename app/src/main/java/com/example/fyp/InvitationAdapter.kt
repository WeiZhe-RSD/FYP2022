package com.example.fyp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fyp.Entity.EatTogehter
import com.example.fyp.Entity.User
import com.google.firebase.firestore.FirebaseFirestore

class InvitationAdapter (private val cafeteriaList: ArrayList<EatTogehter>) : RecyclerView.Adapter<InvitationAdapter.CafeteriaViewHolder>(){
    var onItemClick : ((EatTogehter) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CafeteriaViewHolder {
        val itemHolder = LayoutInflater.from(parent.context).inflate(R.layout.layout_invitations, parent, false)
        return CafeteriaViewHolder(itemHolder)

    }

    override fun getItemCount(): Int {
        return cafeteriaList.size

    }

    override fun onBindViewHolder(holder: CafeteriaViewHolder, position: Int) {
        var cafeteria: EatTogehter = cafeteriaList[position]

        var email:String = ""
        var db = FirebaseFirestore.getInstance()
        db.collection("user")
            .whereEqualTo("id", cafeteria.invitedID)
            .get()
            .addOnSuccessListener { documentss ->

                for (documentsss in documentss) {
                    holder.tvEatTogetherNAme.text = documentsss.toObject(User::class.java).email.toString()
                }
            }


        holder.tvEatTogetherDate.text=cafeteria.dateCreated

        holder.itemView.findViewById<Button>(R.id.btnViewInvitation).setOnClickListener {
            onItemClick?.invoke(cafeteria)
        }

    }

    public class CafeteriaViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var tvEatTogetherNAme = itemView.findViewById<TextView>(R.id.tvInvitationnNAme)
        var tvEatTogetherDate = itemView.findViewById<TextView>(R.id.tvEatTogetherDate)


    }
}