package com.guguchan.app.render

import android.content.Context
import android.graphics.BitmapFactory
import com.guguchan.app.utils.ImageUtils

class BitmapExporter {
    fun export(context: Context, imagePath: String, fileName: String): String? {
        val bitmap = BitmapFactory.decodeFile(imagePath) ?: return null
        return ImageUtils.saveBitmapToGallery(context, bitmap, fileName)
    }
}
