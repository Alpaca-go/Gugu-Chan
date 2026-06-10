package com.guguchan.app.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.guguchan.app.data.model.OrderSource

@Entity(tableName = "orders")
data class OrderEntity(
    @PrimaryKey val orderId: String,
    val consumerId: String,
    val buyerName: String?,
    val receiverName: String?,
    val phoneMasked: String?,
    val itemTitle: String?,
    val quantity: Int?,
    val orderStatus: String?,
    val createdAt: Long,
    val source: OrderSource,
    val importedAt: Long,
    val rawData: String?
)
