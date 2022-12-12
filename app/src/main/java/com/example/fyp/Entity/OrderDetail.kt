package com.example.fyp.Entity

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

data class OrderDetail(


    var orderID:String?= null,
    var orderDetailID:String? = null,
    var OrderContent:Map<String, Int>? = null,

    var status:String?= null,
    var price:String?= null,
)
