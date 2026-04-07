package com.demosportsapl.app.data.repository

import com.demosportsapl.app.data.local.db.dao.RosterDao
import com.demosportsapl.app.data.local.db.entities.RosterEntry
import kotlinx.coroutines.flow.Flow

class RosterRepository(private val dao: RosterDao) {
    fun getByTable(tableId: Int): Flow<List<RosterEntry>> = dao.getByTable(tableId)
    fun getByTableAndSport(tableId: Int, sportId: Int): Flow<List<RosterEntry>> =
        dao.getByTableAndSport(tableId, sportId)
    suspend fun insertAll(entries: List<RosterEntry>) = dao.insertAll(entries)
    suspend fun insert(entry: RosterEntry) = dao.insert(entry)
    suspend fun update(entry: RosterEntry) = dao.update(entry)
    suspend fun delete(entry: RosterEntry) = dao.delete(entry)
    suspend fun setLockStatus(tableId: Int, sportId: Int, locked: Boolean) =
        dao.setLockStatus(tableId, sportId, locked)
}
