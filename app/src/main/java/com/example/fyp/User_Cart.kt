package com.example.fyp

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fyp.Entity.Cart
import com.example.fyp.Entity.CartDetail
import com.example.fyp.Entity.FoodStall
import com.example.fyp.Entity.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class User_Cart : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var cartArrayList: ArrayList<CartDetail>
    private lateinit var cartAdapter:CartDetailAdapter
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var cID: String
    private lateinit var userObj:User
    private lateinit var cartObj:Cart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_cart)

         userObj  = intent.getParcelableExtra<User>("userObj")!!

        if(userObj!= null) {
            Log.i("inherit Success", userObj.email.toString())

            recyclerView = findViewById(R.id.rvCart)
            recyclerView.layoutManager = LinearLayoutManager(this)
            recyclerView.setHasFixedSize(true)
            cartArrayList = arrayListOf()

            cartAdapter = CartDetailAdapter(cartArrayList)

            recyclerView.adapter = cartAdapter

            setDataInList()

            cartAdapter.onItemClick = {
                val intent = Intent(this, User_CartDetail::class.java)

                startActivity(intent)
            }


        }

    }


    private fun setDataInList() {
        db = FirebaseFirestore.getInstance()
        db.collection("cart")
            .whereEqualTo("customerID", userObj.id)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    cartObj = document.toObject(Cart::class.java)
                    Log.i("madafakaaaaaaaaaaaaaaa", cartObj.toString())
                }

                val cDDB = db.collection("cartDetail")

                cDDB



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