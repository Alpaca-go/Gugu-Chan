package com.guguchan.app.domain.usecase

import android.content.Context
import android.net.Uri
import com.guguchan.app.data.model.OrderSource
import com.guguchan.app.utils.CsvParser
import com.guguchan.app.utils.ExcelParser

class ImportOrderUseCase(
    private val csvParser: CsvParser,
    private val excelParser: ExcelParser
) {
    fun import(context: Context, uri: Uri): Pair<OrderSource, List<Map<String, String>>> {
        val name = uri.lastPathSegment.orEmpty().lowercase()
        return when {
            name.endsWith(".csv") -> OrderSource.CSV_IMPORT to context.contentResolver.openInputStream(uri).use {
                csvParser.parse(requireNotNull(it))
            }
            name.endsWith(".xlsx") || name.endsWith(".xls") -> OrderSource.EXCEL_IMPORT to context.contentResolver.openInputStream(uri).use {
                excelParser.parse(requireNotNull(it))
            }
            else -> error("不支持的文件格式")
        }
    }
}
