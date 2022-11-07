package com.example.fyp

import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.RadioGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fyp.Entity.Food
import com.example.fyp.Entity.FoodStall
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.io.File

class User_MenuVariant : AppCompatActivity() {
    private  var quantity: Int = 1

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

            tvFoodNameChoose.text = food.name
            tvFoodStallsNames.text = food.foodstallID

            var imgName = food.image.toString()
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



        }






    }
}