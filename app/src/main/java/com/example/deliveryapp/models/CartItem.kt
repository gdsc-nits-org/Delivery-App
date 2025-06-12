package com.example.deliveryapp.models

data class CartItem(
    val id: Long,
    val title: String = "",
    val imageUrl: String,
    var quantity: Int = 0,
    val price: Int = 0
)