package com.example.fyp.Entity

data class Order(

    var orderID:String?= null,
    var date:String? = null,
    var userID:String? = null,
    var orderDetailID:String? =null,

    var paymentID:String?= null,
    var ttlPrice:String?= null,

    var status: String? = null,
    var foodstallID:String?=null,



)
