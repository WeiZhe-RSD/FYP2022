package com.example.fyp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.example.fyp.Entity.EatTogehter
import com.google.firebase.firestore.FirebaseFirestore

class User_InviteAccept : AppCompatActivity() {
    private lateinit var db: FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_invite_accept)

        db = FirebaseFirestore.getInstance()

        var et = intent.getParcelableExtra<EatTogehter>("invite")

        if(et!=null){

            val tvInviteSender = findViewById<TextView>(R.id.tvInviteSender1)
            val tvInviteReveiver = findViewById<TextView>(R.id.tvInviteReveiver1)
            val tvInviteDate = findViewById<TextView>(R.id.tvInviteDate1)
            val tvInviteVenue = findViewById<TextView>(R.id.tvInviteVenue1)
            val tvInviteStatus = findViewById<TextView>(R.id.tvInviteStatus1)
            val tvInviteDateCreate = findViewById<TextView>(R.id.tvInviteDateCreate1)

            tvInviteSender.text = et.customerID.toString()
            tvInviteReveiver.text = et.invitedID.toString()
            tvInviteDate.text = et.dateMeet.toString()
            tvInviteVenue.text = et.venue.toString()
            tvInviteStatus.text = et.status.toString()
            tvInviteDateCreate.text = et.dateCreated.toString()

            val btnRejectInvi = findViewById<Button>(R.id.btnRejectInvi)
            val btnAccpetInvi = findViewById<Button>(R.id.btnAccpetInvi)

            btnRejectInvi.setOnClickListener(){
                val washingtonRef = db.collection("eatTogether").document(et.eatTogehterID.toString())
                washingtonRef
                    .update("status", "Rejected")

                et.status = "Rejected"

                val intent = Intent(this, User_InviteAccept::class.java)
                intent.putExtra("invite", et)
                startActivity(intent)
                finish()
            }

            btnAccpetInvi.setOnClickListener(){
                val washingtonRef = db.collection("eatTogether").document(et.eatTogehterID.toString())
                washingtonRef
                    .update("status", "Accepted")

                et.status = "Accepted"

                val intent = Intent(this, User_InviteAccept::class.java)
                intent.putExtra("invite", et)
                startActivity(intent)
                finish()
            }


        }



    }
}