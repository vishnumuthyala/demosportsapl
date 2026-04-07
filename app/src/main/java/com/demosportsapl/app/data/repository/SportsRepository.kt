package com.demosportsapl.app.data.repository

import com.demosportsapl.app.data.local.db.dao.SportDao
import com.demosportsapl.app.data.local.db.entities.SportEntity
import kotlinx.coroutines.flow.Flow

class SportsRepository(private val dao: SportDao) {
    fun getAllSports(): Flow<List<SportEntity>> = dao.getAllSports()
    suspend fun getSportById(id: Int): SportEntity? = dao.getSportById(id)
    suspend fun insertAll(sports: List<SportEntity>) = dao.insertAll(sports)
    suspend fun count(): Int = dao.count()
}
