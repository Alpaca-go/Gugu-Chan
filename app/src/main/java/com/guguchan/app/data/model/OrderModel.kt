package com.guguchan.app.data.model

data class OrderModel(
    val orderId: String,
    val consumerId: String,
    val buyerName: String? = null,
    val receiverName: String? = null,
    val phoneMasked: String? = null,
    val itemTitle: String? = null,
    val quantity: Int? = null,
    val orderStatus: String? = null,
    val createdAt: Long,
    val source: OrderSource,
    val rawData: String? = null
)
