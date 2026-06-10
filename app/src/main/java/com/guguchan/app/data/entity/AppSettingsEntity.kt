package com.guguchan.app.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "app_settings")
data class AppSettingsEntity(
    @PrimaryKey val id: Int = 1,
    val defaultTemplateId: String? = null,
    val privacyMode: String = "full",
    val exportFormat: String = "png"
)
