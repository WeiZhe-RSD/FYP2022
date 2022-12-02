package com.example.fyp.Entity

data class EwalletTransaction (
    var walletID:String?=null,
    var transactionID:String?= null,
    var type:String?= null,
    var from:String?=null,
    var to:String?=null,

    var date:String?=null,
    var status:String?=null,
    var amount:String?=null,
)