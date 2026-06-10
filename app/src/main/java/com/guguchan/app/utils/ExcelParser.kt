package com.guguchan.app.utils

import java.io.InputStream
import org.apache.poi.ss.usermodel.DataFormatter
import org.apache.poi.ss.usermodel.WorkbookFactory

class ExcelParser {
    fun parse(inputStream: InputStream): List<Map<String, String>> {
        val formatter = DataFormatter()
        val workbook = WorkbookFactory.create(inputStream)
        workbook.use { wb ->
            val sheet = wb.getSheetAt(0)
            val headerRow = sheet.getRow(sheet.firstRowNum) ?: return emptyList()
            val headers = headerRow.map { formatter.formatCellValue(it).trim() }
            return buildList {
                for (rowIndex in (sheet.firstRowNum + 1)..sheet.lastRowNum) {
                    val row = sheet.getRow(rowIndex) ?: continue
                    add(
                        headers.mapIndexed { index, header ->
                            header to formatter.formatCellValue(row.getCell(index)).trim()
                        }.toMap()
                    )
                }
            }
        }
    }
}
