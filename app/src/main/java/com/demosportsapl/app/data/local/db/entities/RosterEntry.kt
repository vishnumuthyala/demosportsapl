package com.demosportsapl.app.data.local.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "roster_entries")
data class RosterEntry(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val tableId: Int,
    val sportId: Int,
    val playerName: String,
    val playerRole: String = "Player",
    val isLocked: Boolean = false
)
