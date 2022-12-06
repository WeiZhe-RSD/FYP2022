package com.example.fyp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.example.fyp.Entity.User
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    lateinit var toggle: ActionBarDrawerToggle
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var userObj:User
    private val reloadNeed = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawerLayout)
        val navView: NavigationView = findViewById(R.id.nav_view)

        auth = FirebaseAuth.getInstance()

        val user = auth.currentUser
        val db = Firebase.firestore

        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (user != null) {
            Log.i("Login Success", user.email.toString())

            val userRef = db.collection("user").document(user.email.toString())
            userRef.get().addOnSuccessListener {
                if (it != null) {
                    userObj = it.toObject(User::class.java)!!

                    if (userObj.role == "Customer") {


                        navView.setNavigationItemSelectedListener {
                            when (it.itemId) {
                                R.id.nav_home -> {
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
                                R.id.nav_order -> {
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
                                R.id.nav_cart -> {
                                    Toast.makeText(
                                        applicationContext,
                                        "Clicked Cart",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    val intent = Intent(
                                        this, User_Cart::class.java
                                    )
                                        .putExtra("userObj", userObj)
                                    startActivity(intent)
                                }

                                R.id.nav_profile -> {
                                    val userRef2 = db.collection("user").document(user.email.toString())
                                    userRef2.get().addOnSuccessListener {
                                        userObj = it.toObject(User::class.java)!!
                                    }

                                    Toast.makeText(
                                        applicationContext,
                                        "Clicked Profile",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    val intent = Intent(
                                        this, User_Profile::class.java
                                    )
                                        .putExtra("userObj", userObj)
                                    startActivity(intent)



                                }

                                R.id.nav_history -> {
                                    Toast.makeText(
                                        applicationContext,
                                        "Clicked Cart",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    val intent = Intent(
                                        this, User_Cart::class.java
                                    )
                                        .putExtra("userObj", userObj)
                                    startActivity(intent)
                                }

                                R.id.nav_wallet -> {
                                    Toast.makeText(
                                        applicationContext,
                                        "Clicked Cart",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    val intent = Intent(
                                        this, User_Wallet::class.java
                                    )
                                        .putExtra("userObj", userObj)
                                    startActivity(intent)
                                }
                            }
                            true
                        }

                        val btnweizhe = findViewById<Button>(R.id.btnweizhe)


                        btnweizhe.setOnClickListener {
                            val intent = Intent(this, User_Cafeteria::class.java)
                            startActivity(intent)
                        }



                    }else{
                        //Seller's UI
                        navView.setNavigationItemSelectedListener {
                            when (it.itemId) {
                                R.id.nav_home -> {
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
                                R.id.nav_order -> {
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



                        val btnchunwai = findViewById<Button>(R.id.btnchunwai)

                        btnchunwai.setOnClickListener {
                            val intent = Intent(this, Seller_ShopManager::class.java)

                            intent.putExtra("userObj", userObj)
                            startActivity(intent)


                        }
                    }
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item)){
            return true
        }

        return super.onOptionsItemSelected(item)
    }
}