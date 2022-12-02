package com.example.fyp

import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import com.example.fyp.Entity.Cafeteria
import com.example.fyp.Entity.CartDetail
import com.example.fyp.Entity.Ewallet
import com.example.fyp.Entity.Food
import com.google.firebase.firestore.AggregateSource
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.storage.FirebaseStorage
import java.io.File

class User_EditCart : AppCompatActivity() {
    private var quantity: Int = 1
    private lateinit var db: FirebaseFirestore
    private lateinit var foodObj: Food
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_edit_cart)

        val cart = intent.getParcelableExtra<CartDetail>("cart")

        if (cart != null) {
            var detailSize: Int = 0
            db = FirebaseFirestore.getInstance()
            db.collection("food")
                .whereEqualTo("foodID", cart.foodID)
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        foodObj = document.toObject(Food::class.java)
                    }
                    val tvFoodNameChoose = findViewById<TextView>(R.id.tvFoodNameChoose)
                    val tvFoodStallsNames = findViewById<TextView>(R.id.tvFoodStallsNames)
                    val imgFoodDetail = findViewById<ImageView>(R.id.imgFoodDetail)
                    val radioGroup = findViewById<RadioGroup>(R.id.rgFood)
                    val btnDecrease = findViewById<Button>(R.id.btnDecrease)
                    val btnIncrease = findViewById<Button>(R.id.btnIncrease)
                    val tvQuantity = findViewById<TextView>(R.id.tvQuantity)
                    val btnUpdateCart = findViewById<Button>(R.id.btnUpdateCart)
                    val btnRemoveCart = findViewById<Button>(R.id.btnRemoveCart)
                    val tfRemark = findViewById<TextView>(R.id.tfRemark)

                    tvFoodNameChoose.text = foodObj.name
                    tvFoodStallsNames.text = foodObj.foodstallID
                    var imgName = foodObj.image
                    val storageRef =
                        FirebaseStorage.getInstance().reference.child("foodimg/$imgName")
                    val localfile = File.createTempFile("tempImage", "png")

                    storageRef.getFile(localfile).addOnSuccessListener {

                        val bitmap = BitmapFactory.decodeFile(localfile.absolutePath)
                        imgFoodDetail.setImageBitmap(bitmap)
                    }
                    quantity = cart.quantity.toString().toInt()
                    tvQuantity.text = quantity.toString()

                    /*radioGroup.check(R.id.rbNoSpicy)*/
                    btnDecrease.setOnClickListener() {
                        if (quantity != 1) {
                            quantity -= 1
                        }
                        tvQuantity.text = quantity.toString()

                    }

                    btnIncrease.setOnClickListener() {
                        quantity += 1
                        tvQuantity.text = quantity.toString()
                    }

                    tfRemark.text = cart.Remark

                    btnRemoveCart.setOnClickListener(){
                        val washingtonRef = db.collection("cartDetail").document(cart.detailID.toString())
                        washingtonRef
                            .update("status", "Inactive")
                        val intent = Intent(this, User_Cart::class.java)
                        startActivity(intent)
                        finish()

                    }

                    btnUpdateCart.setOnClickListener() {


                        var subtotal =
                            foodObj.price.toString().toDouble()!! * tvQuantity.text.toString()
                                .toDouble()

                        val cartDetail = hashMapOf(
                            "foodID" to foodObj.foodID,
                            "quantity" to tvQuantity.text.trim().toString(),
                            "detailID" to cart.detailID,
                            "cartID" to cart.cartID,
                            "remark" to tfRemark.text.toString(),
                            "status" to "Active",
                            "name" to foodObj.name,
                            "subtotal" to subtotal.toString(),
                        )



                        db.collection("cartDetail")
                            .document(cart.detailID.toString()).set(cartDetail)
                            .addOnSuccessListener {
                                Toast.makeText(this, "Added Successfully", Toast.LENGTH_SHORT)
                                    .show()
                                finish()
                            }
                            .addOnFailureListener {
                                Toast.makeText(
                                    this,
                                    "Added Failed",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                        val intent = Intent(this, User_Cart::class.java)
                        startActivity(intent)
                        finish()


                    }


                }

        }

    }


}


