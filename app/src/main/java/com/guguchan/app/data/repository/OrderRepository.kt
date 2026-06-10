package com.guguchan.app.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import com.guguchan.app.data.entity.OrderEntity
import com.guguchan.app.data.local.OrderDao
import com.guguchan.app.data.model.OrderModel

class OrderRepository(
    private val orderDao: OrderDao
) {
    fun observeOrders(): Flow<List<OrderModel>> = orderDao.observeAll().map { list ->
        list.map(OrderEntity::toModel)
    }

    suspend fun saveOrders(orders: List<OrderModel>) {
        val now = System.currentTimeMillis()
        orderDao.upsertAll(orders.map { it.toEntity(importedAt = now) })
    }

    suspend fun clear() = orderDao.clear()
}

fun OrderModel.toEntity(importedAt: Long): OrderEntity = OrderEntity(
    orderId = orderId,
    consumerId = consumerId,
    buyerName = buyerName,
    receiverName = receiverName,
    phoneMasked = phoneMasked,
    itemTitle = itemTitle,
    quantity = quantity,
    orderStatus = orderStatus,
    createdAt = createdAt,
    source = source,
    importedAt = importedAt,
    rawData = rawData
)

fun OrderEntity.toModel(): OrderModel = OrderModel(
    orderId = orderId,
    consumerId = consumerId,
    buyerName = buyerName,
    receiverName = receiverName,
    phoneMasked = phoneMasked,
    itemTitle = itemTitle,
    quantity = quantity,
    orderStatus = orderStatus,
    createdAt = createdAt,
    source = source,
    rawData = rawData
)
