package com.example.deliveryapp.RoomDatabase

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface OrderDao {
    @Insert
    suspend fun insertOrder(order: Orders)

    @Query("Update Orders Set quantity = :newQuantity where id= :uid")
    suspend fun update(uid: Long, newQuantity: Int)

    @Query("Update Orders Set isFinished = 1 where id=:uid")
    fun finishOrder(uid: Long)

    @Query
        ("Delete From Orders where id=:uid ")
    fun deleteOrderById(uid: Long)

    @Query("SELECT * FROM Orders where isFinished == 0")
    fun getOrders(): LiveData<List<Orders>>

    @Query("SELECT * FROM Orders WHERE id = :uid")
    fun getOrderById(uid: Long): Orders
}