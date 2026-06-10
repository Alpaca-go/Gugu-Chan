package com.guguchan.app.data.repository

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject
import com.guguchan.app.data.model.TemplateModel
import com.guguchan.app.data.model.TemplateSummary
import com.guguchan.app.data.model.TemplateTextField

class TemplateRepository(
    private val context: Context
) {
    fun loadTemplates(): List<TemplateSummary> {
        val templateFolders = context.assets.list("templates").orEmpty()
        return templateFolders.map { folder ->
            val jsonText = context.assets.open("templates/$folder/template.json").bufferedReader().use { it.readText() }
            val template = parseTemplate(JSONObject(jsonText))
            TemplateSummary(
                template = template,
                previewAssetPath = "templates/$folder/${template.backgroundImage}"
            )
        }
    }

    private fun parseTemplate(json: JSONObject): TemplateModel {
        val fields = json.getJSONArray("textFields").toTextFields()
        return TemplateModel(
            templateId = json.getString("templateId"),
            templateName = json.getString("templateName"),
            templateType = json.getString("templateType"),
            backgroundImage = json.getString("backgroundImage"),
            canvasWidth = json.getInt("canvasWidth"),
            canvasHeight = json.getInt("canvasHeight"),
            exportFormat = json.getString("exportFormat"),
            textFields = fields
        )
    }

    private fun JSONArray.toTextFields(): List<TemplateTextField> = buildList {
        for (index in 0 until length()) {
            val item = getJSONObject(index)
            add(
                TemplateTextField(
                    fieldId = item.getString("fieldId"),
                    fieldName = item.getString("fieldName"),
                    x = item.getInt("x"),
                    y = item.getInt("y"),
                    width = item.getInt("width"),
                    height = item.getInt("height"),
                    fontSize = item.getDouble("fontSize").toFloat(),
                    minFontSize = item.getDouble("minFontSize").toFloat(),
                    fontColor = item.getString("fontColor"),
                    fontWeight = item.getString("fontWeight"),
                    align = item.getString("align"),
                    maxLength = item.getInt("maxLength"),
                    autoResize = item.getBoolean("autoResize"),
                    singleLine = item.getBoolean("singleLine")
                )
            )
        }
    }
}
