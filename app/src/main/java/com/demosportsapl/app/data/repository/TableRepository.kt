package com.demosportsapl.app.data.repository

import com.demosportsapl.app.data.local.db.dao.TableDao
import com.demosportsapl.app.data.local.db.entities.TableEntity
import kotlinx.coroutines.flow.Flow

class TableRepository(private val dao: TableDao) {
    fun getAllTables(): Flow<List<TableEntity>> = dao.getAllTables()
    suspend fun getTableById(id: Int): TableEntity? = dao.getTableById(id)
    suspend fun insertAll(tables: List<TableEntity>) = dao.insertAll(tables)
    suspend fun count(): Int = dao.count()
}
