package com.example.fyp

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import com.example.fyp.Entity.Cafeteria
import com.example.fyp.Entity.Food
import com.example.fyp.Entity.FoodStall
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlin.math.log

class Seller_EditItems : AppCompatActivity() {
    private lateinit var imgUri: Uri
    private lateinit var ref: StorageReference
    private lateinit var db: FirebaseFirestore

    private var foodName: String? = ""
    private var foodNameReassigned: String? = ""
    private var foodStallValidaiton: String? = ""

    private var foodImageFromDB: String? = ""

    private lateinit var imageView: ImageView
    private lateinit var btnNewItem: Button
    private lateinit var btnSaveChanges: Button
    private lateinit var btnSoldOut: Button
    private lateinit var btnInStock: Button
    private lateinit var editItemNameField: EditText
    private lateinit var editDesc: EditText
    private lateinit var editPrice: EditText
    private lateinit var editCalories: EditText

    private lateinit var foodStallID: String

    private lateinit var foodstall: Food


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seller_edit_items)

        foodstall = intent.getParcelableExtra<Food>("foodStall")!!
        Log.i("apapappapaap", foodstall.toString())
        ref = FirebaseStorage.getInstance().reference
        db = Firebase.firestore

        imageView = findViewById(R.id.editImageView)
        btnNewItem = findViewById(R.id.btnNewItemImg)
        btnSaveChanges = findViewById(R.id.btnSaveChanges)

        editItemNameField = findViewById(R.id.editItemName)
        editDesc = findViewById(R.id.editDesc)
        editPrice = findViewById(R.id.editPrice)
        editCalories = findViewById(R.id.editCalories)

        btnSoldOut = findViewById(R.id.btnSoldOut)
        btnInStock = findViewById(R.id.btnInStock)

        //foodStallID = intent.getStringExtra("foodStall").toString();


        //Get String from previous intent
        foodName=foodstall.name
        foodNameReassigned = foodName
        getSpecificFoodData(foodNameReassigned!!)

        btnSaveChanges.setOnClickListener {
            updateFoodData()
        }

        btnInStock.setOnClickListener {
            val updateStatus = db.collection("food").document(foodName.toString())
            updateStatus
                .update("status", "Active")
        }

        btnSoldOut.setOnClickListener {
            val updateStatus = db.collection("food").document(foodName.toString())
            updateStatus
                .update("status", "Inactive")
        }
    }

    private fun updateFoodData() {
        if (editItemNameField.text.trim().isEmpty()) {
            Toast.makeText(
                applicationContext,
                "Please enter name for the item",
                Toast.LENGTH_SHORT
            ).show()
        } else if (editDesc.text.trim().isEmpty()) {
            Toast.makeText(
                applicationContext,
                "Please enter description for the item",
                Toast.LENGTH_SHORT
            ).show()
        } else if (editPrice.text.trim().isEmpty()) {
            Toast.makeText(
                applicationContext,
                "Please enter price for the item",
                Toast.LENGTH_SHORT
            ).show()
        } else if (editCalories.text.trim().isEmpty()) {
            Toast.makeText(
                applicationContext,
                "Please enter calories for the item",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            val db = FirebaseFirestore.getInstance()
            val food = hashMapOf(
                "calories" to editCalories.text.trim().toString(),
                "description" to editDesc.text.trim().toString(),
                "foodID" to foodstall.foodID,
                "foodstallID" to foodstall.foodstallID,
                "image" to "$foodImageFromDB",
                "name" to editItemNameField.text.trim().toString(),
                "price" to editPrice.text.trim().toString(),
                "status" to "Active",
                "type" to foodstall.type
            )
            db.collection("food")
                .document(editItemNameField.text.toString()).set(food)
                .addOnSuccessListener {
                    Toast.makeText(this, "Updated Successfully", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Updated Failed", Toast.LENGTH_SHORT).show()
                }
            finish()
        }
    }

    private fun getSpecificFoodData(foodName: String) {
        db.collection("food").document(foodName).get()
            .addOnSuccessListener {
                var cFoodName = it.getString("name").toString()
                var cPrice = it.getString("price").toString()
                var cCalories = it.getString("calories").toString()
                var cDescription = it.getString("description").toString()

                //not used but need to get as well
                var cImagePath = it.getString("image").toString()
                foodImageFromDB = cImagePath

                editItemNameField.editableText.append(cFoodName)
                editDesc.editableText.append(cDescription)
                editPrice.editableText.append(cPrice)
                editCalories.editableText.append(cCalories)
            }
    }

}


