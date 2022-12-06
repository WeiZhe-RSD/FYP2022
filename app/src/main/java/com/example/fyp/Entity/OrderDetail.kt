package com.example.fyp.Entity

data class OrderDetail(


    var orderID:String?= null,
    var orderDetailID:String? = null,
    var orderContent:Map<String, Int>? = null,

    var status:String?= null,
    var price:String?= null,

)

data class OrderContentDetail(
    var foodID:String? =null,
    var quantity:Long
)
