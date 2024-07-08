package com.example.deliveryapp.models

data class Order(
    val dateTime: String,
    val deliveryStatus: String,
    val items: List<OrderItem>,
    val location: String,
    val storeName: String
)

data class OrderItem(
    val productPrice: Double,
    val productTitle: String
)