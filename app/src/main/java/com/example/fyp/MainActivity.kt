package com.example.fyp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import java.nio.channels.DatagramChannel.open

class MainActivity : AppCompatActivity() {
    lateinit var toggle: ActionBarDrawerToggle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val drawerLayout : DrawerLayout = findViewById(R.id.drawerLayout)
val navView : NavigationView = findViewById(R.id.nav_view)

        toggle = ActionBarDrawerToggle(this, drawerLayout,R.string.open,R.string.close )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        navView.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.nav_home->{
                    Toast.makeText(
                        applicationContext,
                        "Clicked Donation",
                        Toast.LENGTH_SHORT
                    ).show()
                    val intent = Intent(
                        this, MainActivity::class.java
                    )
                    startActivity(intent)
                }
                R.id.nav_order->{
                    Toast.makeText(
                        applicationContext,
                        "Clicked Donation",
                        Toast.LENGTH_SHORT
                    ).show()
                    val intent = Intent(
                        this, User_Cafeteria::class.java
                    )
                    startActivity(intent)
                }
            }
            true
        }

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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item)){
            return true
        }

        return super.onOptionsItemSelected(item)
    }
}