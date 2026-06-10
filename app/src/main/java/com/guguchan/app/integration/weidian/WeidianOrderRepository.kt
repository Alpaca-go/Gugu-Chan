package com.guguchan.app.integration.weidian

import com.guguchan.app.data.model.OrderModel

class WeidianOrderRepository(
    private val apiClient: WeidianApiClient
) {
    suspend fun syncOrders(): List<OrderModel> = emptyList()
}
