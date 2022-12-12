package com.example.fyp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.fyp.Entity.Cart
import com.example.fyp.Entity.CartDetail
import com.example.fyp.Entity.Food
import com.example.fyp.Entity.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.AggregateSource
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class customDialogFragment : DialogFragment() {
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    private lateinit var userObj: User
    private lateinit var cartObj: Cart
    private lateinit var cartDetailObj: CartDetail
    private lateinit var tempObj: CartDetail
    private lateinit var foodObj:Food


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var rootView: View = inflater.inflate(R.layout.fragment_customer_dialog, container, false)
        var ac: View = inflater.inflate(R.layout.activity_user_menu_variant, container, false)

        rootView.findViewById<Button>(R.id.btnDialogRemove).setOnClickListener() {

            dismiss()
        }


        return rootView
    }

}