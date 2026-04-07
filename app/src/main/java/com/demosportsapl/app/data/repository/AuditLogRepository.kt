package com.demosportsapl.app.data.repository

import com.demosportsapl.app.data.local.db.dao.AuditLogDao
import com.demosportsapl.app.data.local.db.entities.AuditLog
import kotlinx.coroutines.flow.Flow

class AuditLogRepository(private val dao: AuditLogDao) {
    fun getAll(): Flow<List<AuditLog>> = dao.getAll()
    suspend fun insert(log: AuditLog) = dao.insert(log)
    suspend fun insertAll(logs: List<AuditLog>) = dao.insertAll(logs)
}
