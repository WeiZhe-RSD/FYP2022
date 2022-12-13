package com.example.fyp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fyp.Entity.EatTogehter
import com.example.fyp.Entity.Order
import com.example.fyp.Entity.Payment
import com.example.fyp.Entity.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Seller_ManagePendingPay : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var cafeteriaArrayList: ArrayList<Payment>
    private lateinit var cafeteriaAdapter:PaymentAdapter
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var userObj: User
    var stallname:String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seller_manage_pending_pay)

stallname = intent.getStringExtra("stallname")!!
        val btnViewOrdersss = findViewById<Button>(R.id.btnViewOrdersss)


        recyclerView = findViewById(R.id.rvPendingPay)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        cafeteriaArrayList = arrayListOf()

        cafeteriaAdapter = PaymentAdapter(cafeteriaArrayList)

        recyclerView.adapter = cafeteriaAdapter

        setDataInList()
        btnViewOrdersss.setOnClickListener(){
            finish()
        }

        cafeteriaAdapter.onItemClick = {
            val intent = Intent(this, Seller_PaymentDetail::class.java)
            intent.putExtra("payment", it)
            startActivity(intent)
        }
    }

    private fun setDataInList(){

        db = FirebaseFirestore.getInstance()
                    db.collection("payment")
                        .whereEqualTo("type", "Cash")
                        .whereEqualTo("status", "Pending")
                        .get()
                        .addOnSuccessListener { documentssx ->

                            for (documentsssx in documentssx) {
                                cafeteriaArrayList.add(documentsssx.toObject(Payment::class.java))
                            }
                            cafeteriaArrayList.sortByDescending { "date" }


                            cafeteriaAdapter.notifyDataSetChanged()
                        }









    }

}