package com.guguchan.app.data.model

data class GenerateRecordModel(
    val recordId: String,
    val orderId: String,
    val consumerId: String,
    val templateId: String,
    val imagePath: String,
    val source: OrderSource,
    val status: GenerateStatus,
    val createdAt: Long,
    val errorMessage: String? = null
)
