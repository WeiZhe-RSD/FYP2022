package com.example.fyp

import android.content.ContentValues.TAG
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.fyp.Entity.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.AggregateSource
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.io.File

class User_MenuVariant : AppCompatActivity() {
    private var quantity: Int = 1
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    var check: Boolean = true
    private lateinit var userObj: User
    private lateinit var cartObj: Cart
    private lateinit var cartDetailObj: CartDetail
    private var sameqtt: Int = 0
    private var sameID: Int = 0
    private lateinit var tempObj: CartDetail
    private lateinit var foodObj: Food
    private lateinit var builder: AlertDialog.Builder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_menu_variant)
        val food = intent.getParcelableExtra<Food>("food")


        if (food != null) {
            Log.i("knnnnnnnnnnnnnnn", food.toString())


            builder = AlertDialog.Builder(this)
            val qqq = findViewById<TextView>(R.id.ttttt)
            qqq.text = "Remarks (Optional)"
            val tvFoodNameChoose = findViewById<TextView>(R.id.tvFoodNameChoose)
            val tvFoodStallsNames = findViewById<TextView>(R.id.tvFoodStallsNames)
            val imgFoodDetail = findViewById<ImageView>(R.id.imgFoodDetail)
            val radioGroup = findViewById<RadioGroup>(R.id.rgFood)
            val btnDecrease = findViewById<Button>(R.id.btnDecrease)
            val btnIncrease = findViewById<Button>(R.id.btnIncrease)
            val tvQuantity = findViewById<TextView>(R.id.tvQuantity)
            val btnAddToCart = findViewById<Button>(R.id.btnAddToCart)
            val tfRemark = findViewById<TextView>(R.id.tfRemark)
            val tvShowPrice = findViewById<TextView>(R.id.tvShowPrice)
            var im: String = ""
            var detailSize: Int = 0

            var tllo:Int = tvQuantity.text.toString().toInt() * food.price.toString().toInt()

            btnAddToCart.text = "Add to Cart - RM $tllo"
            /*radioGroup.visibility*/

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
            tvShowPrice.text = "RM ${food.price}"

            var imgName = food.image
            val storageRef = FirebaseStorage.getInstance().reference.child("foodimg/$imgName")
            val localfile = File.createTempFile("tempImage", "png")

            storageRef.getFile(localfile).addOnSuccessListener {

                val bitmap = BitmapFactory.decodeFile(localfile.absolutePath)
                imgFoodDetail.setImageBitmap(bitmap)
            }

            btnDecrease.setOnClickListener() {
                if (quantity != 1) {
                    quantity -= 1
                }
                tvQuantity.text = quantity.toString()
                var tl:Int = tvQuantity.text.toString().toInt() * food.price.toString().toInt()

                btnAddToCart.text = "Add to Cart - RM $tl"

            }

            btnIncrease.setOnClickListener() {
                quantity += 1
                tvQuantity.text = quantity.toString()

                var tl:Int = tvQuantity.text.toString().toInt() * food.price.toString().toInt()

                btnAddToCart.text = "Add to Cart - RM $tl"
            }

            btnAddToCart.setOnClickListener() {
                auth = FirebaseAuth.getInstance()
                val user = auth.currentUser
                db = Firebase.firestore

                val userRef = db.collection("user").document(user?.email.toString())
                userRef.get().addOnSuccessListener {
                    if (it != null) {
                        userObj = it.toObject(User::class.java)!!

                        db = FirebaseFirestore.getInstance()
                        db.collection("cart")
                            .whereEqualTo("customerID", userObj.id)
                            .get()
                            .addOnSuccessListener { documentss ->

                                for (documentsss in documentss) {
                                    cartObj = documentsss.toObject(Cart::class.java)
                                }

                                val db2 = FirebaseFirestore.getInstance()

                                val collection = db2.collection("cartDetail")
                                val countQuery = collection.whereEqualTo("status","Active").whereEqualTo("cartID", cartObj.cartID).count()
                                countQuery.get(AggregateSource.SERVER)
                                    .addOnCompleteListener { taskv ->
                                        if (taskv.isSuccessful) {
                                            val snapshots = taskv.result
                                            var bigSize = snapshots.count.toInt()

                                            if (bigSize != 0) {

                                                db = FirebaseFirestore.getInstance()
                                                db.collection("cartDetail")
                                                    .whereEqualTo("cartID", cartObj.cartID)
                                                    .whereEqualTo("status", "Active")
                                                    .get()
                                                    .addOnSuccessListener { documentssx ->

                                                        for (documentsssx in documentssx) {
                                                            cartDetailObj =
                                                                documentsssx.toObject(CartDetail::class.java)

                                                            if (food.foodID == cartDetailObj.foodID) {
                                                                sameqtt =
                                                                    cartDetailObj.quantity.toString()
                                                                        .toInt()
                                                                sameID =
                                                                    cartDetailObj.detailID.toString()
                                                                        .toInt()
                                                            }
                                                        }

                                                        db = FirebaseFirestore.getInstance()
                                                        db.collection("food")
                                                            .whereEqualTo(
                                                                "foodID",
                                                                cartDetailObj.foodID
                                                            )
                                                            .get()
                                                            .addOnSuccessListener { documentssxx ->

                                                                for (documentsssxx in documentssxx) {
                                                                    foodObj =
                                                                        documentsssxx.toObject(Food::class.java)

                                                                }
Log.i("walaoooooo", food.foodstallID.toString())
                                                                Log.i("walaoooooo", foodObj.foodstallID.toString())

                                                                if (food.foodstallID != foodObj.foodstallID) {
                                                                    check = false

                                                                    var dialog = customDialogFragment()

                                                                    dialog.show(supportFragmentManager, "customerDialog")


                                                                    /*builder.setTitle("Remove your previous items?")
                                                                        .setMessage("You have food from other foodstall in your cart. If you continue your previous items in cart will be removed.")
                                                                        .setCancelable(true)
                                                                        .setPositiveButton("Remove") { dialogInterface, it ->
                                                                            db =
                                                                                FirebaseFirestore.getInstance()
                                                                            db.collection("cartDetail")
                                                                                .whereEqualTo(
                                                                                    "cartID",
                                                                                    cartObj.cartID
                                                                                )
                                                                                .whereEqualTo(
                                                                                    "status",
                                                                                    "Active"
                                                                                )
                                                                                .get()
                                                                                .addOnSuccessListener { documentssxz ->

                                                                                    for (documentsssxz in documentssxz) {
                                                                                        tempObj =
                                                                                            documentsssxz.toObject(
                                                                                                CartDetail::class.java
                                                                                            )

                                                                                        val washingtonRef =
                                                                                            db.collection(
                                                                                                "cartDetail"
                                                                                            )
                                                                                                .document(
                                                                                                    tempObj.detailID.toString()
                                                                                                )
                                                                                        washingtonRef
                                                                                            .update(
                                                                                                "status",
                                                                                                "Removed"
                                                                                            )
                                                                                    }

                                                                                }


                                                                        }
                                                                        .setNegativeButton("No") { dialogInterface, it ->
                                                                            check = false
                                                                            dialogInterface.cancel()
                                                                        }.show()*/


                                                                }

                                                                if (check) {
                                                                    val spicyLevel =
                                                                        radioGroup.checkedRadioButtonId
                                                                    val result = when (spicyLevel) {
                                                                        R.id.rbNoSpicy -> "No"
                                                                        R.id.rbLessSpicy -> "Less"
                                                                        R.id.rbSpicy -> "Yes"

                                                                        else -> "Error"
                                                                    }


                                                                    val db =
                                                                        FirebaseFirestore.getInstance()

                                                                    val collection =
                                                                        db.collection("cartDetail")
                                                                    val countQuery =
                                                                        collection.count()
                                                                    countQuery.get(AggregateSource.SERVER)
                                                                        .addOnCompleteListener { task ->
                                                                            if (task.isSuccessful) {
                                                                                val snapshot =
                                                                                    task.result
                                                                                detailSize =
                                                                                    snapshot.count.toInt()
                                                                                detailSize += 1

                                                                                if (sameID != 0) {
                                                                                    detailSize =
                                                                                        sameID
                                                                                }

                                                                                var qtt =
                                                                                    tvQuantity.text.trim()
                                                                                        .toString()
                                                                                        .toInt()

                                                                                sameqtt += tvQuantity.text.trim()
                                                                                    .toString()
                                                                                    .toInt()


                                                                                var subtotal =
                                                                                    food.price.toString()
                                                                                        .toDouble()!! * tvQuantity.text.toString()
                                                                                        .toInt()

                                                                                val cartDetail =
                                                                                    hashMapOf(
                                                                                        "foodID" to food.foodID,
                                                                                        "quantity" to sameqtt.toString(),
                                                                                        "cartID" to cartObj.cartID,
                                                                                        "remark" to tfRemark.text.toString(),
                                                                                        "status" to "Active",
                                                                                        "name" to food.name,
                                                                                        "subtotal" to subtotal.toString(),
                                                                                        "detailID" to detailSize.toString(),
                                                                                        "orderID" to "",
                                                                                    )



                                                                                db.collection("cartDetail")
                                                                                    .document(
                                                                                        detailSize.toString()
                                                                                    )
                                                                                    .set(cartDetail)
                                                                                    .addOnSuccessListener {
                                                                                        Toast.makeText(
                                                                                            this,
                                                                                            "Added Successfully",
                                                                                            Toast.LENGTH_SHORT
                                                                                        ).show()

                                                                                        finish()
                                                                                    }
                                                                                    .addOnFailureListener {
                                                                                        Toast.makeText(
                                                                                            this,
                                                                                            "Added Failed",
                                                                                            Toast.LENGTH_SHORT
                                                                                        ).show()
                                                                                    }

                                                                            } else {

                                                                            }
                                                                        }
                                                                }


                                                            }


                                                    }

                                            } else {
                                                val result =
                                                    when (radioGroup.checkedRadioButtonId) {
                                                        R.id.rbNoSpicy -> "No"
                                                        R.id.rbLessSpicy -> "Less"
                                                        R.id.rbSpicy -> "Yes"

                                                        else -> "Error"
                                                    }


                                                val db =
                                                    FirebaseFirestore.getInstance()

                                                val collection =
                                                    db.collection("cartDetail")
                                                val countQuery = collection.count()
                                                countQuery.get(AggregateSource.SERVER)
                                                    .addOnCompleteListener { task ->
                                                        if (task.isSuccessful) {
                                                            val snapshot =
                                                                task.result
                                                            detailSize =
                                                                snapshot.count.toInt()
                                                            detailSize += 1



                                                            var subtotal =
                                                                food.price.toString()
                                                                    .toDouble()!! * tvQuantity.text.toString()
                                                                    .toInt()

                                                            val cartDetail =
                                                                hashMapOf(
                                                                    "foodID" to food.foodID,
                                                                    "quantity" to tvQuantity.text.trim()
                                                                        .toString(),
                                                                    "cartID" to cartObj.cartID,
                                                                    "remark" to tfRemark.text.toString(),
                                                                    "status" to "Active",
                                                                    "name" to food.name,
                                                                    "subtotal" to subtotal.toString(),
                                                                    "detailID" to detailSize.toString(),
                                                                    "orderID" to "",
                                                                )



                                                            db.collection("cartDetail")
                                                                .document(detailSize.toString())
                                                                .set(cartDetail)
                                                                .addOnSuccessListener {
                                                                    Toast.makeText(
                                                                        this,
                                                                        "Added Successfully",
                                                                        Toast.LENGTH_SHORT
                                                                    ).show()
                                                                    /*finish()*/
                                                                }
                                                                .addOnFailureListener {
                                                                    Toast.makeText(
                                                                        this,
                                                                        "Added Failed",
                                                                        Toast.LENGTH_SHORT
                                                                    ).show()
                                                                }
                                                        }
                                                    }
                                            }
                                        }/////
                                    }
                            }


                    }


                }


            }
        }
    }
}