package com.guguchan.app.domain.usecase

import android.content.Context
import java.util.UUID
import com.guguchan.app.data.model.GenerateResult
import com.guguchan.app.data.model.OrderModel
import com.guguchan.app.data.model.TemplateModel
import com.guguchan.app.render.BitmapExporter
import com.guguchan.app.render.TemplateRenderer
import com.guguchan.app.utils.FileUtils

class GenerateImageUseCase(
    private val templateRenderer: TemplateRenderer,
    private val bitmapExporter: BitmapExporter
) {
    fun generate(
        context: Context,
        template: TemplateModel,
        order: OrderModel,
        saveToGallery: Boolean
    ): GenerateResult {
        return try {
            val previewFile = templateRenderer.renderToCache(context, template, order)
            val localFile = FileUtils.persistGeneratedImage(
                context = context,
                sourceFile = previewFile,
                fileName = "${order.orderId}_${template.templateId}.png"
            )
            val exportedPath = if (saveToGallery) {
                bitmapExporter.export(
                    context = context,
                    imagePath = localFile.absolutePath,
                    fileName = "${order.orderId}_${UUID.randomUUID()}_${template.templateId}.png"
                )
            } else {
                null
            }

            GenerateResult(
                success = true,
                localPath = localFile.absolutePath,
                imagePath = exportedPath ?: localFile.absolutePath,
                previewPath = previewFile.absolutePath
            )
        } catch (error: Throwable) {
            GenerateResult(
                success = false,
                errorMessage = error.message ?: "生成失败"
            )
        }
    }
}
