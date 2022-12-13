package com.example.fyp

import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.fyp.Entity.User
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.File
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class User_ProfileEdit : AppCompatActivity() {
    private lateinit var btnDatePicker: Button

    private lateinit var imgUri: Uri
    private lateinit var ref: StorageReference
    private lateinit var tempImg:String
    private lateinit var userObj:User
    var oriImg:Boolean =true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile_edit)


         userObj  = intent.getParcelableExtra<User>("userObj")!!

        ref = FirebaseStorage.getInstance().reference

        if(userObj!= null) {

            tempImg = userObj.image.toString()
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
            val imgProfEdit = findViewById<ImageView>(R.id.imgProfEdit)

            if(userObj.image != ""){
                var imgName = userObj.image
                val storageRef = FirebaseStorage.getInstance().reference.child("userimg/$imgName")
                val localfile = File.createTempFile("tempImage", "png")

                storageRef.getFile(localfile).addOnSuccessListener {

                    val bitmap = BitmapFactory.decodeFile(localfile.absolutePath)
                    imgProfEdit.setImageBitmap(bitmap)
                }
            }else{
                imgProfEdit.setImageDrawable(getResources().getDrawable(R.drawable.prof));

            }

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

            imgProfEdit.setOnClickListener(){
                getImage.launch("image/*")


            }

            btnProfileCancel.setOnClickListener(){
                finish()
            }

            btnProfileUpdate.setOnClickListener(){

                //validation




                if( !oriImg){
                    val imgName = ref.child("userimg/${userObj.id}.png")
                    userObj.image = tempImg
                    imgName.putFile(imgUri)
                        .addOnSuccessListener {



                        }
                        .addOnFailureListener(){ ex ->

                        }
                }



                val gen = rgProfileGender.checkedRadioButtonId
                val result = when(gen){
                    R.id.rdProfileMale-> "Male"
                    R.id.rbProfileFemale -> "Female"

                    else -> "Error"
                }


                if (TextUtils.isEmpty(tvEditName.text)) {
                    Toast.makeText(applicationContext, "Please enter name!", Toast.LENGTH_LONG).show()
                } else if (TextUtils.isEmpty(tvEditContact.text)) {
                    Toast.makeText(applicationContext, "Please enter name!", Toast.LENGTH_LONG).show()
                }else if (tvEditContact.text.length < 9 || tvEditContact.text.length>10) {
                    Toast.makeText(applicationContext, "Contact No length should be 9 or 10", Toast.LENGTH_LONG).show()

                }else if (btnDatePicker.text == "Button") {
                    Toast.makeText(applicationContext, "Please Select Date!", Toast.LENGTH_LONG).show()
                }else  {
                    var string22: String = btnDatePicker.text.toString()
                    var yourArray: List<String> = string22.split("-")

                    val sdf = SimpleDateFormat("yyyy")

                    val currentDate = sdf.format(Date())
                    var test:Int = currentDate.toString().toInt().minus(17)

                    if(yourArray[2].toInt() >= test){
                        Toast.makeText(applicationContext, "The birth date must before year $test", Toast.LENGTH_LONG).show()
                    }else{
                        val profileDetail = hashMapOf(
                            "name" to tvEditName.text.toString(),
                            "gender" to result,
                            "birth" to btnDatePicker.text.toString(),
                            "contactNo" to tvEditContact.text.toString(),
                            "id" to userObj.id,
                            "role" to userObj.role,
                            "email" to userObj.email,
                            "image" to tempImg,
                            "status" to userObj.status,
                        )

                        userObj.birth = btnDatePicker.text.toString()
                        userObj.contactNo = tvEditContact.text.toString()
                        userObj.gender = result
                        userObj.name = tvEditName.text.toString()


                        db.collection("user")
                            .document(userObj.email.toString()).set(profileDetail)
                            .addOnSuccessListener { Toast.makeText(this,"Edit Successfully", Toast.LENGTH_SHORT).show()
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


        }
    }

    private fun updateLabel(myCalendar: Calendar){
        val myFormat = "dd-MM-yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.UK)


        btnDatePicker.text = (sdf.format(myCalendar.time))
    }

    private val getImage = registerForActivityResult(
        ActivityResultContracts.GetContent()){ uri ->

        if(uri!= null){
            imgUri = uri
            val imgProfile = findViewById<ImageView>(R.id.imgProfEdit)
            imgProfile.setImageURI(uri)
            tempImg = userObj.id.toString()+".png"

            oriImg = false
        }



    }


}