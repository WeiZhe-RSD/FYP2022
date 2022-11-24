package com.example.fyp

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.example.fyp.Entity.User
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

class User_ProfileEdit : AppCompatActivity() {
    private lateinit var btnDatePicker: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile_edit)

        val userObj  = intent.getParcelableExtra<User>("userObj")!!

        if(userObj!= null) {
            btnDatePicker = findViewById(R.id.datePicker)

            //////////////////////////////////////////////////
            val today = Calendar.getInstance()

            val datePicker = DatePickerDialog.OnDateSetListener{ view, year, month, dateOfMonth ->
                today.set(Calendar.YEAR, year)
                today.set(Calendar.MONTH, month)
                today.set(Calendar.DAY_OF_MONTH, dateOfMonth)
                updateLabel(today)
            }

            btnDatePicker.setOnClickListener(){
                DatePickerDialog(this, datePicker, today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH)).show()
            }
            /////////////////////////////////////////////////
            val db = FirebaseFirestore.getInstance()
            val tvEditName = findViewById<TextView>(R.id.tvEditName)
            val tvEditEmail = findViewById<TextView>(R.id.tvEditEmail)
            val tvEditContact = findViewById<TextView>(R.id.tvEditContact)
            val btnProfileCancel = findViewById<Button>(R.id.btnProfileCancel)
            val btnProfileUpdate = findViewById<Button>(R.id.btnProfileUpdate)
            val rgProfileGender = findViewById<RadioGroup>(R.id.rgProfileGender)

            tvEditName.text = userObj.name
            tvEditEmail.text = userObj.email
            tvEditContact.text = userObj.contactNo

            if(userObj.gender == "Male"){
                rgProfileGender.check(R.id.rdProfileMale)
            }else{
                rgProfileGender.check(R.id.rbProfileFemale)
            }

            if(userObj.birth!=null){
                btnDatePicker.text = userObj.birth
            }



            btnProfileCancel.setOnClickListener(){
                finish()
            }

            btnProfileUpdate.setOnClickListener(){

                val gen = rgProfileGender.checkedRadioButtonId
                val result = when(gen){
                    R.id.rdProfileMale-> "Male"
                    R.id.rbProfileFemale -> "Female"

                    else -> "Error"
                }

                val profileDetail = hashMapOf(
                    "name" to tvEditName.text.toString(),
                    "gender" to result,
                    "birth" to btnDatePicker.text.toString(),
                    "contactNo" to tvEditContact.text.toString(),
                    "id" to userObj.id,
                    "role" to userObj.role,
                    "email" to userObj.email,
                    "image" to userObj.image,
                    "status" to userObj.status,
                )

                userObj.birth = btnDatePicker.text.toString()
                userObj.contactNo = tvEditContact.text.toString()
                userObj.gender = result
                userObj.name = tvEditName.text.toString()

                db.collection("user")
                    .document(userObj.email.toString()).set(profileDetail)
                    .addOnSuccessListener { Toast.makeText(this,"Added Successfully", Toast.LENGTH_SHORT).show()
                        val intent = Intent(
                            this, User_Profile::class.java
                        )
                            .putExtra("userObj", userObj)
                        startActivity(intent)
                        finish()
                    }
                    .addOnFailureListener { Toast.makeText(this,"Added Failed", Toast.LENGTH_SHORT).show()}
            }


        }
    }

    private fun updateLabel(myCalendar: Calendar){
        val myFormat = "dd-MM-yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.UK)


        btnDatePicker.text = (sdf.format(myCalendar.time))
    }


}