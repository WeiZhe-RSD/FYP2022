package com.example.fyp

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
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase

class User_Wallet : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var walletArrayList: ArrayList<EwalletTransaction>
    private lateinit var walletAdapter:TransactionAdapter
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var userObj: User
    private lateinit var walletObj: Ewallet

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_wallet)

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
                    .addOnSuccessListener { documents->
                        Log.i("kongannnnnnnnnnnnnnnnnn", userObj.toString())

                        for (document in documents) {
                            walletObj = document.toObject(Ewallet::class.java)
                        }
                        Log.i("madafakaaaaaaaaaaaaaaa", walletObj.toString())

                        val tvBalance = findViewById<TextView>(R.id.tvBalance)

                        tvBalance.text = walletObj.balance.toString()


                        val btnGenQR = findViewById<Button>(R.id.btnGenQR)
                        val btnTopup = findViewById<Button>(R.id.btnTopup)

                        btnGenQR.setOnClickListener() {
                            val intent = Intent(
                                this, User_WalletQR::class.java
                            )
                            /*.putExtra("userObj", userObj)*/
                            startActivity(intent)
                        }

                        btnTopup.setOnClickListener(){

                        }

                        recyclerView = findViewById(R.id.rvTranHistory)
                        recyclerView.layoutManager = LinearLayoutManager(this)
                        recyclerView.setHasFixedSize(true)
                        walletArrayList = arrayListOf()

                        walletAdapter = TransactionAdapter(walletArrayList)

                        recyclerView.adapter = walletAdapter

                        setDataInList(walletObj.walletID.toString())

                        /*walletAdapter.onItemClick = {
                            val intent = Intent(this, ::class.java)
                            intent.putExtra("food", it)
                            startActivity(intent)
                        }*/


                        /*db.collection("ewalletTransaction")
                            .whereEqualTo("walletID", walletObj.walletID)
                            .get()
                            .addOnSuccessListener { documents ->

                                walletObj = it.toObject(Ewallet::class.java)!!


                            }*/





                    }



            }
        }
    }

    private fun setDataInList(wallet:String){



        db = FirebaseFirestore.getInstance()
        db.collection("ewalletTransaction").orderBy("date", Query.Direction.DESCENDING).addSnapshotListener(object : EventListener<QuerySnapshot> {

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
                        walletArrayList.add((dc.document.toObject(EwalletTransaction::class.java)))
                        if(walletArrayList.get(walletArrayList.size - 1).status == "Inactive" || walletArrayList.get(walletArrayList.size - 1).walletID != wallet){
                            walletArrayList.removeAt(walletArrayList.size - 1)
                        }
                    }
                }
                walletAdapter.notifyDataSetChanged()
            }
        })



    }
}