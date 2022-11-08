package com.example.fyp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val btnweizhe = findViewById<Button>(R.id.btnweizhe)
        val btnchunwai = findViewById<Button>(R.id.btnchunwai)

        btnweizhe.setOnClickListener(){
            val intent = Intent(this, User_Cafeteria::class.java)
            startActivity(intent)
        }

        btnchunwai.setOnClickListener(){
            val intent = Intent(this, Seller_ShopManager::class.java)
            startActivity(intent)
        }

    }
}