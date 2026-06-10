package com.guguchan.app.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.guguchan.app.data.entity.AppSettingsEntity
import com.guguchan.app.data.entity.GenerateRecordEntity
import com.guguchan.app.data.entity.OrderEntity

@Database(
    entities = [OrderEntity::class, GenerateRecordEntity::class, AppSettingsEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(RoomConverters::class)
abstract class GuguChanDatabase : RoomDatabase() {
    abstract fun orderDao(): OrderDao
    abstract fun generateRecordDao(): GenerateRecordDao
    abstract fun appSettingsDao(): AppSettingsDao
}
