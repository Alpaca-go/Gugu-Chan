package com.guguchan.app.data.model

data class TemplateModel(
    val templateId: String,
    val templateName: String,
    val templateType: String,
    val backgroundImage: String,
    val canvasWidth: Int,
    val canvasHeight: Int,
    val exportFormat: String,
    val textFields: List<TemplateTextField>
)

data class TemplateTextField(
    val fieldId: String,
    val fieldName: String,
    val x: Int,
    val y: Int,
    val width: Int,
    val height: Int,
    val fontSize: Float,
    val minFontSize: Float,
    val fontColor: String,
    val fontWeight: String,
    val align: String,
    val maxLength: Int,
    val autoResize: Boolean,
    val singleLine: Boolean
)
