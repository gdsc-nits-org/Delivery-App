package com.example.deliveryapp.models

data class NestedRecyclerModelFood(
    val imageUrl: String,
    val shopName: String,
    val totalOrders: Int,
    val phoneNo: String,
    val location: String,
    val isOpen: Boolean
)