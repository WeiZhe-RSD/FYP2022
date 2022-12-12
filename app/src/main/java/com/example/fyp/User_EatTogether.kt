package com.example.fyp

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fyp.Entity.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class User_EatTogether : AppCompatActivity() {
    private lateinit var btnDatePicker: Button
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var userObj: User
    private lateinit var cafeterianame:ArrayList<String>
    private lateinit var recyclerView: RecyclerView
    private lateinit var foodArrayList: ArrayList<User>
    private lateinit var foodAdapter:EatTogetherAdapter
    private lateinit var eatTogObj:EatTogehter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_eat_together)

        val btnEatApply = findViewById<Button>(R.id.btnEatApply)


            auth = FirebaseAuth.getInstance()
            val user = auth.currentUser
            db = Firebase.firestore

            if (user != null) {
                Log.i("Login Success", user.email.toString())

                val userRef = db.collection("user").document(user?.email.toString())
                userRef.get().addOnSuccessListener {
                    if (it != null) {
                        userObj = it.toObject(User::class.java)!!


                        btnDatePicker = findViewById(R.id.btnDate)


                        //////////////////////////////////////////////////
                        val today = Calendar.getInstance()

                        val datePicker =
                            DatePickerDialog.OnDateSetListener { view, year, month, dateOfMonth ->
                                today.set(Calendar.YEAR, year)
                                today.set(Calendar.MONTH, month)
                                today.set(Calendar.DAY_OF_MONTH, dateOfMonth)
                                updateLabel(today)

                            }



                        btnDatePicker.setOnClickListener() {
                            DatePickerDialog(
                                this,
                                datePicker,
                                today.get(Calendar.YEAR),
                                today.get(Calendar.MONTH),
                                today.get(Calendar.DAY_OF_MONTH)
                            ).show()

                        }


                        val spinner = findViewById<Spinner>(R.id.spinner)
                        val spinner2 = findViewById<Spinner>(R.id.spinner2)


                        val times = arrayOf(
                            "8am",
                            "9am",
                            "10am",
                            "11am",
                            "12pm",
                            "1pm",
                            "2pm",
                            "3pm",
                            "4pm",
                            "5pm"
                        )
                        val adapter = ArrayAdapter<String>(
                            this,
                            android.R.layout.simple_spinner_item, times
                        )

                        spinner.adapter = adapter
                        spinner.setSelection(0);
                        cafeterianame = arrayListOf()

                        db = FirebaseFirestore.getInstance()
                        db.collection("cafeteria")
                            .whereEqualTo("status", "active")
                            .get()
                            .addOnSuccessListener { documents ->

                                for (document in documents) {
                                    cafeterianame.add(document.toObject(Cafeteria::class.java).name.toString()!!)
                                }

                                val adapter2 = ArrayAdapter<String>(
                                    this,
                                    android.R.layout.simple_spinner_item, cafeterianame
                                )

                                spinner2.adapter = adapter2
                                spinner2.setSelection(0);

                                btnEatApply.setOnClickListener(){

                                    if(btnDatePicker.text!="DATE"){

                                    recyclerView = findViewById(R.id.rvEatTogether)
                                    recyclerView.layoutManager = LinearLayoutManager(this)
                                    recyclerView.setHasFixedSize(true)
                                    foodArrayList = arrayListOf()

                                    foodAdapter = EatTogetherAdapter(foodArrayList)

                                    recyclerView.adapter = foodAdapter

                                    setDataInList()

                                    foodAdapter.onItemClick = {

                                        var transSize:Int = 0
                                        var tranID:String = ""
                                        val collection3 = db.collection("eatTogether")
                                        val countQuery3 = collection3.count()
                                        countQuery3.get(AggregateSource.SERVER).addOnCompleteListener { taskssx ->
                                            if (taskssx.isSuccessful) {
                                                val snapshot3 = taskssx.result
                                                transSize = snapshot3.count.toInt()
                                                transSize += 1

                                                if(transSize < 10){
                                                    tranID = "ET000$transSize"
                                                } else if(transSize < 100){
                                                    tranID = "ET00$transSize"
                                                }else if(transSize < 1000){
                                                    tranID = "ET0$transSize"
                                                }else if(transSize < 10000){
                                                    tranID = "ET$transSize"
                                                }

                                                var resultTime = when(spinner.selectedItemPosition){
                                                    0 -> "08:00:00"
                                                    1 -> "09:00:00"
                                                    2 -> "10:00:00"
                                                    3 -> "11:00:00"
                                                    4 -> "12:00:00"
                                                    5 -> "13:00:00"
                                                    6 -> "14:00:00"
                                                    7 -> "15:00:00"
                                                    8 -> "16:00:00"
                                                    else->"error"
                                                }

                                                var resultVenue = cafeterianame.get(spinner2.selectedItemPosition)
var ab:Boolean = true
                                                if(transSize > 1){
                                                    db = FirebaseFirestore.getInstance()
                                                    db.collection("eatTogether")
                                                        .whereEqualTo("status", "Pending")
                                                        .get()
                                                        .addOnSuccessListener { documents ->

                                                            for (document in documents) {
                                                                eatTogObj =
                                                                    document.toObject(EatTogehter::class.java)
                                                                Log.i("omggggggggggggggg", "kknnnnnnnn")
                                                                if (userObj.id == eatTogObj.customerID &&
                                                                    it.id == eatTogObj.invitedID &&
                                                                    "${btnDatePicker.text.trim()} $resultTime" == eatTogObj.dateMeet &&
                                                                    resultVenue == eatTogObj.venue
                                                                ) {
                                                                    Toast.makeText(
                                                                        applicationContext,
                                                                        "Invitation Already Exist",
                                                                        Toast.LENGTH_SHORT
                                                                    ).show()
                                                                    ab = false

                                                                }

                                                            }

                                                            if(ab){
                                                                val sdf =
                                                                    SimpleDateFormat("dd-MM-yyyy hh:mm:ss")

                                                                val currentDate =
                                                                    sdf.format(Date())

                                                                val orderDetail = hashMapOf(
                                                                    "eatTogehterID" to tranID,
                                                                    "dateCreated" to currentDate.toString(),
                                                                    "status" to "Pending",
                                                                    "customerID" to userObj.id,
                                                                    "invitedID" to it.id,
                                                                    "dateMeet" to "${btnDatePicker.text.toString()} $resultTime",
                                                                    "venue" to resultVenue,
                                                                )


                                                                db.collection("eatTogether")
                                                                    .document(tranID)
                                                                    .set(orderDetail)
                                                                    .addOnSuccessListener {
                                                                        Toast.makeText(
                                                                            this,
                                                                            "Sent Successfully",
                                                                            Toast.LENGTH_SHORT
                                                                        )
                                                                            .show()
                                                                    }
                                                            }



                                                        }
                                                }else{

                                                    val sdf = SimpleDateFormat("dd-MM-yyyy hh:mm:ss")

                                                    val currentDate = sdf.format(Date())

                                                    val orderDetail = hashMapOf(
                                                        "eatTogehterID" to tranID,
                                                        "dateCreated" to currentDate.toString(),
                                                        "status" to "Pending",
                                                        "customerID" to userObj.id,
                                                        "invitedID" to it.id,
                                                        "dateMeet" to "${btnDatePicker.text.toString()} $resultTime",
                                                        "venue" to resultTime,
                                                    )



                                                    db.collection("eatTogether")
                                                        .document(tranID).set(orderDetail)
                                                        .addOnSuccessListener {
                                                            Toast.makeText(this, "Sent Successfully", Toast.LENGTH_SHORT)
                                                                .show()
                                                        }

                                                }
                                            }

                                        }


                                    }
                                }else{
                                        Toast.makeText(this, "Select Date", Toast.LENGTH_SHORT)
                                            .show()
                                    }

                                }


                                }




                            }


                    }
                }
            }




    private fun setDataInList(){

        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser
        db = Firebase.firestore

        if (user != null) {
            Log.i("Login Success", user.email.toString())

            val userRef = db.collection("user").document(user?.email.toString())
            userRef.get().addOnSuccessListener {
                if (it != null) {
                    userObj = it.toObject(User::class.java)!!


                    db = FirebaseFirestore.getInstance()
                    db.collection("user")
                        .addSnapshotListener(object : EventListener<QuerySnapshot> {

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
                                        foodArrayList.add((dc.document.toObject(User::class.java)))
                                        if (foodArrayList.get(foodArrayList.size - 1).status == "Inactive" || foodArrayList.get(
                                                foodArrayList.size - 1
                                            ).id == userObj.id|| foodArrayList.get(
                                                foodArrayList.size - 1
                                            ).role == "Seller"
                                        ) {
                                            foodArrayList.removeAt(foodArrayList.size - 1)
                                        }
                                    }
                                }
/*
                tempArrayList.addAll(eventArrayList)
*/
                                foodAdapter.notifyDataSetChanged()
                            }
                        })
                }
            }
        }


    }

    private fun updateLabel(myCalendar: Calendar){
        val myFormat = "dd-MM-yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.UK)


        btnDatePicker.text = (sdf.format(myCalendar.time))
    }
}