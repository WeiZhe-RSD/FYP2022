package com.example.fyp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.fyp.Entity.Ewallet
import com.google.firebase.firestore.FirebaseFirestore

class User_SetPin : AppCompatActivity() {
    private lateinit var db : FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_set_pin)

        var wallet = intent.getParcelableExtra<Ewallet>("wallet")

        if(wallet!=null){
            val tfSetPin = findViewById<TextView>(R.id.tfSetPin)
            val btnSetPin = findViewById<Button>(R.id.btnSetPin)


            btnSetPin.setOnClickListener(){
                db = FirebaseFirestore.getInstance()

                if(tfSetPin.length() != 6){
                    Toast.makeText(
                        applicationContext,
                        "Please Enter 6-digit Pin Number !",
                        Toast.LENGTH_SHORT
                    ).show()
                }else{
                    val washingtonRef2 = db.collection("ewallet").document(wallet.walletID.toString())
                    washingtonRef2
                        .update("pinNo", tfSetPin.text.toString())

                    washingtonRef2.update("status", "Active")

                    val intent = Intent(this, User_Wallet::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }


    }
}