package com.demosportsapl.app.data.local.db.dao

import androidx.room.*
import com.demosportsapl.app.data.local.db.entities.SportEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SportDao {
    @Query("SELECT * FROM sports ORDER BY name")
    fun getAllSports(): Flow<List<SportEntity>>

    @Query("SELECT * FROM sports WHERE id = :id")
    suspend fun getSportById(id: Int): SportEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(sports: List<SportEntity>)

    @Query("SELECT COUNT(*) FROM sports")
    suspend fun count(): Int
}
