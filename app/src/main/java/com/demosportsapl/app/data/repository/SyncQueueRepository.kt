package com.demosportsapl.app.data.repository

import com.demosportsapl.app.data.local.db.dao.SyncQueueDao
import com.demosportsapl.app.data.local.db.entities.SyncQueueItem
import kotlinx.coroutines.flow.Flow

class SyncQueueRepository(private val dao: SyncQueueDao) {
    fun getPending(): Flow<List<SyncQueueItem>> = dao.getPending()
    suspend fun insert(item: SyncQueueItem) = dao.insert(item)
    suspend fun markSynced(id: Int) = dao.markSynced(id)
}
