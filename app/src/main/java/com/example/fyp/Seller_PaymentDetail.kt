package com.example.fyp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.fyp.Entity.Payment
import com.google.firebase.firestore.FirebaseFirestore

class Seller_PaymentDetail : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seller_payment_detail)

        var payment = intent.getParcelableExtra<Payment>("payment")

        if(payment!=null){

            val tvCashPaymentID = findViewById<TextView>(R.id.tvCashPaymentID)
            val tvCashOrderID = findViewById<TextView>(R.id.tvCashOrderID)
            val tvCashAmount = findViewById<TextView>(R.id.tvCashAmount)
            val tvCashDate = findViewById<TextView>(R.id.tvCashDate)
            val tvCashStatus = findViewById<TextView>(R.id.tvCashStatus)

            tvCashPaymentID.text = payment.paymentID
            tvCashOrderID.text = payment.orderID
            tvCashAmount.text = payment.amount
            tvCashDate.text = payment.date
            tvCashStatus.text = payment.status

            val btnReceiveCash = findViewById<Button>(R.id.btnReceiveCash)

            btnReceiveCash.setOnClickListener(){
                var db = FirebaseFirestore.getInstance()
                val washingtonRef = db.collection("payment").document(payment.paymentID.toString())
                washingtonRef
                    .update("status", "Completed")

                Toast.makeText(this, "Updated Successfully", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }
}