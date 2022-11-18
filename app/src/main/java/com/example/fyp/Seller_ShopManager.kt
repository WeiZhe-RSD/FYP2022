package com.example.fyp

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import com.example.fyp.Entity.Cafeteria
import com.example.fyp.Entity.FoodStall
import com.example.fyp.Entity.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Seller_ShopManager : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seller_shop_manager)

        val btnManageFoodItems = findViewById<Button>(R.id.btnManageFI)
        val btnManageFoodStall = findViewById<Button>(R.id.btnManageOH)
        val tvShopName = findViewById<TextView>(R.id.tvShopName)

        ///////////////////////////////////////////////////////////   get current user
        val userObj  = intent.getParcelableExtra<User>("userObj")

        if(userObj!= null) {
            Log.i("inherit Success", userObj.email.toString())
        }

        //////////////////////////////////////////////////////////


        ///////////////////////////////////////////////////////////// check this user own which food stall
        var foodstallObj: FoodStall ?= null

        db = FirebaseFirestore.getInstance()
        if (userObj != null) {
            db.collection("foodstall")
                .whereEqualTo("sellerID", userObj.id)
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        foodstallObj = document.toObject(FoodStall::class.java)
                        Log.i("madafakaaaaaaaaaaaaaaa", foodstallObj.toString())
                    }
                    /////////////////////  this foodstallObj only exist here,  can try if u can get the data outside this addOnSuccessListioner
                    tvShopName.text = foodstallObj!!.name
                    /////////////////////
                }
                .addOnFailureListener { exception ->
                    Log.w(TAG, "Error getting documents: ", exception)
                }
        }



        btnManageFoodStall.setOnClickListener(){
            val intent = Intent(this@Seller_ShopManager, Seller_ManageFoodStall::class.java)
            startActivity(intent)
        }

        btnManageFoodItems.setOnClickListener(){
            val intent = Intent(this@Seller_ShopManager, Seller_ManageMenuItems::class.java)
            startActivity(intent)
        }

    }

}