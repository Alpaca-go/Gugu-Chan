package com.guguchan.app.data.local

import androidx.room.TypeConverter
import com.guguchan.app.data.model.GenerateStatus
import com.guguchan.app.data.model.OrderSource

class RoomConverters {
    @TypeConverter
    fun toOrderSource(value: String): OrderSource = OrderSource.valueOf(value)

    @TypeConverter
    fun fromOrderSource(value: OrderSource): String = value.name

    @TypeConverter
    fun toGenerateStatus(value: String): GenerateStatus = GenerateStatus.valueOf(value)

    @TypeConverter
    fun fromGenerateStatus(value: GenerateStatus): String = value.name
}
