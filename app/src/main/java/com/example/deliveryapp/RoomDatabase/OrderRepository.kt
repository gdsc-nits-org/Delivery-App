package com.example.deliveryapp.RoomDatabase

import androidx.lifecycle.LiveData

class OrderRepository(private val orderDao: OrderDao) {

    val allOrders: LiveData<List<Orders>> = orderDao.getOrders()

    suspend fun insertOrder(order: Orders) {
        orderDao.insertOrder(order)
    }

    suspend fun finishOrder(uid: Long) {
        orderDao.finishOrder(uid)
    }

    suspend fun deleteOrder(uid: Long) {
        orderDao.deleteOrderById(uid)
    }

    suspend fun getOrderById(uid: Long): Orders {
        return orderDao.getOrderById(uid)
    }
}
