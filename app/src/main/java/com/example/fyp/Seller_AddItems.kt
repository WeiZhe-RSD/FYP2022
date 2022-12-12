package com.example.fyp

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.firestore.AggregateSource
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class Seller_AddItems : AppCompatActivity() {
    private lateinit var imgUri: Uri
    private lateinit var ref: StorageReference
    private lateinit var db: FirebaseFirestore

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
        val rgFoodType = findViewById<RadioGroup>(R.id.rgFoodType)

        val itemType = rgFoodType.checkedRadioButtonId
        val result = when(itemType){
            R.id.rdFood -> "Food"
            R.id.rbBeverage -> "Beverage"

            else -> "Error"
        }

        val userObj = intent.getStringExtra("foodStall")

        db = FirebaseFirestore.getInstance()

        if (userObj != null) {

            btnNewItemImg.setOnClickListener {
                getImage.launch("image/*")
            }

            btnAddNewItem.setOnClickListener {
                var foodSize: Int = 0
                var id: String = ""

                if (tvNewItemName.text.trim().isEmpty()) {
                    Toast.makeText(
                        applicationContext,
                        "Please enter name for the item",
                        Toast.LENGTH_SHORT
                    ).show()
                } else if (tvDesc.text.trim().isEmpty()) {
                    Toast.makeText(
                        applicationContext,
                        "Please enter description for the item",
                        Toast.LENGTH_SHORT
                    ).show()
                } else if (tvPrice.text.trim().isEmpty()) {
                    Toast.makeText(
                        applicationContext,
                        "Please enter price for the item",
                        Toast.LENGTH_SHORT
                    ).show()
                } else if (tvCalories.text.trim().isEmpty()) {
                    Toast.makeText(
                        applicationContext,
                        "Please enter calories for the item",
                        Toast.LENGTH_SHORT
                    ).show()
                }else if (imgName.drawable == null) {
                    Toast.makeText(
                        applicationContext,
                        "Please insert images for the item",
                        Toast.LENGTH_SHORT
                    ).show()
                } else if (result.isEmpty()) {
                    Toast.makeText(
                        applicationContext,
                        "Please select item type for the item",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    val regImg = ref.child("foodimg/$imgName.png")
                    regImg.putFile(imgUri)
                    Log.i("File Name", imgName.toString())

                    val collection = db.collection("food")
                    val countQuery = collection.count()
                    countQuery.get(AggregateSource.SERVER)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val snapshot = task.result
                                foodSize = snapshot.count.toInt()
                                foodSize += 1

                                if (foodSize < 10) {
                                    id =
                                        "F000$foodSize"
                                } else if (foodSize < 100) {
                                    id =
                                        "F00$foodSize"
                                } else if (foodSize < 1000) {
                                    id =
                                        "F0$foodSize"
                                } else if (foodSize < 10000) {
                                    id =
                                        "F$foodSize"
                                }

                                val food = hashMapOf(
                                    "calories" to tvCalories.text.trim().toString(),
                                    "description" to tvDesc.text.trim().toString(),
                                    "foodID" to id,
                                    "foodstallID" to userObj,
                                    "image" to "$imgName.png",
                                    "name" to tvNewItemName.text.trim().toString(),
                                    "price" to tvPrice.text.trim().toString(),
                                    "status" to "Active",
                                    "type" to result
                                )
                                db.collection("food")
                                    .document(tvNewItemName.text.toString()).set(food)
                                    .addOnSuccessListener {
                                        Toast.makeText(
                                            this, "Added Successfully", Toast.LENGTH_SHORT)
                                            .show()
                                    }
                                    .addOnFailureListener {
                                        Toast.makeText(this, "Added Failed", Toast.LENGTH_SHORT)
                                            .show()
                                    }
                                finish()
                            }
                        }
                }
            }
        }

    }

    private val getImage = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->

        val btnNewItemImg = findViewById<Button>(R.id.btnNewItemImg)
        imgUri = uri!!

        val imgAddNewItem = findViewById<ImageView>(R.id.imgAddNewItem)
        imgAddNewItem.setImageURI(uri)
    }
}