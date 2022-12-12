package com.example.fyp

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fyp.Entity.Food
import com.example.fyp.Entity.OrderContentDetail
import com.example.fyp.Entity.OrderDetail
import com.google.firebase.firestore.*

class Seller_ManageSpecificOrder : AppCompatActivity() {
    private lateinit var orderedItemArrayList: ArrayList<OrderContentDetail>
    private lateinit var SpecifcOrderAdapter: SpecifcOrderAdapter
    private lateinit var db: FirebaseFirestore

    private lateinit var orderId: TextView
    private lateinit var btnOrderComplete: Button
    private lateinit var orderItemListRecycler: RecyclerView

    private var orderIDFPI: String? = ""
    private var orderDetailIDFPI: String? = ""
    private var dateFPI: String? = ""
    private var paymentFPI: String? = ""
    private var statusFPI: String? = ""
    private var ttlPriceFPI: String? = ""
    private var foodStallIDFPI: String? = ""
    private var userIDFPI: String? = ""

    //OrderDetailsInfo
    private var orderDetailIDFromODTB: String? = ""
    private var orderIDFromODTB: String? = ""
    private var priceFromODTB: String? = ""
    private var statusFromODTB: String? = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seller_manage_specific_order)

        orderId = findViewById(R.id.specificOrderIdNo)
        btnOrderComplete = findViewById(R.id.completeOrderBtn)
        orderItemListRecycler = findViewById(R.id.orderListRecyclerView)

        orderItemListRecycler.layoutManager = LinearLayoutManager(this)
        orderItemListRecycler.setHasFixedSize(true)
        orderedItemArrayList = arrayListOf()

        SpecifcOrderAdapter = SpecifcOrderAdapter(orderedItemArrayList)
        orderItemListRecycler.adapter = SpecifcOrderAdapter

        getCurrentOrderID()
        getCurrentOrderDetailID()
        orderId.text = orderIDFPI
        setDataInList()

        val db = FirebaseFirestore.getInstance()


        db.collection("order").document(orderIDFPI.toString())
            .get().addOnSuccessListener {
                val statusFTDB = it.getString("status").toString()

                if (statusFTDB == "Order Completed") {
                    btnOrderComplete.isEnabled = false
                    btnOrderComplete.setBackgroundColor(Color.GRAY)
                    btnOrderComplete.visibility = View.VISIBLE
                } else {
                    btnOrderComplete.setOnClickListener {
                        val order = hashMapOf(
                            "date" to dateFPI,
                            "foodstallID" to foodStallIDFPI,
                            "orderDetailID" to orderDetailIDFPI,
                            "orderID" to orderIDFPI,
                            "paymentID" to paymentFPI,
                            "status" to statusFPI,
                            "ttlPrice" to ttlPriceFPI,
                            "userID" to userIDFPI,
                            "status" to "Order Completed"
                        )

                        db.collection("order")
                            .document(orderIDFPI.toString()).set(order)
                            .addOnSuccessListener {
                                Toast.makeText(this, "Updated Successfully", Toast.LENGTH_SHORT)
                                    .show()
                                btnOrderComplete.isEnabled = false
                                btnOrderComplete.setBackgroundColor(Color.GRAY)
                                btnOrderComplete.visibility = View.VISIBLE

                                val intent = Intent(
                                    this@Seller_ManageSpecificOrder,
                                    Seller_ManageOrderDetails::class.java
                                )
                                intent.putExtra("foodStall", foodStallIDFPI)
                                startActivity(intent)
                                finish()
                            }
                            .addOnFailureListener {
                                Toast.makeText(this, "Updated Failed", Toast.LENGTH_SHORT).show()
                            }

                    }
                }
            }


    }

    //All these for order table
    private fun getCurrentOrderID() {
        val intent = intent
        orderIDFPI = intent.getStringExtra("orderID")
    }

    private fun getCurrentOrderDetailID() {
        val intent = intent
        orderDetailIDFPI = intent.getStringExtra("orderDetailID")
        dateFPI = intent.getStringExtra("date")
        paymentFPI = intent.getStringExtra("payment")
        statusFPI = intent.getStringExtra("status")
        ttlPriceFPI = intent.getStringExtra("ttlPrice")
        foodStallIDFPI = intent.getStringExtra("foodstallID")//--
        userIDFPI = intent.getStringExtra("userID")//--
    }

    private fun setDataInList() {
        db = FirebaseFirestore.getInstance()
        db.collection("orderDetail").whereEqualTo("orderDetailID", orderDetailIDFPI)
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
                            var contentMap = dc.document.get("OrderContent") as HashMap<*, *>
                            for ((key, value) in contentMap) {
                                var orderContent = OrderContentDetail(
                                    key as String?,
                                    value as Long
                                )
                                orderedItemArrayList.add(orderContent)
                            }

                        }
                    }
                    SpecifcOrderAdapter.notifyDataSetChanged()
                }
            })


    }

    private fun getCurrentOrderDetail() {
        db.collection("orderDetail").whereEqualTo("orderDetailID", orderDetailIDFPI)
            .get().addOnSuccessListener {
                //string assign using it
            }
    }
}