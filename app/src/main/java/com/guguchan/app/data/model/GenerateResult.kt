package com.guguchan.app.data.model

data class GenerateResult(
    val success: Boolean,
    val imagePath: String? = null,
    val previewPath: String? = null,
    val errorMessage: String? = null
)
