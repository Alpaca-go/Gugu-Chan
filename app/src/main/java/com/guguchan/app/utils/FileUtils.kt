package com.guguchan.app.utils

import android.content.Context
import android.net.Uri
import java.io.File
import java.io.FileOutputStream
import java.util.UUID

object FileUtils {
    fun cacheImportedFile(context: Context, uri: Uri, suffix: String): File {
        val target = File(context.cacheDir, "import_${UUID.randomUUID()}.$suffix")
        context.contentResolver.openInputStream(uri)?.use { input ->
            FileOutputStream(target).use { output -> input.copyTo(output) }
        }
        return target
    }
}
