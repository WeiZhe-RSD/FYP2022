package com.example.fyp.Entity

data class Feedback(
    var feedbackID:String?=null,
    var rating:String?= null,
    var description:String?= null,
    var date:String?=null,
    var status:String?=null,

    var customerID:String?=null,
    var foodstallID:String?=null,
    var orderID:String?=null,
)
