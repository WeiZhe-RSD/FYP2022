package com.example.fyp

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.format.DateFormat.is24HourFormat
import android.util.Log
import android.widget.*
import com.example.fyp.Entity.FoodStall
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.text.DateFormat
import java.util.*

class Seller_ManageFoodStall : AppCompatActivity(), TimePickerDialog.OnTimeSetListener {
    var hour = 0
    var minute = 0
    var opening:String = ""
    var closing:String = ""
    var datetime: String = "DATE"
    var selectedHour = ""
    var selectedMinute = ""
    var num = 0


    private lateinit var ref: StorageReference
    private lateinit var db: FirebaseFirestore



    private lateinit var mondayCB : CheckBox
    private lateinit var tuesdayCB : CheckBox
    private lateinit var wednesdayCB : CheckBox
    private lateinit var thursdayCB : CheckBox
    private lateinit var fridayCB : CheckBox
    private lateinit var saturdayCB : CheckBox

    private lateinit var stallName : EditText


    private lateinit var updateStallInfoBtn : Button
    private lateinit var openingHoursButton : Button
    private lateinit var closingHoursButton : Button




    private var foodStall : String? =""
    private var foodStallReassigned : String? =""

    //Data got from DB Reassigning
    private var cafeteriaID : String? =""
    private var image : String? =""
    private var location : String? =""
    private var name : String? =""
    private var sellerID : String? =""
    private var status : String? =""

    private var mondayRA : String? =""
    private var tuesdayRA : String? =""
    private var wednesdayRA : String? =""
    private var thursdayRA : String? =""
    private var fridayRA : String? =""
    private var saturdayRA : String? =""

    private var openRA : String? =""
    private var closeRA : String? =""

    private var openingHour : String? =""
    private var openingMinutes : String? =""

    private var closingHour : String? =""
    private var closingMinutes : String? =""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seller_manage_food_stall)

        ref = FirebaseStorage.getInstance().reference
        db = Firebase.firestore

        getCurrentImgSrc()
        getCurrentStallData()

        mondayCB = findViewById(R.id.cbMonday)
        tuesdayCB = findViewById(R.id.cbTuesday)
        wednesdayCB = findViewById(R.id.cbWednesday)
        thursdayCB = findViewById(R.id.cbThursday)
        fridayCB = findViewById(R.id.cbFriday)
        saturdayCB = findViewById(R.id.cbSaturday)

        stallName = findViewById(R.id.ptStallName)

        stallName.editableText.append(foodStallReassigned)

        updateStallInfoBtn = findViewById(R.id.btnUpdate)

        updateStallInfoBtn.setOnClickListener {
            updateStallWorkingHours()
        }

        openingHoursButton = findViewById(R.id.btnOpening)
        closingHoursButton = findViewById(R.id.btnClosing)



        openingHoursButton.setOnClickListener {
            getTimeCalender()
            num = 1
            if(openingHour != ""){
                openingHour?.let { it1 -> openingMinutes?.let { it2 -> TimePickerDialog(this, this, it1.toInt(), it2.toInt(), true).show() } }
            }else{
                TimePickerDialog(this, this, hour, minute, true).show()
            }

        }

        closingHoursButton.setOnClickListener {
            getTimeCalender()
            num = 2
            if(closingHour != ""){
                closingHour?.let { it1 -> closingMinutes?.let { it2 -> TimePickerDialog(this, this, it1.toInt(), it2.toInt(), true).show() } }
            }else{
                TimePickerDialog(this, this, hour, minute, true).show()
            }
        }

    }

    private fun setTimeText (num:Int){
        if(num == 1){
            opening = datetime
            openingHoursButton.text = datetime

        }else{
            closing = datetime
            closingHoursButton.text = datetime
        }
        datetime ="Date"

    }

    private fun getTimeCalender() {
        val cal = Calendar.getInstance()
        hour = cal.get(Calendar.HOUR_OF_DAY)
        minute = cal.get(Calendar.MINUTE)
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        selectedHour = hourOfDay.toString()
        selectedMinute = minute.toString()


        if(selectedHour.length == 1){
            selectedHour = "0${selectedHour.toString()}"
        }
        if(selectedMinute.length == 1){
            selectedMinute = "0$selectedMinute"
        }
        datetime = "$selectedHour:$selectedMinute:00"
        setTimeText(num)
    }

    private fun updateStallWorkingHours(){
        var mondayCheckOrNo : String
        var tuesdayCheckOrNo : String
        var wednesdayCheckOrNo : String
        var thursdayCheckOrNo : String
        var fridayCheckOrNo : String
        var saturdayCheckOrNo : String

        mondayCheckOrNo = if(mondayCB.isChecked){
            "1"
        }else{
            "0"
        }

        tuesdayCheckOrNo = if(tuesdayCB.isChecked){
            "1"
        }else{
            "0"
        }


        wednesdayCheckOrNo = if(wednesdayCB.isChecked){
            "1"
        }else{
            "0"
        }

        thursdayCheckOrNo = if(thursdayCB.isChecked){
            "1"
        }else{
            "0"
        }

        fridayCheckOrNo = if(fridayCB.isChecked){
            "1"
        }else{
            "0"
        }


        saturdayCheckOrNo = if(saturdayCB.isChecked){
            "1"
        }else{
            "0"
        }

        val db = FirebaseFirestore.getInstance()
        val stallInfo = hashMapOf(
            "cafeteriaID" to "$cafeteriaID",
            "image" to "$image",
            "location" to "$location",
            "name" to "$name",
            "sellerID" to "$sellerID",
            "status" to "$status",
            "monday" to mondayCheckOrNo,
            "tuesday" to tuesdayCheckOrNo,
            "wednesday" to wednesdayCheckOrNo,
            "thursday" to thursdayCheckOrNo,
            "friday" to fridayCheckOrNo,
            "saturday" to saturdayCheckOrNo,
            "openingHours" to opening,
            "closingHours" to closing,
        )

        db.collection("foodstall")
            .document(foodStallReassigned.toString()).set(stallInfo)
            .addOnSuccessListener { Toast.makeText(this,"Updated Stall Status Successfully", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {Toast.makeText(this,"Updated Stall Status Failed", Toast.LENGTH_SHORT).show()
            }
        finish()
    }

    private fun getCurrentStallData(){
        db.collection("foodstall").document(foodStallReassigned.toString()).get()
            .addOnSuccessListener {
                var cCafeteriaID = it.getString("cafeteriaID").toString()
                var cImage = it.getString("image").toString()
                var cLocation = it.getString("location").toString()
                var cName = it.getString("name").toString()
                var cSellerID = it.getString("sellerID").toString()
                var cStatus = it.getString("status").toString()
                var cMonday = it.getString("monday").toString()
                var cTuesday = it.getString("tuesday").toString()
                var cWednesday = it.getString("wednesday").toString()
                var cThursday = it.getString("thursday").toString()
                var cFriday = it.getString("friday").toString()
                var cSaturday = it.getString("saturday").toString()
                var cOpeningHours = it.getString("openingHours").toString()
                var cClosingHours = it.getString("closingHours").toString()

                cafeteriaID =cCafeteriaID
                image = cImage
                location = cLocation
                name = cName
                sellerID = cSellerID
                status = cStatus


                if(cMonday == "1"){
                    mondayCB.isChecked = true
                }
                if(cTuesday == "1"){
                    tuesdayCB.isChecked = true
                }
                if(cWednesday == "1"){
                    wednesdayCB.isChecked = true
                }
                if(cThursday == "1"){
                    thursdayCB.isChecked = true
                }
                if(cFriday == "1"){
                    fridayCB.isChecked = true
                }
                if(cSaturday == "1"){
                    saturdayCB.isChecked = true
                }

                openingHoursButton.text = cOpeningHours
                closingHoursButton.text = cClosingHours

                val del = ":"
                val openingArray = cOpeningHours.split(del).toTypedArray()
                val closingArray = cClosingHours.split(del).toTypedArray()

                openingHour = openingArray[0]
                openingMinutes = openingArray[1]

                closingHour = closingArray[0]
                closingMinutes = closingArray[1]

                if(cOpeningHours != ""){
                    opening = cOpeningHours
                }

                if (cClosingHours != "") {
                    closing = cClosingHours
                }

                mondayRA = cMonday
                tuesdayRA = cTuesday
                wednesdayRA = cWednesday
                thursdayRA = cThursday
                fridayRA = cFriday
                saturdayRA = cSaturday
                openRA = cOpeningHours
                closeRA = cClosingHours
            }
    }


    private fun getCurrentImgSrc(){
        val intent = intent
        foodStall = intent.getStringExtra("foodStall")
        foodStallReassigned = foodStall
    }

}


