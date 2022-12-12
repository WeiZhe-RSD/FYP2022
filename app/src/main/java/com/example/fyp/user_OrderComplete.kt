package com.example.fyp

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fyp.Entity.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.math.RoundingMode
import java.text.DecimalFormat

class user_OrderComplete : AppCompatActivity() {
    private lateinit var db : FirebaseFirestore
    private lateinit var orderObj: OrderDetail
    private lateinit var recyclerView: RecyclerView
    private lateinit var cartArrayList: ArrayList<CartDetail>
    private lateinit var cartAdapter:CheckoutAdapter
    private lateinit var auth: FirebaseAuth

    private lateinit var userObj:User
    private lateinit var cartObj: Cart
    private lateinit var walletObj: Ewallet
    private lateinit var a:Food
    private var sub: Double = 0.0
    var order: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_order_complete)


        val progress = findViewById<ProgressBar>(R.id.progressBarOrder)
        auth = FirebaseAuth.getInstance()
        progress.max = 10

         order = intent.getStringExtra("order")!!
        var tvCheckOutCafe = findViewById<TextView>(R.id.tvCompleteCafe)

        if(order != null){
            val user = auth.currentUser
            db = Firebase.firestore

            val userRef = db.collection("user").document(user?.email.toString())
            userRef.get().addOnSuccessListener {
                if (it != null) {
                    userObj = it.toObject(User::class.java)!!

                    val btnFeedback = findViewById<Button>(R.id.btnFeedback)

                    db = FirebaseFirestore.getInstance()
                    db.collection("orderDetail")
                        .whereEqualTo("orderID", order)
                        .get()
                        .addOnSuccessListener { documentxx ->

                            for (documentx in documentxx) {
                                orderObj =
                                    documentx.toObject(OrderDetail::class.java)
                            }

                            if (orderObj.status == "Pending") {
                                progress.progress = 1
                            } else if (orderObj.status == "Preparing") {
                                progress.progress = 6
                            } else if (orderObj.status == "Done") {
                                progress.progress = 10
                            }

                            recyclerView = findViewById(R.id.rvOrderComplete)
                            recyclerView.layoutManager = LinearLayoutManager(this)
                            recyclerView.setHasFixedSize(true)
                            cartArrayList = arrayListOf()

                            cartAdapter = CheckoutAdapter(cartArrayList)

                            recyclerView.adapter = cartAdapter

                            setDataInList()

                            btnFeedback.setOnClickListener(){

                                val intent = Intent(
                                    this, User_SubmitFeedback::class.java
                                )
                                    .putExtra("stallName", tvCheckOutCafe.text.toString())
                                    .putExtra("order", order)
                                startActivity(intent)
                                finish()

                            }


                        }
                }
            }
        }


    }

    private fun setDataInList() {
        var tvCheckOutCafe = findViewById<TextView>(R.id.tvCompleteCafe)
        var tvTotalPay = findViewById<TextView>(R.id.tvCompTotal)
        db = FirebaseFirestore.getInstance()
        db.collection("cart")
            .whereEqualTo("customerID", userObj.id)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    cartObj = document.toObject(Cart::class.java)
                }


                db = FirebaseFirestore.getInstance()
                db.collection("cartDetail")
                    .whereEqualTo("cartID", cartObj.cartID)
                    .whereEqualTo("orderID", order)
                    .get()
                    .addOnSuccessListener { documentss ->

                        for (documentsss in documentss) {
                            cartArrayList.add(documentsss.toObject(CartDetail::class.java))
                            sub += cartArrayList.get(cartArrayList.size - 1).subtotal.toString()
                                .toDouble()
                        }

                        db = FirebaseFirestore.getInstance()
                        db.collection("food")
                            .whereEqualTo("foodID", cartArrayList.get(cartArrayList.size - 1).foodID)
                            .get()
                            .addOnSuccessListener { documentxx ->

                                for (documentxxx in documentxx) {
                                    a = documentxxx.toObject(Food::class.java)
                                }
                                tvCheckOutCafe.text = a.foodstallID
                            }
                        val df = DecimalFormat("#.##")
                        df.roundingMode = RoundingMode.DOWN
                        val roundoff = df.format(sub)
                        tvTotalPay.text = "RM $roundoff"

                        cartAdapter.notifyDataSetChanged()
                    }

            }
            .addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "Error getting documents: ", exception)
            }


    }



}