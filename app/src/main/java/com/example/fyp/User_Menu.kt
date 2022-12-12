package com.example.fyp

import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fyp.Entity.*
import com.google.firebase.firestore.*
import com.google.firebase.storage.FirebaseStorage
import java.io.File
import java.math.RoundingMode
import java.text.DecimalFormat

class User_Menu : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var foodArrayList: ArrayList<Food>
    private lateinit var foodAdapter:FoodAdapter
    private lateinit var db : FirebaseFirestore
    private lateinit var foodstallname:String
    private lateinit var stallObj:FoodStall
    private lateinit var feedbackObj:Feedback
    private lateinit var filter:String
    var allRating:Double = 0.0
    var averageRating:Double = 0.0
    var count:Double = 0.0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_menu)
        val foodstall  = intent.getParcelableExtra<FoodStall>("foodstall")

        if(foodstall!= null){

            filter = intent.getStringExtra("filter")!!

            val imgFoodStalls = findViewById<ImageView>(R.id.imgFoodStalls)
            val tvFoodStallNameChoose = findViewById<TextView>(R.id.tvFoodStallNameChoose)
            val tvCafeteriaNames = findViewById<TextView>(R.id.tvCafeteriaNames)
            val btnShowRating = findViewById<Button>(R.id.btnShowRating)
            val tvInwaiting = findViewById<TextView>(R.id.tvInwaiting)
            val btnFilterFood = findViewById<Button>(R.id.btnFilterFood)
            val btnFilterBeverage = findViewById<Button>(R.id.btnFilterBeverage)
            val btnClearFilter = findViewById<Button>(R.id.btnClearFilter)

            btnClearFilter.setOnClickListener(){
                val intent = Intent(this, User_Menu::class.java)
                intent.putExtra("foodstall", foodstall)
                intent.putExtra("filter", "")
                startActivity(intent)
            }

            btnFilterFood.setOnClickListener(){

                if(filter == "Food"){
                    val intent = Intent(this, User_Menu::class.java)
                    intent.putExtra("foodstall", foodstall)
                    intent.putExtra("filter", "")
                    startActivity(intent)
                    finish()
                }else{
                    val intent = Intent(this, User_Menu::class.java)
                    intent.putExtra("filter", "Food")
                    intent.putExtra("foodstall", foodstall)
                    startActivity(intent)
                    finish()
                }

            }

            btnFilterBeverage.setOnClickListener(){

                if(filter == "Beverage"){
                    val intent = Intent(this, User_Menu::class.java)
                    intent.putExtra("foodstall", foodstall)
                    intent.putExtra("filter", "")
                    startActivity(intent)
                    finish()
                }else {
                    val intent = Intent(this, User_Menu::class.java)
                    intent.putExtra("filter", "Beverage")
                    intent.putExtra("foodstall", foodstall)
                    startActivity(intent)
                    finish()
                }
            }


            db = FirebaseFirestore.getInstance()
            db.collection("feedback")
                .whereEqualTo("foodstallID", foodstall.name)
                .get()
                .addOnSuccessListener { documentxx ->

                    for (documentx in documentxx) {
                        feedbackObj =
                            documentx.toObject(Feedback::class.java)
                        allRating += feedbackObj.rating.toString().toInt()
                        count +=1
                    }

                    averageRating = allRating / count

                    val df = DecimalFormat("#.##")
                    df.roundingMode = RoundingMode.DOWN
                    var roundoff = df.format(averageRating)

                    if(averageRating > 0){
                        btnShowRating.text = "$roundoff/5 Stars"
                    }else{
                        btnShowRating.text = "0/5 Stars"
                    }


                }
            var transSize:Int = 0
            val collection3 = db.collection("order")
            val countQuery3 = collection3.whereEqualTo("status", "Ordered").whereEqualTo("foodstallID", foodstall.name).count()
            countQuery3.get(AggregateSource.SERVER).addOnCompleteListener { taskssx ->
                if (taskssx.isSuccessful) {
                    val snapshot3 = taskssx.result
                    transSize = snapshot3.count.toInt()
                    tvInwaiting.text = transSize.toString() + " Inwaiting Orders"
                }
            }


            tvCafeteriaNames.text = foodstall.cafeteriaID
            tvFoodStallNameChoose.text = foodstall.name
            foodstallname = foodstall.name.toString()
            var imgName = foodstall.image
            val storageRef = FirebaseStorage.getInstance().reference.child("foodstallimg/$imgName")
            val localfile = File.createTempFile("tempImage", "png")

            storageRef.getFile(localfile).addOnSuccessListener {

                val bitmap = BitmapFactory.decodeFile(localfile.absolutePath)
                imgFoodStalls.setImageBitmap(bitmap)
            }

            recyclerView = findViewById(R.id.rvFood)
            recyclerView.layoutManager = LinearLayoutManager(this)
            recyclerView.setHasFixedSize(true)
            foodArrayList = arrayListOf()

            foodAdapter = FoodAdapter(foodArrayList)

            recyclerView.adapter = foodAdapter

            setDataInList()

            btnShowRating.setOnClickListener(){
                val intent = Intent(this, User_FoodstallFeedback::class.java)
                intent.putExtra("foodstall", foodstall.name)
                startActivity(intent)
            }

            foodAdapter.onItemClick = {
                val intent = Intent(this, User_MenuVariant::class.java)
                intent.putExtra("food", it)
                startActivity(intent)
            }
        }
    }

    private fun setDataInList(){



        db = FirebaseFirestore.getInstance()
        db.collection("food").addSnapshotListener(object : EventListener<QuerySnapshot> {

            override fun onEvent(
                value: QuerySnapshot?,
                error: FirebaseFirestoreException?
            ){
                if(error != null){
                    Log.e("Firestore Error", error.message.toString())
                    return
                }

                if(filter == "Food" || filter == "Beverage") {
                    for (dc: DocumentChange in value?.documentChanges!!) {

                        if (dc.type == DocumentChange.Type.ADDED) {
                            foodArrayList.add((dc.document.toObject(Food::class.java)))
                            if (foodArrayList.get(foodArrayList.size - 1).status == "Inactive" || foodArrayList.get(
                                    foodArrayList.size - 1
                                ).foodstallID != foodstallname || filter != foodArrayList.get(
                                    foodArrayList.size - 1
                                ).type
                            ) {

                                foodArrayList.removeAt(foodArrayList.size - 1)


                            }
                        }
                    }
                }else{
                    for (dc: DocumentChange in value?.documentChanges!!) {

                        if (dc.type == DocumentChange.Type.ADDED) {
                            foodArrayList.add((dc.document.toObject(Food::class.java)))
                            if (foodArrayList.get(foodArrayList.size - 1).status == "Inactive" || foodArrayList.get(
                                    foodArrayList.size - 1
                                ).foodstallID != foodstallname
                            ) {

                                foodArrayList.removeAt(foodArrayList.size - 1)


                            }
                        }
                    }
                }
                foodAdapter.notifyDataSetChanged()
            }
        })



    }
}