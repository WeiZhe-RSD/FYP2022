package com.example.fyp

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fyp.Entity.CartDetail
import com.example.fyp.Entity.Food
import com.example.fyp.Entity.Order
import com.google.firebase.firestore.*

class Seller_ManageOrderDetails : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var OrderReceivedArrayList: ArrayList<Order>
    private lateinit var OrderReceivedAdapter: OrderReceivedAdapter
    private lateinit var db: FirebaseFirestore
    private var userObj: String? = ""


    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seller_manage_order_details)

        val btnPendingPay = findViewById<Button>(R.id.btnPendingPay)

        btnPendingPay.setOnClickListener(){
            val intent = Intent(this, Seller_ManagePendingPay::class.java)
            intent.putExtra("stallname", userObj)
            startActivity(intent)
        }

        getCFoodStallID()

        recyclerView = findViewById(R.id.rvOrderStatus)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        OrderReceivedArrayList = arrayListOf()

        OrderReceivedAdapter = OrderReceivedAdapter(OrderReceivedArrayList, applicationContext)
        recyclerView.adapter = OrderReceivedAdapter

        setDataInList()
    }

    private fun setDataInList() {
        db = FirebaseFirestore.getInstance()
        db.collection("order").whereEqualTo("foodstallID", userObj)
            .addSnapshotListener(object : EventListener<QuerySnapshot> {


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
                            OrderReceivedArrayList.add((dc.document.toObject(Order::class.java)))
                            if (OrderReceivedArrayList[OrderReceivedArrayList.size - 1].status == "Inactive") {
                                OrderReceivedArrayList.removeAt(OrderReceivedArrayList.size - 1)
                            }
                        }
                    }
                    OrderReceivedAdapter.notifyDataSetChanged()
                }
            })


    }

    private fun getCFoodStallID() {
        val intent = intent
        userObj = intent.getStringExtra("foodStall")
    }


}