package com.demosportsapl.app.data.repository

import com.demosportsapl.app.data.local.db.dao.AlertDao
import com.demosportsapl.app.data.local.db.entities.AlertEntity
import kotlinx.coroutines.flow.Flow

class AlertRepository(private val dao: AlertDao) {
    fun getAll(): Flow<List<AlertEntity>> = dao.getAll()
    fun getUnreadCount(): Flow<Int> = dao.getUnreadCount()
    suspend fun insertAll(alerts: List<AlertEntity>) = dao.insertAll(alerts)
    suspend fun insert(alert: AlertEntity) = dao.insert(alert)
    suspend fun markRead(id: Int) = dao.markRead(id)
    suspend fun markAllRead() = dao.markAllRead()
    suspend fun count(): Int = dao.count()
}
