package com.example.fyp

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.format.DateFormat.is24HourFormat
import android.widget.*
import com.example.fyp.Entity.FoodStall
import com.google.firebase.firestore.FirebaseFirestore
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seller_manage_food_stall)

        ref = FirebaseStorage.getInstance().reference
        val foodstall = intent.getParcelableExtra<FoodStall>("foodstall")


        if (foodstall != null){
            val ptStallName = findViewById<TextView>(R.id.ptStallName)
            val cbMonday = findViewById<CheckBox>(R.id.cbMonday)
            val cbTuesday = findViewById<CheckBox>(R.id.cbTuesday)
            val cbWednesday = findViewById<CheckBox>(R.id.cbWednesday)
            val cbThursday = findViewById<CheckBox>(R.id.cbThursday)
            val cbFriday = findViewById<CheckBox>(R.id.cbFriday)
            val cbSaturday = findViewById<CheckBox>(R.id.cbSaturday)
            val btnOpening = findViewById<Button>(R.id.btnOpening)
            val btnClosing = findViewById<Button>(R.id.btnClosing)
            val btnUpdate = findViewById<Button>(R.id.btnUpdate)

            btnOpening.text = foodstall.opening
            btnClosing.text = foodstall.closing
            cbMonday.text = foodstall.operatingDay
            cbTuesday.text = foodstall.operatingDay
            cbWednesday.text = foodstall.operatingDay
            cbThursday.text = foodstall.operatingDay
            cbFriday.text = foodstall.operatingDay
            cbSaturday.text = foodstall.operatingDay

            btnOpening.setOnClickListener(){
                getTimeCalender()
                num = 1
                TimePickerDialog(this, this, hour, minute, true).show()
            }

            btnClosing.setOnClickListener(){
                getTimeCalender()
                num = 2
                TimePickerDialog(this, this, hour, minute, true).show()
            }

            btnUpdate.setOnClickListener(){
                if(cbMonday.isChecked){
                    Toast.makeText(applicationContext, "Monday is checked", Toast.LENGTH_LONG).show()
                }
                else if(cbTuesday.isChecked){
                Toast.makeText(applicationContext, "Tuesday is checked", Toast.LENGTH_LONG).show()
                }else{
                    val db = FirebaseFirestore.getInstance()
                    val foodstall = hashMapOf(
                        "opening" to btnOpening.text.toString(),
                        "closing" to btnClosing.text.toString(),
                    )
                }
            }
        }
    }

    private fun setTimeText (num:Int){
        val btnOpening = findViewById<Button>(R.id.btnOpening)
        val btnClosing = findViewById<Button>(R.id.btnClosing)

        if(num == 1){
            opening = datetime
            btnOpening.text = datetime

        }else{
            closing = datetime
            btnClosing.text = datetime
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
}


