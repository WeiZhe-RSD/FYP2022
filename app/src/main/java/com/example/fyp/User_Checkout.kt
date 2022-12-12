package com.example.fyp

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fyp.Entity.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.math.RoundingMode
import java.text.DecimalFormat

class User_Checkout : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var cartArrayList: ArrayList<CartDetail>
    private lateinit var cartAdapter:CheckoutAdapter
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    private lateinit var userObj:User
    private lateinit var cartObj: Cart
    private lateinit var walletObj: Ewallet
    private lateinit var a:Food
    private var sub: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_checkout)

        auth = FirebaseAuth.getInstance()

        val user = auth.currentUser
        db = Firebase.firestore

        val userRef = db.collection("user").document(user?.email.toString())
        userRef.get().addOnSuccessListener {
            if (it != null) {
                userObj = it.toObject(User::class.java)!!

                val rgPMethod = findViewById<RadioGroup>(R.id.rgPMethod)
                val btnPay = findViewById<Button>(R.id.btnPay)
                var tvCheckOutCafe = findViewById<TextView>(R.id.tvCheckOutCafe)

                Log.i("inherit Success", userObj.email.toString())

                recyclerView = findViewById(R.id.rvCheckOut)
                recyclerView.layoutManager = LinearLayoutManager(this)
                recyclerView.setHasFixedSize(true)
                cartArrayList = arrayListOf()

                cartAdapter = CheckoutAdapter(cartArrayList)

                recyclerView.adapter = cartAdapter

                setDataInList()

                btnPay.setOnClickListener(){
                    val spicyLevel = rgPMethod.checkedRadioButtonId
                    val result = when(spicyLevel){
                        R.id.rbCash-> "Cash"
                        R.id.rbEwallet -> "Ewallet"

                        else -> "Error"
                    }

                    if(result == "Cash"){
                        val intent = Intent(this, splash_CashPay::class.java)
                        intent.putExtra("sub", sub)
                        intent.putExtra("stallname", tvCheckOutCafe.text.toString())
                        startActivity(intent)
                        finish()
                    }else if (result == "Ewallet"){

                        db = FirebaseFirestore.getInstance()
                        db.collection("ewallet")
                            .whereEqualTo("userID", userObj.id)
                            .get()
                            .addOnSuccessListener { documentssx ->
                                Log.i("kongannnnnnnnnnnnnnnnnn", userObj.toString())

                                for (documentsssx in documentssx) {
                                    walletObj= documentsssx.toObject(Ewallet::class.java)
                                }

                                if(walletObj.status == "Inactive"){
                                    Toast.makeText(
                                        applicationContext,
                                        "You have not activate your ewallet yet",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }else if(walletObj.balance.toString().toDouble()!! > sub){
                                    val intent = Intent(this, User_CheckPin::class.java)
                                    intent.putExtra("sub", sub)
                                    intent.putExtra("stallname", tvCheckOutCafe.text.toString())
                                    startActivity(intent)
                                    finish()
                                }else{
                                    Toast.makeText(
                                        applicationContext,
                                        "Insufficient Ewallet Balance",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                }
                            }




                    }


                }

                cartAdapter.onItemClick = {
                    val intent = Intent(this, User_EditCart::class.java)
                    intent.putExtra("cart", it)
                    startActivity(intent)
                    finish()
                }

            }
        }



    }


    private fun setDataInList() {
        var tvCheckOutCafe = findViewById<TextView>(R.id.tvCheckOutCafe)
        var tvTotalPay = findViewById<TextView>(R.id.tvTotalPay)
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
                    .whereEqualTo("status", "Active")
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


                /*db.collection("cartDetail").addSnapshotListener(object : EventListener<QuerySnapshot> {


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
                        Log.e("harlooooooooooooooo", dc.document.toString())


                        cartArrayList.add((dc.document.toObject(CartDetail::class.java)))
                        if (cartArrayList.get(cartArrayList.size - 1).status == "Inactive" || cartArrayList.get(
                                cartArrayList.size - 1
                            ).cartID != cartObj.cartID
                        ) {
                            cartArrayList.removeAt(cartArrayList.size - 1)
                        }
                    }
                }
                cartAdapter.notifyDataSetChanged()
            }
        })*/

            }
            .addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "Error getting documents: ", exception)
            }


    }


}