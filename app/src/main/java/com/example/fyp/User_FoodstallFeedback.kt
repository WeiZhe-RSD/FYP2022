package com.example.fyp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ProgressBar
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fyp.Entity.Feedback
import com.example.fyp.Entity.Food
import com.google.firebase.firestore.*
import java.math.RoundingMode
import java.text.DecimalFormat

class User_FoodstallFeedback : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var foodArrayList: ArrayList<Feedback>
    private lateinit var foodAdapter:FeedbackAdapter
    private lateinit var db : FirebaseFirestore
    private lateinit var feedbackObj : Feedback


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_foodstall_feedback)

        var allRating:Double = 0.0
        var count:Int = 0
        var average:Double = 0.0
        var one:Int = 0
        var two:Int = 0
        var three:Int = 0
        var four:Int = 0
        var five:Int = 0

        var foodstall = intent.getStringExtra("foodstall")

        if(foodstall != null){


            var tvAverRatin = findViewById<TextView>(R.id.tvAverRatin)
            var tvTtlRatin = findViewById<TextView>(R.id.tvTtlRatin)
            var AverRatinBar = findViewById<RatingBar>(R.id.AverRatinBar)

            var oneprogressBar = findViewById<ProgressBar>(R.id.oneprogressBar)
            var twoprogressBar2 = findViewById<ProgressBar>(R.id.twoprogressBar2)
            var threeprogressBar3 = findViewById<ProgressBar>(R.id.threeprogressBar3)
            var fourprogressBar4 = findViewById<ProgressBar>(R.id.fourprogressBar4)
            var fiveprogressBar5 = findViewById<ProgressBar>(R.id.fiveprogressBar5)


            db = FirebaseFirestore.getInstance()
            db.collection("feedback")
                .whereEqualTo("foodstallID", foodstall)
                .get()
                .addOnSuccessListener { documentxx ->

                    for (documentx in documentxx) {
                        feedbackObj =
                            documentx.toObject(Feedback::class.java)
                        allRating += feedbackObj.rating.toString().toInt()
                        count += 1

                        if(feedbackObj.rating.toString().toInt() == 1){
                            one+=1
                        } else if(feedbackObj.rating.toString().toInt() == 2){
                            two+=1
                        } else if(feedbackObj.rating.toString().toInt() == 3){
                            three+=1
                        } else if(feedbackObj.rating.toString().toInt() == 4){
                            four+=1
                        } else if(feedbackObj.rating.toString().toInt() == 5){
                            five+=1
                        }

                    }
                    average = allRating/count

                    val df = DecimalFormat("#.##")
                    df.roundingMode = RoundingMode.DOWN
                    val roundoff = df.format(average)

                    tvTtlRatin.text = "$count Ratings"
                    tvAverRatin.text = roundoff.toString()

                    AverRatinBar.rating = roundoff.toFloat()

                    oneprogressBar.max = count
                    twoprogressBar2.max = count
                    threeprogressBar3.max = count
                    fourprogressBar4.max = count
                    fiveprogressBar5.max = count

                    oneprogressBar.progress = one
                    twoprogressBar2.progress = two
                    threeprogressBar3.progress = three
                    fourprogressBar4.progress = four
                    fiveprogressBar5.progress = five

                }






            recyclerView = findViewById(R.id.rvAllReview)
            recyclerView.layoutManager = LinearLayoutManager(this)
            recyclerView.setHasFixedSize(true)
            foodArrayList = arrayListOf()

            foodAdapter = FeedbackAdapter(foodArrayList)

            recyclerView.adapter = foodAdapter

            setDataInList(foodstall)

        }




    }

    private fun setDataInList(foodstall:String){



        db = FirebaseFirestore.getInstance()
        db.collection("feedback").addSnapshotListener(object : EventListener<QuerySnapshot> {

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
                        foodArrayList.add((dc.document.toObject(Feedback::class.java)))
                        if(foodArrayList.get(foodArrayList.size - 1).status == "Inactive" || foodArrayList.get(foodArrayList.size - 1).foodstallID  != foodstall){
                            foodArrayList.removeAt(foodArrayList.size - 1)
                        }
                    }
                }
                foodAdapter.notifyDataSetChanged()
            }
        })



    }



}