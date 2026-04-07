package com.demosportsapl.app.data.repository

import com.demosportsapl.app.data.local.db.dao.ResultDao
import com.demosportsapl.app.data.local.db.entities.ResultEntity
import kotlinx.coroutines.flow.Flow

class ResultRepository(private val dao: ResultDao) {
    fun getAllResults(): Flow<List<ResultEntity>> = dao.getAllResults()
    suspend fun getByFixtureId(fixtureId: Int): ResultEntity? = dao.getByFixtureId(fixtureId)
    suspend fun insertAll(results: List<ResultEntity>) = dao.insertAll(results)
    suspend fun insert(result: ResultEntity) = dao.insert(result)
    suspend fun update(result: ResultEntity) = dao.update(result)
}
