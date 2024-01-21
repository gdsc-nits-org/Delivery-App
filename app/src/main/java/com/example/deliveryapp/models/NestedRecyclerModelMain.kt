package com.example.deliveryapp.models

data class NestedRecyclerModelMain(
    val title : String,
    val movieModel: List<NestedRecyclerModelFood>
)

