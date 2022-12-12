package com.example.fyp.Entity

data class Report(
    var foodID: String? = null,
    var foodName: String? = null,
    var date: String? = null,
    var quantity: Int? = null,
    var subtotal: Double? = null,
)