package com.example.fyp.Entity

data class Payment(
    var paymentID:String?= null,
    var amount:String? = null,
    var type:String? = null,

    var date:String?= null,
    var status:String?= null,

    var orderID: String? = null,
)
