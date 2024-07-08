package com.example.deliveryapp.utils

import com.example.deliveryapp.models.NestedRecyclerModelFood
import com.example.deliveryapp.models.NestedRecyclerModelMain

object SampleData {
    private val foodModel = listOf(
        NestedRecyclerModelFood(Images.imageUrl0,true,"abc"),
        NestedRecyclerModelFood(Images.imageUrl1,true,"abc"),
        NestedRecyclerModelFood(Images.imageUrl2,true,"abc"),
        NestedRecyclerModelFood(Images.imageUrl3,true,"abc"),
        NestedRecyclerModelFood(Images.imageUrl4,true,"abc"),
        NestedRecyclerModelFood(Images.imageUrl5,true,"abc"),
        NestedRecyclerModelFood(Images.imageUrl6,true,"abc"),
        NestedRecyclerModelFood(Images.imageUrl7,true,"abc"),
        NestedRecyclerModelFood(Images.imageUrl8,true,"abc"),
        NestedRecyclerModelFood(Images.imageUrl9,true,"abc")
    )

    val collections = listOf(
        NestedRecyclerModelMain("At Your DoorStep", foodModel),
        NestedRecyclerModelMain("Shop By Shop", foodModel.reversed()),
        NestedRecyclerModelMain("Treat Yourself", foodModel.shuffled()),
        NestedRecyclerModelMain("Shops and Restaurant", foodModel),
    )
}