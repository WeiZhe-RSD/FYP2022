package com.example.fyp

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.fyp.Entity.Cafeteria
import com.example.fyp.Entity.CartDetail
import com.example.fyp.Entity.User
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class   Register : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private val role:String = "Customer"
    private var count:Int = 0
    private lateinit var userArrayList: ArrayList<User>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        userArrayList = arrayListOf()

        val btnRegistered = findViewById<Button>(R.id.btnRegistered)
        val btnCancel = findViewById<Button>(R.id.btnCancel)

        auth = FirebaseAuth.getInstance();
        db = Firebase.firestore

        btnRegistered.setOnClickListener() {
            registerNewUser()
        }

        btnCancel.setOnClickListener(){
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
            finish()
        }

    }

    private fun registerNewUser() {

        val tfRegisterName = findViewById<TextView>(R.id.tfRegisterName)
        val tfRegisterEmail = findViewById<TextView>(R.id.tfRegisterEmail)
        val tfRegisterPassword = findViewById<TextView>(R.id.tfRegisterPassword)
        val tfComfirmPassword = findViewById<TextView>(R.id.tfComfirmPassword)
        val rgGender = findViewById<RadioGroup>(R.id.rgGender)
        val rbFemale = findViewById<RadioButton>(R.id.rbFemale)
        val rdMale = findViewById<RadioButton>(R.id.rdMale)
        val registerContact = findViewById<TextView>(R.id.registerContact)

        var contact:String = registerContact.text.toString()
        var email: String = tfRegisterEmail.text.toString().lowercase()
        var name:String = tfRegisterName.text.toString()
        var password: String = tfRegisterPassword.text.toString()
        var RepeatPassword: String = tfComfirmPassword.text.toString()

        var temp:Int = 0

        var strinID:String = ""
        var check:Boolean = true





        val spicyLevel = rgGender.checkedRadioButtonId
        val result = when(spicyLevel){
            R.id.rdMale-> "Male"
            R.id.rbFemale -> "Female"

            else -> "Error"
        }

        if (TextUtils.isEmpty(name)) {
            Toast.makeText(applicationContext, "Please enter name!", Toast.LENGTH_LONG).show()
            return;
        }else if (result.isEmpty()) {
            Toast.makeText(applicationContext, "Please enter gender!", Toast.LENGTH_LONG).show()
            return;}
        else if (TextUtils.isEmpty(email)) {
            Toast.makeText(applicationContext, "Please enter email!", Toast.LENGTH_LONG).show()
            return;
        }else if (TextUtils.isEmpty(contact)) {
            Toast.makeText(applicationContext, "Please enter Contact No!", Toast.LENGTH_LONG).show()
            return;
        }else if (TextUtils.isEmpty(password)) {
            Toast.makeText(applicationContext, "Please enter password!", Toast.LENGTH_LONG).show()
            return;
        }else if (TextUtils.isEmpty(RepeatPassword)) {
            Toast.makeText(applicationContext, "Please enter Repeat Password!", Toast.LENGTH_LONG)
                .show()
            return;
        }else if (contact.length < 9 || contact.length>10) {
            Toast.makeText(applicationContext, "Incorrect Contact No Format!", Toast.LENGTH_LONG).show()
            return;
        }else if(password != RepeatPassword){
            Toast.makeText(applicationContext,"Password and Confirm Password do not match",Toast.LENGTH_LONG).show()
            return;
        }else {
            db = FirebaseFirestore.getInstance()
            db.collection("user")
                .get()
                .addOnSuccessListener { documentss ->

                    for (documentsss in documentss) {
                        userArrayList.add(documentsss.toObject(User::class.java))

                        if(email == userArrayList.get(userArrayList.size-1).email){
                            check = false
                        }
                    }

                    if(check == true){

            count = userArrayList.size
            temp = count + 1

            if(count<10){
                strinID = "C000$temp"
            }else if (count<100){
                strinID = "C00$temp"
            }else if (count<1000){
                strinID = "C0$temp"
            }else{
                strinID = "C$temp"
            }

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener() { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(
                            applicationContext,
                            "Registration successful!",
                            Toast.LENGTH_LONG
                        ).show()
                        val user = hashMapOf(
                            "id" to strinID,
                            "name" to name,
                            "gender" to result,
                            "email" to email,
                            "role" to role,
                            "status" to "Active",
                            "contactNo" to contact,
                            "image" to "",

                        )




                        db.collection("user")
                            .document(email)
                            .set(user)
                            .addOnSuccessListener { documentReference ->
                                Log.d(
                                    "Firebase",
                                    "DocumentSnapshot added with ID: ${documentReference.toString()}"
                                )
                            }
                            .addOnFailureListener { e ->
                                Log.w("Firebase", "Error adding document", e)
                            }


                        var cartCount:Int = 0
                        var caID:String = ""
                        val collection1 = db.collection("cart")
                        val countQuery1 = collection1.count()
                        countQuery1.get(AggregateSource.SERVER).addOnCompleteListener { tasks ->
                            if (tasks.isSuccessful) {
                                val snapshot1 = tasks.result
                                cartCount = snapshot1.count.toInt()
                                cartCount += 1

                                if(cartCount < 10){
                                    caID = "K000$cartCount"
                                } else if(cartCount < 100){
                                    caID = "K00$cartCount"
                                }else if(cartCount < 1000){
                                    caID = "K0$cartCount"
                                }else if(cartCount < 10000){
                                    caID = "K$cartCount"
                                }

                                val cart = hashMapOf(
                                    "cartID" to caID,
                                    "customerID" to strinID,
                                )




                                db.collection("cart")
                                    .document(caID)
                                    .set(cart)
                                    .addOnSuccessListener {
                                    }

                            }
                        }


                        var walletCount:Int = 0
                        var wallID:String = ""
                        val collection2 = db.collection("ewallet")
                        val countQuery2 = collection2.count()
                        countQuery2.get(AggregateSource.SERVER).addOnCompleteListener { taskss ->
                            if (taskss.isSuccessful) {
                                val snapshot2 = taskss.result
                                walletCount = snapshot2.count.toInt()
                                walletCount += 1

                                if(walletCount < 10){
                                    wallID = "W000$walletCount"
                                } else if(walletCount < 100){
                                    wallID = "W00$walletCount"
                                }else if(walletCount < 1000){
                                    wallID = "W0$walletCount"
                                }else if(walletCount < 10000){
                                    wallID = "W$walletCount"
                                }

                                val wallet = hashMapOf(
                                    "balance" to "0",
                                    "pinNo" to null,
                                    "status" to "Inactive",
                                    "userID" to strinID,
                                    "walletID" to wallID,

                                )




                                db.collection("ewallet")
                                    .document(wallID)
                                    .set(wallet)
                                    .addOnSuccessListener {
                                    }

                            }
                        }



                        val intent = Intent(this, Login::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        try {
                            Log.e("Register error", task.exception.toString())
                            throw task.exception!!
                        } catch (e: FirebaseAuthWeakPasswordException) {
                            throw e
                        } catch (e: FirebaseAuthInvalidCredentialsException) {
                            throw e
                        } catch (e: FirebaseAuthUserCollisionException) {
                            throw e
                        } catch (e: FirebaseException) {

                        }
                        Toast.makeText(this, "Singed Up Failed!", Toast.LENGTH_SHORT).show()
                    }
                }
                }else{
                        Toast.makeText(applicationContext,"This Email is taken",Toast.LENGTH_LONG).show()
                    }

            }
        }

    }
}