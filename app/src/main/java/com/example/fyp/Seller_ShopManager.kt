package com.example.fyp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class Seller_ShopManager : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seller_shop_manager)

        val btnManageFoodItems = findViewById<Button>(R.id.btnManageFI)
        val btnManageFoodStall = findViewById<Button>(R.id.btnManageOH)

        btnManageFoodStall.setOnClickListener(){
            val intent = Intent(this@Seller_ShopManager, Seller_ManageFoodStall::class.java)
            startActivity(intent)
        }

        btnManageFoodItems.setOnClickListener(){
            val intent = Intent(this@Seller_ShopManager, Seller_ManageMenuItems::class.java)
            startActivity(intent)
        }

    }

}