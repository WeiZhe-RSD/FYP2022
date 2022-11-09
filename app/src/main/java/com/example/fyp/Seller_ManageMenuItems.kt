package com.example.fyp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fyp.Entity.FoodStall
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*

class Seller_ManageMenuItems : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var MenuItemArrayList: ArrayList<FoodStall>
    private lateinit var MenuItemAdapter: MenuItemAdapter
    private lateinit var db : FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seller_manage_menu_items)

        val btnAdd = findViewById<Button>(R.id.btnAdd)

        recyclerView = findViewById(R.id.rvMenuItems)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        MenuItemArrayList = arrayListOf()

        MenuItemAdapter = MenuItemAdapter(MenuItemArrayList)

        recyclerView.adapter = MenuItemAdapter

        setDataInList()

        /*MenuItemAdapter.onItemClick = {
            val intent = Intent(this, Seller_AddItems::class.java)
            intent.putExtra("food", it)
            startActivity(intent)
        }*/

        btnAdd.setOnClickListener() {
            val intent = Intent(this@Seller_ManageMenuItems, Seller_AddItems::class.java)
            startActivity(intent)
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
                        MenuItemArrayList.add((dc.document.toObject(FoodStall::class.java)))
                        if(MenuItemArrayList.get(MenuItemArrayList.size - 1).status == "Inactive"){
                            MenuItemArrayList.removeAt(MenuItemArrayList.size - 1)
                        }
                    }
                }
                MenuItemAdapter.notifyDataSetChanged()
            }
        })


    }
}