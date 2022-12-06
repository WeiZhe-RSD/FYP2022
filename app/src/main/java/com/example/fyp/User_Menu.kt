package com.example.fyp

import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fyp.Entity.Cafeteria
import com.example.fyp.Entity.Food
import com.example.fyp.Entity.FoodStall
import com.google.firebase.firestore.*
import com.google.firebase.storage.FirebaseStorage
import java.io.File

class User_Menu : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var foodArrayList: ArrayList<Food>
    private lateinit var foodAdapter:FoodAdapter
    private lateinit var db : FirebaseFirestore
    private lateinit var foodstallname:String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_menu)
        val foodstall  = intent.getParcelableExtra<FoodStall>("foodstall")

        if(foodstall!= null){
            val imgFoodStalls = findViewById<ImageView>(R.id.imgFoodStalls)
            val tvFoodStallNameChoose = findViewById<TextView>(R.id.tvFoodStallNameChoose)
            val tvCafeteriaNames = findViewById<TextView>(R.id.tvCafeteriaNames)

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


                for(dc : DocumentChange in value?.documentChanges!!){

                    if(dc.type == DocumentChange.Type.ADDED){
                        foodArrayList.add((dc.document.toObject(Food::class.java)))
                        if(foodArrayList.get(foodArrayList.size - 1).status == "Inactive" || foodArrayList.get(foodArrayList.size - 1).foodstallID != foodstallname){
                            foodArrayList.removeAt(foodArrayList.size - 1)
                        }
                    }
                }
                foodAdapter.notifyDataSetChanged()
            }
        })



    }
}