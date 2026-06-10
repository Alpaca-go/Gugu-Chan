package com.guguchan.app.domain.usecase

import android.content.Context
import com.guguchan.app.data.model.GenerateRecordModel
import com.guguchan.app.data.model.GenerateStatus
import com.guguchan.app.data.model.OrderModel
import com.guguchan.app.data.model.TemplateModel

class BatchGenerateUseCase(
    private val generateImageUseCase: GenerateImageUseCase
) {
    fun generateBatch(
        context: Context,
        template: TemplateModel,
        orders: List<OrderModel>
    ): List<GenerateRecordModel> {
        return orders.map { order ->
            val result = generateImageUseCase.generate(
                context = context,
                template = template,
                order = order,
                saveToGallery = true
            )
            GenerateRecordModel(
                recordId = "${order.orderId}_${template.templateId}_${System.currentTimeMillis()}",
                orderId = order.orderId,
                consumerId = order.consumerId,
                templateId = template.templateId,
                imagePath = result.imagePath.orEmpty(),
                source = order.source,
                status = if (result.success) GenerateStatus.SUCCESS else GenerateStatus.FAILED,
                createdAt = System.currentTimeMillis(),
                errorMessage = result.errorMessage
            )
        }
    }
}
