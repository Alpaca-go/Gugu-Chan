package com.guguchan.app.render

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.text.TextPaint
import com.guguchan.app.data.model.TemplateTextField

class TextFieldRenderer {
    fun drawText(canvas: Canvas, text: String, field: TemplateTextField) {
        val displayText = if (text.length > field.maxLength) {
            text.take(field.maxLength - 1) + "…"
        } else {
            text
        }

        val paint = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor(field.fontColor)
            textAlign = when (field.align.lowercase()) {
                "left" -> Paint.Align.LEFT
                "right" -> Paint.Align.RIGHT
                else -> Paint.Align.CENTER
            }
            textSize = field.fontSize
            isFakeBoldText = field.fontWeight.equals("bold", ignoreCase = true)
        }

        if (field.autoResize) {
            while (paint.textSize > field.minFontSize &&
                paint.measureText(displayText) > field.width
            ) {
                paint.textSize -= 1f
            }
        }

        val bounds = Rect()
        paint.getTextBounds(displayText, 0, displayText.length, bounds)
        val centerX = field.x + (field.width / 2f)
        val centerY = field.y + (field.height / 2f)
        val baseline = centerY - bounds.exactCenterY()
        canvas.drawText(displayText, centerX, baseline, paint)
    }
}
