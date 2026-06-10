package com.guguchan.app.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import com.guguchan.app.data.entity.GenerateRecordEntity

@Dao
interface GenerateRecordDao {
    @Query("SELECT * FROM generate_records ORDER BY createdAt DESC")
    fun observeAll(): Flow<List<GenerateRecordEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: GenerateRecordEntity)

    @Query("DELETE FROM generate_records")
    suspend fun clear()
}
