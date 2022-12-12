package com.example.fyp

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fyp.Entity.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import java.math.RoundingMode
import java.text.DecimalFormat

class User_Cart : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var cartArrayList: ArrayList<CartDetail>
    private lateinit var cartAdapter:CartDetailAdapter
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var cID: String
    private lateinit var userObj:User
    private lateinit var cartObj:Cart
    private lateinit var a:Food

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_cart)

        auth = FirebaseAuth.getInstance()

        val user = auth.currentUser
        db = Firebase.firestore

        val userRef = db.collection("user").document(user?.email.toString())
        userRef.get().addOnSuccessListener {
            if (it != null) {
                userObj = it.toObject(User::class.java)!!

                Log.i("inherit Success", userObj.email.toString())

                recyclerView = findViewById(R.id.rvCart)
                recyclerView.layoutManager = LinearLayoutManager(this)
                recyclerView.setHasFixedSize(true)
                cartArrayList = arrayListOf()

                cartAdapter = CartDetailAdapter(cartArrayList)

                recyclerView.adapter = cartAdapter

                setDataInList()

                cartAdapter.onItemClick = {
                    val intent = Intent(this, User_EditCart::class.java)
                    intent.putExtra("cart", it)
                    startActivity(intent)
                    finish()
                }

                val btnCheckOut = findViewById<Button>(R.id.btnCheckOut)

                btnCheckOut.setOnClickListener(){
                    val intent = Intent(this, User_Checkout::class.java)
                    startActivity(intent)
                    finish()
                }

            }
        }


    }


    private fun setDataInList() {
        var tvCartCafe = findViewById<TextView>(R.id.tvCartCafe)
        var tvCartTotal = findViewById<TextView>(R.id.tvCartTotal)
        var sub: Double = 0.0
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

                        if(cartArrayList.size!=0) {

                            db = FirebaseFirestore.getInstance()
                            db.collection("food")
                                .whereEqualTo(
                                    "foodID",
                                    cartArrayList.get(cartArrayList.size - 1).foodID.toString()
                                )
                                .get()
                                .addOnSuccessListener { documentxx ->

                                    for (documentxxx in documentxx) {
                                        a = documentxxx.toObject(Food::class.java)
                                    }
                                        tvCartCafe.text = a.foodstallID


                                }
                            val df = DecimalFormat("#.##")
                            df.roundingMode = RoundingMode.DOWN
                            val roundoff = df.format(sub)
                            tvCartTotal.text = "RM $roundoff"

                            cartAdapter.notifyDataSetChanged()
                        }else{
                            tvCartCafe.text = "The Cart is Empty..."
                            tvCartTotal.text = "RM 0.00"
                        }
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
