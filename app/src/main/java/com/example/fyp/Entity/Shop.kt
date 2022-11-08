package com.example.fyp.Entity

data class Shop(
    var name:String? = null,
    var image:String? = null,
    var status: String?= null,
    val foods:Map<String,Int> ?= null,
)
