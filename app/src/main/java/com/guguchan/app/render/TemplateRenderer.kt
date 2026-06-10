package com.guguchan.app.render

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import java.io.File
import java.io.FileOutputStream
import java.util.UUID
import com.guguchan.app.data.model.OrderModel
import com.guguchan.app.data.model.TemplateModel

class TemplateRenderer(
    private val textFieldRenderer: TextFieldRenderer
) {
    fun renderToCache(
        context: Context,
        template: TemplateModel,
        order: OrderModel
    ): File {
        val background = loadBackground(context, template)
        val resultBitmap = Bitmap.createBitmap(
            template.canvasWidth,
            template.canvasHeight,
            Bitmap.Config.ARGB_8888
        )

        val canvas = Canvas(resultBitmap)
        canvas.drawBitmap(background, 0f, 0f, null)
        template.textFields.forEach { field ->
            val value = when (field.fieldId) {
                "consumer_id" -> order.consumerId
                "buyer_name" -> order.buyerName.orEmpty()
                "order_id" -> order.orderId
                else -> ""
            }
            textFieldRenderer.drawText(canvas, value, field)
        }

        val file = File(context.cacheDir, "preview_${UUID.randomUUID()}.png")
        FileOutputStream(file).use { output ->
            resultBitmap.compress(Bitmap.CompressFormat.PNG, 100, output)
        }
        return file
    }

    private fun loadBackground(context: Context, template: TemplateModel): Bitmap {
        val path = "templates/${template.templateId}/${template.backgroundImage}"
        return context.assets.open(path).use { stream ->
            BitmapFactory.decodeStream(stream)
        }
    }
}
