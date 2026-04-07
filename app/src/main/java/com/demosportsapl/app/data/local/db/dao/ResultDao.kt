package com.demosportsapl.app.data.local.db.dao

import androidx.room.*
import com.demosportsapl.app.data.local.db.entities.ResultEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ResultDao {
    @Query("SELECT * FROM results")
    fun getAllResults(): Flow<List<ResultEntity>>

    @Query("SELECT * FROM results WHERE fixtureId = :fixtureId")
    suspend fun getByFixtureId(fixtureId: Int): ResultEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(results: List<ResultEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(result: ResultEntity)

    @Update
    suspend fun update(result: ResultEntity)
}
