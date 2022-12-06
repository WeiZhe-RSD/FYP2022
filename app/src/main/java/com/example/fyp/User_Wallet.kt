package com.example.fyp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class User_Wallet : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_wallet)

        val btnGenQR = findViewById<Button>(R.id.btnGenQR)

        btnGenQR.setOnClickListener(){
            val intent = Intent(
                this, User_WalletQR::class.java
            )
                /*.putExtra("userObj", userObj)*/
            startActivity(intent)
        }
    }
}