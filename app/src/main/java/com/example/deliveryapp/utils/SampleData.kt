

package com.example.deliveryapp.utils

import com.example.deliveryapp.models.NestedRecyclerModelFood
import com.example.deliveryapp.models.NestedRecyclerModelMain

object SampleData {
    private val foodModel = listOf(
        NestedRecyclerModelFood(Images.imageUrl0, "Shop 1", 100, "123-456-7890", "Location 1"),
        NestedRecyclerModelFood(Images.imageUrl1, "Shop 2", 150, "234-567-8901", "Location 2"),
        NestedRecyclerModelFood(Images.imageUrl2, "Shop 3", 200, "345-678-9012", "Location 3"),
        NestedRecyclerModelFood(Images.imageUrl3, "Shop 4", 250, "456-789-0123", "Location 4"),
        NestedRecyclerModelFood(Images.imageUrl4, "Shop 5", 300, "567-890-1234", "Location 5"),
        NestedRecyclerModelFood(Images.imageUrl5, "Shop 6", 350, "678-901-2345", "Location 6"),
        NestedRecyclerModelFood(Images.imageUrl6, "Shop 7", 400, "789-012-3456", "Location 7"),
        NestedRecyclerModelFood(Images.imageUrl7, "Shop 8", 450, "890-123-4567", "Location 8"),
        NestedRecyclerModelFood(Images.imageUrl8, "Shop 9", 500, "901-234-5678", "Location 9"),
        NestedRecyclerModelFood(Images.imageUrl9, "Shop 10", 550, "012-345-6789", "Location 10")
    )

    val collections = listOf(
        NestedRecyclerModelMain("At Your DoorStep", foodModel),
        NestedRecyclerModelMain("Shop By Shop", foodModel.reversed()),
        NestedRecyclerModelMain("Treat Yourself", foodModel.shuffled()),
        NestedRecyclerModelMain("Shops and Restaurant", foodModel),
    )
}