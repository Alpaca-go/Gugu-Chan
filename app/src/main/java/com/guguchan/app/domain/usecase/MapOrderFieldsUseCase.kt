package com.guguchan.app.domain.usecase

import java.util.UUID
import com.guguchan.app.data.model.FieldMapping
import com.guguchan.app.data.model.OrderModel
import com.guguchan.app.data.model.OrderSource

class MapOrderFieldsUseCase {
    fun mapFields(
        rawRows: List<Map<String, String>>,
        fieldMapping: FieldMapping,
        source: OrderSource
    ): List<OrderModel> {
        return rawRows.mapNotNull { row ->
            val consumerId = row[fieldMapping.consumerIdColumn].orEmpty().trim()
            if (consumerId.isBlank()) {
                null
            } else {
                OrderModel(
                    orderId = row.valueFor(fieldMapping.orderIdColumn).ifBlank { "order_${UUID.randomUUID()}" },
                    consumerId = consumerId,
                    buyerName = row.valueFor(fieldMapping.buyerNameColumn).ifBlank { null },
                    itemTitle = row.valueFor(fieldMapping.itemTitleColumn).ifBlank { null },
                    orderStatus = row.valueFor(fieldMapping.orderStatusColumn).ifBlank { null },
                    createdAt = System.currentTimeMillis(),
                    source = source,
                    rawData = row.toString()
                )
            }
        }
    }
}

private fun Map<String, String>.valueFor(key: String?): String = key?.let { this[it] }.orEmpty().trim()
