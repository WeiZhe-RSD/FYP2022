package com.example.fyp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class Login : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        auth = FirebaseAuth.getInstance()
        auth.currentUser

        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val btnRegister = findViewById<Button>(R.id.btnRegister)


        btnLogin.setOnClickListener(){
            login()
        }

        btnRegister.setOnClickListener() {
            val intent = Intent(this, Register::class.java)

            startActivity(intent)
        }

    }

    private fun login() {
        val tfLoginEmail = findViewById<TextView>(R.id.tfLoginEmail)
        val tfLoginPassword = findViewById<TextView>(R.id.tfLoginPassword)



        if(tfLoginEmail.text.isNotEmpty() && tfLoginPassword.text.isNotEmpty()) {


            val email = tfLoginEmail.text.toString()
            val password = tfLoginPassword.text.toString()

            var auth: FirebaseAuth = FirebaseAuth.getInstance()

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Log.i("Login Success", task.result.toString())
                        Toast.makeText(this, "Successfully LoggedIn", Toast.LENGTH_LONG).show()
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Log.e("Login error", task.exception.toString())
                        Toast.makeText(this, "Incorrect username/password. Please try again", Toast.LENGTH_LONG).show()

                    }
                }
        }

    }
}