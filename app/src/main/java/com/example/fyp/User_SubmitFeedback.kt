package com.example.fyp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import com.example.fyp.Entity.Ewallet
import com.example.fyp.Entity.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.AggregateSource
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*

class User_SubmitFeedback : AppCompatActivity() {
    private lateinit var db : FirebaseFirestore
    private lateinit var userObj: User
    private lateinit var auth: FirebaseAuth



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_submit_feedback)

var transSize:Int = 0
        var tranID:String = ""
        var stallName = intent.getStringExtra("stallName")
        var order = intent.getStringExtra("order")
        val sdf = SimpleDateFormat("dd-MM-yyyy hh:mm:ss")

        val currentDate = sdf.format(Date())
        if(stallName != null && order != null){

            var tvFeedbackCafe = findViewById<TextView>(R.id.tvFeedbackCafe)
            var tfFeedbackDesc = findViewById<TextView>(R.id.tfFeedbackDesc)
            var ratingBarRate = findViewById<RatingBar>(R.id.ratingBarRate)
            var ratingScale = findViewById<TextView>(R.id.ratingScale)
            var btnFeedbackSub = findViewById<Button>(R.id.btnFeedbackSub)

            tvFeedbackCafe.text = stallName

            ratingBarRate.setOnRatingBarChangeListener{ rBar, fl,b ->
                ratingScale.text = fl.toString()

                when(rBar.rating.toInt()){
                    1 -> ratingScale.text = "Very Bad"
                    2 -> ratingScale.text = "Bad"
                    3 -> ratingScale.text = "Moderate"
                    4 -> ratingScale.text = "Good"
                    5 -> ratingScale.text = "Very Good"
                    else ->ratingScale.text = ""
                }



            }


            btnFeedbackSub.setOnClickListener(){
                auth = FirebaseAuth.getInstance()

                val user = auth.currentUser
                db = Firebase.firestore

                val userRef = db.collection("user").document(user?.email.toString())
                userRef.get().addOnSuccessListener {
                    if (it != null) {
                        userObj = it.toObject(User::class.java)!!

                val collection3 = db.collection("feedback")
                val countQuery3 = collection3.count()
                countQuery3.get(AggregateSource.SERVER).addOnCompleteListener { taskssx ->
                    if (taskssx.isSuccessful) {
                        val snapshot3 = taskssx.result
                        transSize = snapshot3.count.toInt()
                        transSize += 1

                        if(transSize < 10){
                            tranID = "FB000$transSize"
                        } else if(transSize < 100){
                            tranID = "FB00$transSize"
                        }else if(transSize < 1000){
                            tranID = "FB0$transSize"
                        }else if(transSize < 10000){
                            tranID = "FB$transSize"
                        }


                var ordDetail = hashMapOf(
                    "feedbackID" to tranID,
                    "rating" to ratingBarRate.rating.toInt().toString(),
                    "description" to tfFeedbackDesc.text.toString(),
                    "status" to "Active",
                    "date" to currentDate.toString(),
                    "customerID" to userObj.id,
                    "foodstallID" to stallName,
                    "orderID" to order,

                )



                    db.collection("feedback")
                    .document(tranID).set(ordDetail)
                    .addOnSuccessListener {
                        Toast.makeText(
                            applicationContext,
                            "Feedback Submitted",
                            Toast.LENGTH_SHORT
                        ).show()

                        val intent = Intent(
                            this, user_OrderComplete::class.java
                        )
                            .putExtra("order", order)
                        startActivity(intent)
                        finish()

                    }
                    }
                }

            }






        }
            }
        }


    }
}