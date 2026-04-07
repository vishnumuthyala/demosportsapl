package com.demosportsapl.app.data.local.db.dao

import androidx.room.*
import com.demosportsapl.app.data.local.db.entities.FixtureEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FixtureDao {
    @Query("SELECT * FROM fixtures ORDER BY scheduledAt")
    fun getAllFixtures(): Flow<List<FixtureEntity>>

    @Query("SELECT * FROM fixtures WHERE sportId = :sportId ORDER BY scheduledAt")
    fun getFixturesBySport(sportId: Int): Flow<List<FixtureEntity>>

    @Query("SELECT * FROM fixtures WHERE homeTableId = :tableId OR awayTableId = :tableId ORDER BY scheduledAt")
    fun getFixturesByTable(tableId: Int): Flow<List<FixtureEntity>>

    @Query("SELECT * FROM fixtures WHERE status = 'LIVE' OR (status = 'SCHEDULED' AND scheduledAt <= :cutoff) ORDER BY scheduledAt LIMIT 8")
    fun getLiveAndUpcoming(cutoff: Long): Flow<List<FixtureEntity>>

    @Query("SELECT * FROM fixtures WHERE id = :id")
    suspend fun getById(id: Int): FixtureEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(fixtures: List<FixtureEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(fixture: FixtureEntity): Long

    @Update
    suspend fun update(fixture: FixtureEntity)

    @Query("SELECT COUNT(*) FROM fixtures")
    suspend fun count(): Int
}
