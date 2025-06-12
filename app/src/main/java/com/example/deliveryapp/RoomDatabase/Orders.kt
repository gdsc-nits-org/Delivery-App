package com.example.deliveryapp.RoomDatabase

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Orders(
    var title:String,
    var imageUrl:String,
    var resName:String,
    var price:String,
    var quantity: Int = 0,
    var isFinished: Int = 0,
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
)