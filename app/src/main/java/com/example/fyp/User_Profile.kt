package com.example.fyp

import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.example.fyp.Entity.User
import com.google.firebase.storage.FirebaseStorage
import java.io.File

class User_Profile : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

        val userObj  = intent.getParcelableExtra<User>("userObj")!!

        if(userObj!= null) {
            val tvProfileName = findViewById<TextView>(R.id.tvProfileName)
            val tvProfileGender = findViewById<TextView>(R.id.tvProfileGender)
            val tvProfileEmail = findViewById<TextView>(R.id.tvProfileEmail)
            val tvProfileBirth = findViewById<TextView>(R.id.tvProfileBirth)
            val tvProfileContact = findViewById<TextView>(R.id.tvProfileContact)
            val btnProfileEdit = findViewById<Button>(R.id.btnProfileEdit)
            val imgProfile = findViewById<ImageView>(R.id.imgProfile)

            tvProfileName.text = userObj.name
            tvProfileGender.text = userObj.gender
            tvProfileEmail.text = userObj.email
            if(userObj.birth != null){
                tvProfileBirth.text = userObj.birth
            }else{
                tvProfileBirth.text = "*Plase Provide Your\n Birth Date*"
            }


            if(userObj.contactNo != null){
                tvProfileContact.text = userObj.contactNo.toString()
            }else{
                tvProfileContact.text = "*Plase Provide Your\n Contact No*"
            }

            if(userObj.image != null){
                var imgName = userObj.image
                val storageRef = FirebaseStorage.getInstance().reference.child("userimg/$imgName")
                val localfile = File.createTempFile("tempImage", "png")

                storageRef.getFile(localfile).addOnSuccessListener {

                    val bitmap = BitmapFactory.decodeFile(localfile.absolutePath)
                    imgProfile.setImageBitmap(bitmap)
                }
            }


            btnProfileEdit.setOnClickListener {
                val intent = Intent(
                    this, User_ProfileEdit::class.java
                )
                    .putExtra("userObj", userObj)
                startActivity(intent)
                finish()
            }



        }
    }
}