package com.example.fyp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fyp.Entity.Cafeteria
import com.example.fyp.Entity.Food
import com.example.fyp.Entity.Order
import com.example.fyp.Entity.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class User_OrderHistory : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var foodArrayList: ArrayList<Order>
    private lateinit var foodAdapter: OrderAdapter
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var userObj: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_order_history)


        userObj = intent.getParcelableExtra<User>("userObj")!!


        if (userObj != null) {
            recyclerView = findViewById(R.id.rvOrderHistory)
            recyclerView.layoutManager = LinearLayoutManager(this)
            recyclerView.setHasFixedSize(true)
            foodArrayList = arrayListOf()

            foodAdapter = OrderAdapter(foodArrayList)

            recyclerView.adapter = foodAdapter

            setDataInList()


            foodAdapter.onItemClick = {
                val intent = Intent(this, user_OrderComplete::class.java)
                intent.putExtra("order", it.orderID)
                startActivity(intent)
            }
        }







    }

    private fun setDataInList() {


        db = FirebaseFirestore.getInstance()
        db.collection("order").addSnapshotListener(object : EventListener<QuerySnapshot> {

            override fun onEvent(
                value: QuerySnapshot?,
                error: FirebaseFirestoreException?
            ) {
                if (error != null) {
                    Log.e("Firestore Error", error.message.toString())
                    return

                }


                for (dc: DocumentChange in value?.documentChanges!!) {

                    if (dc.type == DocumentChange.Type.ADDED) {


                        foodArrayList.add((dc.document.toObject(Order::class.java)))
                        if (foodArrayList.get(foodArrayList.size - 1).userID != userObj.id) {
                            foodArrayList.removeAt(foodArrayList.size - 1)
                        }
                    }
                }
                foodArrayList.sortByDescending { list -> list.orderID }
/*
                tempArrayList.addAll(eventArrayList)
*/
                foodAdapter.notifyDataSetChanged()

            }
        })
    }
}

