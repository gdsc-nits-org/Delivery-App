package com.example.deliveryapp.utils

import com.example.deliveryapp.models.NestedRecyclerModelFood
import com.example.deliveryapp.models.NestedRecyclerModelMain

object SampleData {
    private val foodModel = listOf(
        NestedRecyclerModelFood(Images.imageUrl0),
        NestedRecyclerModelFood(Images.imageUrl1),
        NestedRecyclerModelFood(Images.imageUrl2),
        NestedRecyclerModelFood(Images.imageUrl3),
        NestedRecyclerModelFood(Images.imageUrl4),
        NestedRecyclerModelFood(Images.imageUrl5),
        NestedRecyclerModelFood(Images.imageUrl6),
        NestedRecyclerModelFood(Images.imageUrl7),
        NestedRecyclerModelFood(Images.imageUrl8),
        NestedRecyclerModelFood(Images.imageUrl9)
    )

    val collections = listOf(
        NestedRecyclerModelMain("At Your DoorStep", foodModel),
        NestedRecyclerModelMain("Shop By Shop", foodModel.reversed()),
        NestedRecyclerModelMain("Treat Yourself", foodModel.shuffled()),
        NestedRecyclerModelMain("Shops and Restaurant", foodModel),
    )
}