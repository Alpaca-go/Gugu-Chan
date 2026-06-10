package com.guguchan.app.utils

import java.io.InputStream
import java.io.InputStreamReader
import org.apache.commons.csv.CSVFormat

class CsvParser {
    fun parse(inputStream: InputStream): List<Map<String, String>> {
        val reader = InputStreamReader(inputStream, Charsets.UTF_8)
        val records = CSVFormat.DEFAULT.builder()
            .setHeader()
            .setSkipHeaderRecord(true)
            .setTrim(true)
            .build()
            .parse(reader)

        return records.map { record ->
            record.toMap().mapValues { it.value ?: "" }
        }
    }
}
