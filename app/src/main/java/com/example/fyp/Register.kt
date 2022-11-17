package com.example.fyp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Register : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private val role:String = "Customer"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

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

        var email: String = tfRegisterEmail.text.toString().lowercase()
        var name:String = tfRegisterName.text.toString()
        var password: String = tfRegisterPassword.text.toString()
        var RepeatPassword: String = tfComfirmPassword.text.toString()

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
        }else if (TextUtils.isEmpty(password)) {
            Toast.makeText(applicationContext, "Please enter password!", Toast.LENGTH_LONG).show()
            return;
        }else if (TextUtils.isEmpty(RepeatPassword)) {
            Toast.makeText(applicationContext, "Please enter Repeat Password!", Toast.LENGTH_LONG)
                .show()
            return;
        }else if(password != RepeatPassword){
            Toast.makeText(applicationContext,"Password and Confirm Password do not match",Toast.LENGTH_LONG).show()
            return;
        }else {

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener() { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(
                            applicationContext,
                            "Registration successful!",
                            Toast.LENGTH_LONG
                        ).show()
                        val user = hashMapOf(
                            "name" to name,
                            "gender" to result,
                            "email" to email,
                            "role" to role
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
        }

    }
}