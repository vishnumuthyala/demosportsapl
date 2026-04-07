package com.demosportsapl.app.data.local.db.dao

import androidx.room.*
import com.demosportsapl.app.data.local.db.entities.RosterEntry
import kotlinx.coroutines.flow.Flow

@Dao
interface RosterDao {
    @Query("SELECT * FROM roster_entries WHERE tableId = :tableId ORDER BY playerName")
    fun getByTable(tableId: Int): Flow<List<RosterEntry>>

    @Query("SELECT * FROM roster_entries WHERE tableId = :tableId AND sportId = :sportId ORDER BY playerName")
    fun getByTableAndSport(tableId: Int, sportId: Int): Flow<List<RosterEntry>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(entries: List<RosterEntry>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entry: RosterEntry)

    @Update
    suspend fun update(entry: RosterEntry)

    @Delete
    suspend fun delete(entry: RosterEntry)

    @Query("UPDATE roster_entries SET isLocked = :locked WHERE tableId = :tableId AND sportId = :sportId")
    suspend fun setLockStatus(tableId: Int, sportId: Int, locked: Boolean)
}
