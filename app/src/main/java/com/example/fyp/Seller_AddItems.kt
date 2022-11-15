package com.example.fyp

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class Seller_AddItems : AppCompatActivity() {
    private lateinit var imgUri: Uri
    private lateinit var ref: StorageReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seller_add_items)

        ref = FirebaseStorage.getInstance().reference
        val btnNewItemImg = findViewById<Button>(R.id.btnNewItemImg)
        val btnAddNewItem = findViewById<Button>(R.id.btnAddNewItem)
        val tvNewItemName = findViewById<TextView>(R.id.tvNewItemName)
        val tvDesc = findViewById<TextView>(R.id.tvDesc)
        val tvPrice = findViewById<TextView>(R.id.tvPrice)
        val tvCalories = findViewById<TextView>(R.id.tvCalories)
        val imgName = findViewById<ImageView>(R.id.imgAddNewItem)

        btnNewItemImg.setOnClickListener(){
            getImage.launch("image/*")
        }

        btnAddNewItem.setOnClickListener(){
            if(tvNewItemName.text.trim().isEmpty()){
                Toast.makeText(applicationContext,"Please enter name for the item", Toast.LENGTH_SHORT).show()
            }else if(tvDesc.text.trim().isEmpty()){
                Toast.makeText(applicationContext,"Please enter description for the item", Toast.LENGTH_SHORT).show()
            }else if(tvPrice.text.trim().isEmpty()){
                Toast.makeText(applicationContext,"Please enter price for the item", Toast.LENGTH_SHORT).show()
            }else if(tvCalories.text.trim().isEmpty()){
                Toast.makeText(applicationContext,"Please enter calories for the item", Toast.LENGTH_SHORT).show()
            }else if(imgName.drawable == null){
                Toast.makeText(applicationContext,"Please insert images for the item", Toast.LENGTH_SHORT).show()
            }else{
                val imgName = ref.child("foodimg/$imgName.png")
                imgName.putFile(imgUri)
                val db = FirebaseFirestore.getInstance()
                val food = hashMapOf(
                    "calories" to tvCalories.text.trim().toString().toDouble(),
                    "description" to tvDesc.text.trim().toString(),
                    "foodID" to "F0003",
                    "foodstallID" to "Masakan",
                    "image" to "$imgName.png",
                    "name" to tvNewItemName.text.trim().toString(),
                    "price" to tvPrice.text.trim().toString().toDouble(),
                    "status" to "Active"


                )
                db.collection("food")
                    .document(tvNewItemName.text.toString()).set(food)
                    .addOnSuccessListener { Toast.makeText(this,"Added Successfully", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener {Toast.makeText(this,"Added Failed", Toast.LENGTH_SHORT).show()
                    }
                finish()
            }
        }

    }

    private val getImage = registerForActivityResult(
        ActivityResultContracts.GetContent()){ uri ->

        val btnNewItemImg = findViewById<Button>(R.id.btnNewItemImg)
        imgUri = uri!!

        val imgAddNewItem = findViewById<ImageView>(R.id.imgAddNewItem)
        imgAddNewItem.setImageURI(uri)
    }
}