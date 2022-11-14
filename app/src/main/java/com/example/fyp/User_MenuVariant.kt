package com.example.fyp

import android.content.ContentValues.TAG
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.RadioGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fyp.Entity.Food
import com.example.fyp.Entity.FoodStall
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.storage.FirebaseStorage
import java.io.File

class User_MenuVariant : AppCompatActivity() {
    private  var quantity: Int = 1
    private lateinit var db : FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_menu_variant)

        val food  = intent.getParcelableExtra<Food>("food")


        if(food!= null){



            val tvFoodNameChoose = findViewById<TextView>(R.id.tvFoodNameChoose)
            val tvFoodStallsNames = findViewById<TextView>(R.id.tvFoodStallsNames)
            val imgFoodDetail = findViewById<ImageView>(R.id.imgFoodDetail)
            val radioGroup = findViewById<RadioGroup>(R.id.rgFood)
            val btnDecrease = findViewById<Button>(R.id.btnDecrease)
            val btnIncrease = findViewById<Button>(R.id.btnIncrease)
            val tvQuantity = findViewById<TextView>(R.id.tvQuantity)
            val btnAddToCart = findViewById<Button>(R.id.btnAddToCart)
            val tfRemark = findViewById<TextView>(R.id.tfRemark)
            var im:String = ""

//            db = FirebaseFirestore.getInstance()
//
//            db.collection("food")
//                .whereEqualTo("name",food.name).get()
//                .addOnSuccessListener { result ->
//                    for (document in result) {
//                         foods = document.toObject(Food::class.java)
//                        im = foods.image.toString()
//                        Log.d(TAG, "${document.id} => ${document.data}")
//                    }
//                }
//                .addOnFailureListener { exception ->
//                    Log.w(TAG, "Error getting documents: ", exception)
//                }

            tvFoodNameChoose.text = food.name
                tvFoodStallsNames.text = food.foodstallID


            var imgName = food.image
            val storageRef = FirebaseStorage.getInstance().reference.child("foodimg/$imgName")
            val localfile = File.createTempFile("tempImage", "png")

            storageRef.getFile(localfile).addOnSuccessListener {

                val bitmap = BitmapFactory.decodeFile(localfile.absolutePath)
                imgFoodDetail.setImageBitmap(bitmap)
            }

            btnDecrease.setOnClickListener(){
                if(quantity != 1){
                    quantity -= 1
                }
                tvQuantity.text = quantity.toString()

            }

            btnIncrease.setOnClickListener(){
                quantity += 1
                tvQuantity.text = quantity.toString()
            }

            btnAddToCart.setOnClickListener(){

                val spicyLevel = radioGroup.checkedRadioButtonId
                val result = when(spicyLevel){
                    R.id.rbNoSpicy-> "No"
                    R.id.rbLessSpicy -> "Less"
                    R.id.rbSpicy -> "Yes"

                    else -> "Error"
                }

                val qtt = tvQuantity.text
                val remark = tfRemark.text


                /*val db = FirebaseFirestore.getInstance()
                val cartDetail = hashMapOf(
                    "address" to tfAddress.text.trim().toString(),
                    "dateEnd" to btnEndDate.text.toString(),
                    "dateStart" to btnStartDate.text.toString(),
                )

                db.collection("events")
                    .document(currentDate.toString()).set(event)
                    .addOnSuccessListener { Toast.makeText(this,"Added Successfully", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                    .addOnFailureListener {Toast.makeText(this,"Added Failed", Toast.LENGTH_SHORT).show()}*/




            }




        }






    }
}