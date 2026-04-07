package com.demosportsapl.app.data.local.db.dao

import androidx.room.*
import com.demosportsapl.app.data.local.db.entities.SyncQueueItem
import kotlinx.coroutines.flow.Flow

@Dao
interface SyncQueueDao {
    @Query("SELECT * FROM sync_queue WHERE isSynced = 0 ORDER BY createdAt")
    fun getPending(): Flow<List<SyncQueueItem>>

    @Insert
    suspend fun insert(item: SyncQueueItem)

    @Query("UPDATE sync_queue SET isSynced = 1 WHERE id = :id")
    suspend fun markSynced(id: Int)
}
