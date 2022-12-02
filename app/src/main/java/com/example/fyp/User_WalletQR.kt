package com.example.fyp

import android.graphics.Bitmap
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import com.example.fyp.Entity.Ewallet
import com.example.fyp.Entity.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.google.zxing.qrcode.QRCodeWriter

class User_WalletQR : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var userObj: User
    private lateinit var walletObj: Ewallet

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_wallet_qr)

        auth = FirebaseAuth.getInstance()

        val user = auth.currentUser
        db = Firebase.firestore

        val userRef = db.collection("user").document(user?.email.toString())
        userRef.get().addOnSuccessListener {
            if (it != null) {
                userObj = it.toObject(User::class.java)!!
                Log.i("kongannnnnnnnnnnnnnnnnn", userObj.toString())

                db = FirebaseFirestore.getInstance()
                db.collection("ewallet")
                    .whereEqualTo("userID", userObj.id)
                    .get()
                    .addOnSuccessListener { documents ->
                        Log.i("kongannnnnnnnnnnnnnnnnn", userObj.toString())

                        for (document in documents) {
                            walletObj = document.toObject(Ewallet::class.java)
                        }


                        val imgQR = findViewById<ImageView>(R.id.imgQR)

                        val data = "{'walletID':'${walletObj.walletID}'}"

                        val writer = QRCodeWriter()
                        try {
                            val bitMatrix = writer.encode(data, BarcodeFormat.QR_CODE, 512, 512)
                            val width = bitMatrix.width
                            val height = bitMatrix.height
                            val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
                            for (x in 0 until width) {
                                for (y in 0 until height) {
                                    bmp.setPixel(
                                        x,
                                        y,
                                        if (bitMatrix[x, y]) Color.BLACK else Color.WHITE
                                    )
                                }
                            }
                            imgQR.setImageBitmap(bmp)
                        } catch (e: WriterException) {
                            e.printStackTrace()
                        }
                    }
            }
        }

    }
}