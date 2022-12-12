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
import com.example.fyp.Entity.Cafeteria
import com.example.fyp.Entity.FoodStall
import com.google.firebase.firestore.*
import com.google.firebase.storage.FirebaseStorage
import java.io.File

class User_Foodstall : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var foodstallArrayList: ArrayList<FoodStall>
    private lateinit var foodstallAdapter:FoodStallAdapter
    private lateinit var db : FirebaseFirestore
    private lateinit var cafeterianame:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_foodstall)

        val cafeteria  = intent.getParcelableExtra<Cafeteria>("cafeteria")

        if(cafeteria!= null){
            val imgCafeteriaChoose = findViewById<ImageView>(R.id.imgCafeteriaChoose)
            val tvCafeName = findViewById<TextView>(R.id.tvCafeName)
            val btnCafeteriaMap = findViewById<Button>(R.id.btnCafeteriaMap)

            tvCafeName.text = cafeteria.name
            cafeterianame = cafeteria.name.toString()
            var imgName = cafeteria.image
            val storageRef = FirebaseStorage.getInstance().reference.child("cafeteriaimg/$imgName")
            val localfile = File.createTempFile("tempImage", "png")

            storageRef.getFile(localfile).addOnSuccessListener {

                val bitmap = BitmapFactory.decodeFile(localfile.absolutePath)
                imgCafeteriaChoose.setImageBitmap(bitmap)
            }

            recyclerView = findViewById(R.id.rvFoodStall)
            recyclerView.layoutManager = LinearLayoutManager(this)
            recyclerView.setHasFixedSize(true)
            foodstallArrayList = arrayListOf()

            foodstallAdapter = FoodStallAdapter(foodstallArrayList)

            recyclerView.adapter = foodstallAdapter

            setDataInList()

            foodstallAdapter.onItemClick = {
                val intent = Intent(this, User_Menu::class.java)
                intent.putExtra("foodstall", it)
                intent.putExtra("filter", "")
                startActivity(intent)
            }

            btnCafeteriaMap.setOnClickListener {
                val intent = Intent(this, com.example.fyp.Map::class.java)
                intent.putExtra("cafeteria", cafeteria)

                startActivity(intent)
            }




        }
    }

    private fun setDataInList(){



        db = FirebaseFirestore.getInstance()
        db.collection("foodstall").addSnapshotListener(object : EventListener<QuerySnapshot> {

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
                        foodstallArrayList.add((dc.document.toObject(FoodStall::class.java)))
                        if(foodstallArrayList.get(foodstallArrayList.size - 1).status == "Inactive" || foodstallArrayList.get(foodstallArrayList.size - 1).cafeteriaID != cafeterianame){
                            foodstallArrayList.removeAt(foodstallArrayList.size - 1)
                        }
                    }
                }
                foodstallAdapter.notifyDataSetChanged()
            }
        })


    }
}