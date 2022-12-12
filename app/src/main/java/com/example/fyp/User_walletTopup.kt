package com.example.fyp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.firestore.AggregateSource
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*

class User_walletTopup : AppCompatActivity() {
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_wallet_topup)

        val wallet = intent.getStringExtra("wallet")
        var bal = intent.getStringExtra("bal")

        db = Firebase.firestore

        var tfTopupAmount = findViewById<TextView>(R.id.tfTopupAmount)

        var btnTopupCredit = findViewById<Button>(R.id.btnTopupCredit)

        btnTopupCredit.setOnClickListener() {
            if (tfTopupAmount.text.isEmpty()) {
                Toast.makeText(applicationContext,"Please Enter Top up amount",
                    Toast.LENGTH_LONG).show()

            } else {


                var ttl:Double = bal.toString().toDouble() + tfTopupAmount.text.toString().toDouble()
                val sdf = SimpleDateFormat("dd-MM-yyyy hh:mm:ss")
                var transSize: Int = 0
                var tranID: String = ""

                val currentDate = sdf.format(Date())

                val washingtonRef2 = db.collection("ewallet").document(wallet.toString())
                washingtonRef2
                    .update("balance", ttl.toString())

                val collection3 = db.collection("ewalletTransaction")
                val countQuery3 = collection3.count()
                countQuery3.get(AggregateSource.SERVER).addOnCompleteListener { taskssx ->
                    if (taskssx.isSuccessful) {
                        val snapshot3 = taskssx.result
                        transSize = snapshot3.count.toInt()
                        transSize += 1

                        if (transSize < 10) {
                            tranID = "T000$transSize"
                        } else if (transSize < 100) {
                            tranID = "T00$transSize"
                        } else if (transSize < 1000) {
                            tranID = "T0$transSize"
                        } else if (transSize < 10000) {
                            tranID = "T$transSize"
                        }

                        val transDetail = hashMapOf(
                            "amount" to tfTopupAmount.text.toString(),
                            "date" to currentDate.toString(),
                            "from" to "Top up",
                            "status" to "Active",
                            "to" to wallet,
                            "transactionID" to tranID,
                            "type" to "Reload",
                            "walletID" to wallet,
                        )



                        db.collection("ewalletTransaction")
                            .document(transSize.toString()).set(transDetail)
                            .addOnSuccessListener {

                            }
                    }
                }

                val intent = Intent(this, User_Wallet::class.java)
                startActivity(intent)
                finish()

            }
        }


    }
}