package com.example.fyp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fyp.Entity.Cafeteria
import com.google.firebase.firestore.*

class User_Cafeteria : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var cafeteriaArrayList: ArrayList<Cafeteria>
    private lateinit var cafeteriaAdapter:CafeteriaAdapter
    private lateinit var db : FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_cafeteria)

        recyclerView = findViewById(R.id.rvCafeteria)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        cafeteriaArrayList = arrayListOf()

        cafeteriaAdapter = CafeteriaAdapter(cafeteriaArrayList)

        recyclerView.adapter = cafeteriaAdapter

        setDataInList()

        cafeteriaAdapter.onItemClick = {
            val intent = Intent(this, User_Foodstall::class.java)
            intent.putExtra("cafeteria", it)
            startActivity(intent)
        }
    }

    private fun setDataInList(){



        db = FirebaseFirestore.getInstance()
        db.collection("cafeteria").addSnapshotListener(object : EventListener<QuerySnapshot> {

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
                        cafeteriaArrayList.add((dc.document.toObject(Cafeteria::class.java)))
                        if(cafeteriaArrayList.get(cafeteriaArrayList.size - 1).status == "Inactive"){
                            cafeteriaArrayList.removeAt(cafeteriaArrayList.size - 1)
                        }
                    }
                }
/*
                tempArrayList.addAll(eventArrayList)
*/
                cafeteriaAdapter.notifyDataSetChanged()
            }
        })


    }
}