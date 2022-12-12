package com.example.fyp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fyp.Entity.*
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.math.RoundingMode
import java.text.DecimalFormat

class MainActivity : AppCompatActivity() {
    lateinit var toggle: ActionBarDrawerToggle
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var userObj:User
    private val reloadNeed = true
    private lateinit var recyclerView: RecyclerView
    private lateinit var foodArrayList: ArrayList<Food>
    private lateinit var foodAdapter:FoodAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawerLayout)
        val navView: NavigationView = findViewById(R.id.nav_view)

        auth = FirebaseAuth.getInstance()

        val user = auth.currentUser
        db = Firebase.firestore

        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        recyclerView = findViewById(R.id.rvMainTop)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        foodArrayList = arrayListOf()

        foodAdapter = FoodAdapter(foodArrayList)

        recyclerView.adapter = foodAdapter



        if (user != null) {

            Log.i("Login Success", user.email.toString())

            val userRef = db.collection("user").document(user?.email.toString())
            userRef.get().addOnSuccessListener {
                if (it != null) {
                    userObj = it.toObject(User::class.java)!!

                    if (userObj.role == "Customer") {
                        val na = findViewById<TextView>(R.id.user_name)
                        na.text = userObj.name

                        val mm = findViewById<TextView>(R.id.user_emmail)
                        mm.text = userObj.email

                        setDataInList()

                        foodAdapter.onItemClick = {
                            val intent = Intent(this, User_MenuVariant::class.java)
                            intent.putExtra("food", it)
                            startActivity(intent)
                        }

                        val btnStartOrderrr = findViewById<Button>(R.id.btnStartOrderrr)
                        val imgMainYumYum = findViewById<ImageView>(R.id.imgMainYumYum)
                        val imgMainRedbrick = findViewById<ImageView>(R.id.imgMainRedbrick)
                        val imgMainCasuarina = findViewById<ImageView>(R.id.imgMainCasuarina)

                        imgMainYumYum.setOnClickListener(){
                            var cafeObj:Cafeteria
                            db.collection("cafeteria")
                                .whereEqualTo("name", "Yum Yum Cafeteria")
                                .get()
                                .addOnSuccessListener { documentssx ->

                                    for (documentsssx in documentssx) {
                                        cafeObj = documentsssx.toObject(Cafeteria::class.java)
                                        val intent = Intent(
                                            this, User_Foodstall::class.java
                                        )
                                        intent.putExtra("cafeteria", cafeObj)
                                        startActivity(intent)
                                    }


                                }

                        }

                        imgMainRedbrick.setOnClickListener(){
                            var cafeObj:Cafeteria
                            db.collection("cafeteria")
                                .whereEqualTo("name", "The Red Bricks Cafeteria")
                                .get()
                                .addOnSuccessListener { documentssx ->

                                    for (documentsssx in documentssx) {
                                        cafeObj = documentsssx.toObject(Cafeteria::class.java)
                                        val intent = Intent(
                                            this, User_Foodstall::class.java
                                        )
                                        intent.putExtra("cafeteria", cafeObj)
                                        startActivity(intent)
                                    }


                                }
                        }

                        imgMainCasuarina.setOnClickListener(){
                            var cafeObj:Cafeteria
                            db.collection("cafeteria")
                                .whereEqualTo("name", "Casuarina Cafe")
                                .get()
                                .addOnSuccessListener { documentssx ->

                                    for (documentsssx in documentssx) {
                                        cafeObj = documentsssx.toObject(Cafeteria::class.java)
                                        val intent = Intent(
                                            this, User_Foodstall::class.java
                                        )
                                        intent.putExtra("cafeteria", cafeObj)
                                        startActivity(intent)
                                    }


                                }
                        }

                        btnStartOrderrr.setOnClickListener(){
                            val intent = Intent(
                                this, User_Cafeteria::class.java
                            )
                            startActivity(intent)
                        }


                        navView.setNavigationItemSelectedListener {
                            when (it.itemId) {
                                R.id.nav_home -> {
                                    Toast.makeText(
                                        applicationContext,
                                        "Clicked Home",
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
                                        "Clicked Order",
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
                                    val userRef2 = db.collection("user").document(user?.email.toString())
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
                                        "Clicked Order History",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    val intent = Intent(
                                        this, User_OrderHistory::class.java
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

                                R.id.nav_eatogether -> {
                                    Toast.makeText(
                                        applicationContext,
                                        "Clicked EatTogether",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    val intent = Intent(
                                        this, User_EatTogether::class.java
                                    )
                                        .putExtra("userObj", userObj)
                                    startActivity(intent)
                                }
                                R.id.nav_viewInvite -> {
                                    Toast.makeText(
                                        applicationContext,
                                        "Clicked EatTogether",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    val intent = Intent(
                                        this, User_EatTogether_Invitation::class.java
                                    )
                                        .putExtra("userObj", userObj)
                                    startActivity(intent)
                                }

                                R.id.nav_logout -> {
                                    Toast.makeText(
                                        applicationContext,
                                        "Clicked Logout",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    val intent = Intent(
                                        this, Login::class.java
                                    )
                                    startActivity(intent)
                                    finish()
                                }

                            }
                            true
                        }






                    }else{
                        //Seller's UI
                        /*navView.setNavigationItemSelectedListener {
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
                        }*/



                        val intent = Intent(this, Seller_ShopManager::class.java)
                        intent.putExtra("userObj", userObj)
                        startActivity(intent)
                        finish()


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

    private fun setDataInList(){

        var qtt:ArrayList<Int>


        db = FirebaseFirestore.getInstance()
        db.collection("food")
            .whereEqualTo("status", "Active")
            .get()
            .addOnSuccessListener { documentxx ->

                for (documentx in documentxx) {
                    foodArrayList.add(documentx.toObject(Food::class.java))
                }

                foodAdapter.notifyDataSetChanged()
            }

        /*db = FirebaseFirestore.getInstance()
        db.collection("food").addSnapshotListener(object : EventListener<QuerySnapshot> {

            override fun onEvent(
                value: QuerySnapshot?,
                error: FirebaseFirestoreException?
            ){
                if(error != null){
                    Log.e("Firestore Error", error.message.toString())
                    return

                }

                    for (dc: DocumentChange in value?.documentChanges!!) {

                        if (dc.type == DocumentChange.Type.ADDED) {
                            foodArrayList.add((dc.document.toObject(Food::class.java)))
                            if (foodArrayList.get(foodArrayList.size - 1).status == "Inactive" || foodArrayList.get(
                                    foodArrayList.size - 1
                                ).foodstallID != foodstallname || filter != foodArrayList.get(
                                    foodArrayList.size - 1
                                ).type
                            ) {

                                foodArrayList.removeAt(foodArrayList.size - 1)


                            }
                        }
                    }

                foodAdapter.notifyDataSetChanged()
            }
        })*/



    }
}
