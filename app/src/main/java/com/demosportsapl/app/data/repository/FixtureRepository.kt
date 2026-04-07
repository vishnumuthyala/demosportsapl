package com.demosportsapl.app.data.repository

import com.demosportsapl.app.data.local.db.dao.FixtureDao
import com.demosportsapl.app.data.local.db.entities.FixtureEntity
import kotlinx.coroutines.flow.Flow

class FixtureRepository(private val dao: FixtureDao) {
    fun getAllFixtures(): Flow<List<FixtureEntity>> = dao.getAllFixtures()
    fun getByTable(tableId: Int): Flow<List<FixtureEntity>> = dao.getFixturesByTable(tableId)
    fun getBySport(sportId: Int): Flow<List<FixtureEntity>> = dao.getFixturesBySport(sportId)
    fun getLiveAndUpcoming(cutoff: Long): Flow<List<FixtureEntity>> = dao.getLiveAndUpcoming(cutoff)
    suspend fun getById(id: Int): FixtureEntity? = dao.getById(id)
    suspend fun insertAll(fixtures: List<FixtureEntity>) = dao.insertAll(fixtures)
    suspend fun insert(fixture: FixtureEntity): Long = dao.insert(fixture)
    suspend fun update(fixture: FixtureEntity) = dao.update(fixture)
    suspend fun count(): Int = dao.count()
}
