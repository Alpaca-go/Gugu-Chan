package com.guguchan.app.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.guguchan.app.data.model.GenerateStatus
import com.guguchan.app.data.model.OrderSource

@Entity(tableName = "generate_records")
data class GenerateRecordEntity(
    @PrimaryKey val recordId: String,
    val orderId: String,
    val consumerId: String,
    val templateId: String,
    val imagePath: String,
    val source: OrderSource,
    val status: GenerateStatus,
    val createdAt: Long,
    val errorMessage: String?
)
