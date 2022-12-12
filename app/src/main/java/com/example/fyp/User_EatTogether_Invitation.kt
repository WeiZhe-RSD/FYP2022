package com.example.fyp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fyp.Entity.Cafeteria
import com.example.fyp.Entity.CartDetail
import com.example.fyp.Entity.EatTogehter
import com.example.fyp.Entity.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class User_EatTogether_Invitation : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var cafeteriaArrayList: ArrayList<EatTogehter>
    private lateinit var cafeteriaAdapter:InvitationAdapter
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    private lateinit var userObj:User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_eat_together_invitation)


        val btnViewRecei = findViewById<Button>(R.id.btnViewRecei)

        recyclerView = findViewById(R.id.rvInvitationSent)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        cafeteriaArrayList = arrayListOf()

        cafeteriaAdapter = InvitationAdapter(cafeteriaArrayList)

        recyclerView.adapter = cafeteriaAdapter

        setDataInList()

        cafeteriaAdapter.onItemClick = {
            val intent = Intent(this, User_InvitationDetail::class.java)
            intent.putExtra("invite", it)
            startActivity(intent)
        }

        btnViewRecei.setOnClickListener(){
            val intent = Intent(this, User_EatTogether_InviteMe::class.java)
            startActivity(intent)
        }



    }


    private fun setDataInList(){
        auth = FirebaseAuth.getInstance()

        val user = auth.currentUser
        db = Firebase.firestore

        val userRef = db.collection("user").document(user?.email.toString())
        userRef.get().addOnSuccessListener {
            if (it != null) {
                userObj = it.toObject(User::class.java)!!


                db = FirebaseFirestore.getInstance()
                db.collection("eatTogether")
                    .whereEqualTo("customerID", userObj.id)
                    .get()
                    .addOnSuccessListener { documentss ->

                        for (documentsss in documentss) {
                            cafeteriaArrayList.add(documentsss.toObject(EatTogehter::class.java))
                        }
                        cafeteriaArrayList.sortByDescending { "dateCreated" }


                        cafeteriaAdapter.notifyDataSetChanged()
                    }
            }
        }






    }

}