package com.guguchan.app.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import com.guguchan.app.data.entity.GenerateRecordEntity
import com.guguchan.app.data.local.GenerateRecordDao
import com.guguchan.app.data.model.GenerateRecordModel

class GenerateRecordRepository(
    private val dao: GenerateRecordDao
) {
    fun observeRecords(): Flow<List<GenerateRecordModel>> = dao.observeAll().map { list ->
        list.map(GenerateRecordEntity::toModel)
    }

    suspend fun saveRecord(record: GenerateRecordModel) {
        dao.insert(record.toEntity())
    }

    suspend fun clear() = dao.clear()
}

fun GenerateRecordModel.toEntity(): GenerateRecordEntity = GenerateRecordEntity(
    recordId = recordId,
    orderId = orderId,
    consumerId = consumerId,
    templateId = templateId,
    imagePath = imagePath,
    source = source,
    status = status,
    createdAt = createdAt,
    errorMessage = errorMessage
)

fun GenerateRecordEntity.toModel(): GenerateRecordModel = GenerateRecordModel(
    recordId = recordId,
    orderId = orderId,
    consumerId = consumerId,
    templateId = templateId,
    imagePath = imagePath,
    source = source,
    status = status,
    createdAt = createdAt,
    errorMessage = errorMessage
)
