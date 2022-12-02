package com.example.fyp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.View
import android.widget.*
import com.example.fyp.Entity.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.AggregateSource
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class User_CheckPin : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var userObj: User
    private lateinit var walletObj: Ewallet
    private lateinit var cartObj: Cart
    private lateinit var cartDetailObj: CartDetail



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_check_pin)

        var  map = mutableMapOf<String, Int>()
        var sub  = intent.getDoubleExtra("sub", 0.0)
        var stallname  = intent.getStringExtra("stallname")

        if(sub!= null){
        val digit1 = findViewById<TextView>(R.id.editTextNumber)

        val btnCheckPin = findViewById<Button>(R.id.btnCheckPin)


        btnCheckPin.setOnClickListener(){
var detailSize:Int = 0
            var paymentSize:Int = 0
            var orderDetailSize:Int = 0
            var transSize:Int = 0
            var id:String = ""
            var detailid:String = ""
            var payid:String = ""
            var tranID:String = ""
            var balance:Double = 0.0
            auth = FirebaseAuth.getInstance()

            val user = auth.currentUser
            db = Firebase.firestore

            val userRef = db.collection("user").document(user?.email.toString())
            userRef.get().addOnSuccessListener {
                if (it != null) {
                    userObj = it.toObject(User::class.java)!!
                    Log.i("kongannnnnnnnnnnnnnnnnn", userObj.toString())

                    db = FirebaseFirestore.getInstance()
                    db.collection("ewallet")
                        .whereEqualTo("userID", userObj.id)
                        .get()
                        .addOnSuccessListener { documents ->
                            Log.i("kongannnnnnnnnnnnnnnnnn", userObj.toString())

                            for (document in documents) {
                                walletObj = document.toObject(Ewallet::class.java)
                            }
                            Log.i("waletttttttttttttt", walletObj.toString())
                            if(digit1.text.toString() == walletObj.pinNo){

                                val collection = db.collection("order")
                                val countQuery = collection.count()
                                countQuery.get(AggregateSource.SERVER).addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        val snapshot = task.result
                                        detailSize = snapshot.count.toInt()
                                        detailSize += 1

                                        val collection1 = db.collection("payment")
                                        val countQuery1 = collection1.count()
                                        countQuery1.get(AggregateSource.SERVER).addOnCompleteListener { tasks ->
                                            if (tasks.isSuccessful) {
                                                val snapshot1 = tasks.result
                                                paymentSize = snapshot1.count.toInt()
                                                paymentSize += 1


                                                val collection2 = db.collection("orderDetail")
                                                val countQuery2 = collection2.count()
                                                countQuery2.get(AggregateSource.SERVER).addOnCompleteListener { taskss ->
                                                    if (taskss.isSuccessful) {
                                                        val snapshot2 = taskss.result
                                                        orderDetailSize = snapshot2.count.toInt()
                                                        orderDetailSize += 1

                                                        val collection3 = db.collection("ewalletTransaction")
                                                        val countQuery3 = collection3.count()
                                                        countQuery3.get(AggregateSource.SERVER).addOnCompleteListener { taskssx ->
                                                            if (taskssx.isSuccessful) {
                                                                val snapshot3 = taskssx.result
                                                                transSize = snapshot3.count.toInt()
                                                                transSize += 1

                                                                if(transSize < 10){
                                                                    tranID = "T000$transSize"
                                                                } else if(transSize < 100){
                                                                    tranID = "T00$transSize"
                                                                }else if(transSize < 1000){
                                                                    tranID = "T0$transSize"
                                                                }else if(transSize < 10000){
                                                                    tranID = "T$transSize"
                                                                }

                                                        if(paymentSize < 10){
                                                    payid = "P000$paymentSize"
                                                } else if(paymentSize < 100){
                                                    payid = "P00$paymentSize"
                                                }else if(paymentSize < 1000){
                                                    payid = "P0$paymentSize"
                                                }else if(paymentSize < 10000){
                                                    payid = "P$paymentSize"
                                                }


                                                if(detailSize < 10){
                                                    id = "OD0000$detailSize"
                                                } else if(detailSize < 100){
                                                    id = "OD000$detailSize"
                                                }else if(detailSize < 1000){
                                                    id = "OD00$detailSize"
                                                }else if(detailSize < 10000){
                                                    id = "OD0$detailSize"
                                                }else if(detailSize < 100000){
                                                    id = "OD$detailSize"
                                                }

                                                        if(orderDetailSize < 10){
                                                            detailid = "ORD0000$orderDetailSize"
                                                        } else if(orderDetailSize < 100){
                                                            detailid = "ORD000$orderDetailSize"
                                                        }else if(orderDetailSize < 1000){
                                                            detailid = "ORD00$orderDetailSize"
                                                        }else if(orderDetailSize < 10000){
                                                            detailid = "ORD0$orderDetailSize"
                                                        }else if(orderDetailSize < 100000){
                                                            detailid = "ORD$orderDetailSize"
                                                        }


                                                        val sdf = SimpleDateFormat("dd-MM-yyyy hh:mm:ss")

                                                val currentDate = sdf.format(Date())

                                        val orderDetail = hashMapOf(
                                            "orderID" to id,
                                            "date" to currentDate.toString(),
                                            "status" to "Ordered",
                                            "userID" to userObj.id,
                                            "paymentID" to payid,
                                            "ttlPrice" to sub.toString(),
                                        )



                                        db.collection("order")
                                            .document(id).set(orderDetail)
                                            .addOnSuccessListener {
                                                Toast.makeText(this, "Added Successfully", Toast.LENGTH_SHORT)
                                                    .show()
                                            }




                                                val paymentDetail = hashMapOf(
                                                    "paymentID" to payid,
                                                    "amount" to sub.toString(),
                                                    "type" to "Ewallet",
                                                    "date" to currentDate.toString(),
                                                    "status" to "Completed",
                                                    "orderID" to id,
                                                )



                                                db.collection("payment")
                                                    .document(payid).set(paymentDetail)
                                                    .addOnSuccessListener {
                                                        Toast.makeText(this, "Added Successfully", Toast.LENGTH_SHORT)
                                                            .show()
                                                    }


                                                        db = FirebaseFirestore.getInstance()
                                                        db.collection("cart")
                                                            .whereEqualTo("customerID", userObj.id)
                                                            .get()
                                                            .addOnSuccessListener { documentxx ->

                                                                for (documentx in documentxx) {
                                                                    cartObj =
                                                                        documentx.toObject(Cart::class.java)
                                                                }


                                                                db = FirebaseFirestore.getInstance()
                                                                db.collection("cartDetail")
                                                                    .whereEqualTo("cartID", cartObj.cartID)
                                                                    .whereEqualTo("status", "Active")
                                                                    .get()
                                                                    .addOnSuccessListener { documentxx ->

                                                                        for (documentx in documentxx) {
                                                                            cartDetailObj = documentx.toObject(CartDetail::class.java)
/*
                                                                             map = mutableMapOf("${cartDetailObj.foodID}" to cartDetailObj.quantity)
*/


                                                                            map.put("${cartDetailObj.foodID}" , cartDetailObj.quantity!!.toInt())
                                                                            val washingtonRef = db.collection("cartDetail").document(cartDetailObj.detailID.toString())
                                                                            washingtonRef
                                                                                .update("status", "Inactive")


                                                                        }
                                                                        Log.i("kannnnnnnnnnnnnnnnn", map.toString())


                                                                        val OrdDetail = hashMapOf(
                                                                            "orderID" to id,
                                                                            "orderDetailID" to detailid,
                                                                            "OrderContent" to map,
                                                                            "status" to "Pending",
                                                                            "price" to cartDetailObj.subtotal.toString(),
                                                                        )



                                                                        db.collection("orderDetail")
                                                                            .document(detailid).set(OrdDetail)
                                                                            .addOnSuccessListener {

                                                                            }


                                                                        val transDetail = hashMapOf(
                                                                            "amount" to sub.toString(),
                                                                            "date" to currentDate.toString(),
                                                                            "from" to userObj.id,
                                                                            "status" to "Active",
                                                                            "to" to stallname,
                                                                            "transactionID" to tranID,
                                                                            "type" to "Payment",
                                                                            "walletID" to walletObj.walletID,
                                                                        )



                                                                        db.collection("ewalletTransaction")
                                                                            .document(tranID).set(transDetail)
                                                                            .addOnSuccessListener {

                                                                            }

                                                                        val washingtonRef2 = db.collection("ewallet").document(walletObj.walletID.toString())
                                                                        washingtonRef2
                                                                            .update("balance", walletObj.balance!! - sub)


                                                                        Toast.makeText(
                                                                            applicationContext,
                                                                            "Everything Done",
                                                                            Toast.LENGTH_SHORT
                                                                        ).show()


                                                                    }

                                                            }



                                                    }
                                                }


                                            }
                                            }
                                        }

                                        }

                                    }
                                }




                            }else{
                                Log.i("wronggggggggggggggggggg", userObj.toString())
                            }



                        }
                }
            }


        }
        }


    }

}