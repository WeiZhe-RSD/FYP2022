package com.example.fyp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fyp.Entity.CartDetail
import com.example.fyp.Entity.Food
import com.google.firebase.firestore.*

class Seller_ManageOrderDetails : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var OrderReceivedArrayList: ArrayList<CartDetail>
    private lateinit var OrderReceivedAdapter: OrderReceivedAdapter
    private lateinit var db : FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seller_manage_order_details)

        recyclerView = findViewById(R.id.rvOrderStatus)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        OrderReceivedArrayList = arrayListOf()

        OrderReceivedAdapter = OrderReceivedAdapter(OrderReceivedArrayList)

        recyclerView.adapter = OrderReceivedAdapter

        setDataInList()
    }

    private fun setDataInList(){
        db = FirebaseFirestore.getInstance()
        db.collection("cartDetail").addSnapshotListener(object : EventListener<QuerySnapshot> {


            override fun onEvent(
                value: QuerySnapshot?,
                error: FirebaseFirestoreException?
            ){
                if(error != null){
                    Log.e("Firestore Error", error.message.toString())
                    return

                }

                for(dc : DocumentChange in value?.documentChanges!!){

                    if(dc.type == DocumentChange.Type.ADDED){
                        OrderReceivedArrayList.add((dc.document.toObject(CartDetail::class.java)))
                        if(OrderReceivedArrayList[OrderReceivedArrayList.size - 1].status == "Inactive"){
                            OrderReceivedArrayList.removeAt(OrderReceivedArrayList.size - 1)
                        }
                    }
                }
                OrderReceivedAdapter.notifyDataSetChanged()
            }
        })


    }
}