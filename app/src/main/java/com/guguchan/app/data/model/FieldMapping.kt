package com.guguchan.app.data.model

data class FieldMapping(
    val consumerIdColumn: String,
    val orderIdColumn: String? = null,
    val buyerNameColumn: String? = null,
    val createdAtColumn: String? = null,
    val itemTitleColumn: String? = null,
    val orderStatusColumn: String? = null
)
