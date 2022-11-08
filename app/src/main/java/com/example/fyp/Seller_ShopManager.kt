package com.example.fyp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fyp.Entity.Cafeteria
import com.example.fyp.Entity.Shop
import com.google.firebase.firestore.*

class Seller_ShopManager : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var shopArrayList: ArrayList<Shop>
    private lateinit var shopAdapter: ShopAdapter
    private lateinit var db : FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seller_shop_manager)

        recyclerView = findViewById(R.id.rvShop)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        shopArrayList = arrayListOf()

        shopAdapter = ShopAdapter(shopArrayList)

        recyclerView.adapter = shopAdapter

        setDataInList()
/*
        shopAdapter.onItemClick = {
            val intent = Intent(this, User_Foodstall::class.java)
            intent.putExtra("event", it)
            startActivity(intent)
        }
 */

    }

    private fun setDataInList(){
        db = FirebaseFirestore.getInstance()
        db.collection("shop").addSnapshotListener(object : EventListener<QuerySnapshot> {

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
                        shopArrayList.add((dc.document.toObject(Shop::class.java)))
                        if(shopArrayList.get(shopArrayList.size - 1).status == "Inactive"){
                            shopArrayList.removeAt(shopArrayList.size - 1)
                        }
                    }
                }


                shopAdapter.notifyDataSetChanged()
            }
        })


    }
}