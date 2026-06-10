package com.guguchan.app.integration.weidian

class WeidianApiClient {
    suspend fun getOrderList(
        startTime: Long,
        endTime: Long,
        page: Int,
        pageSize: Int
    ): List<WeidianOrderDto> = emptyList()
}

data class WeidianOrderDto(
    val orderId: String,
    val buyerId: String,
    val buyerName: String?,
    val createdAt: Long,
    val status: String
)
