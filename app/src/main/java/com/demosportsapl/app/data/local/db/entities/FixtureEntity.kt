package com.demosportsapl.app.data.local.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "fixtures")
data class FixtureEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val sportId: Int,
    val homeTableId: Int,
    val awayTableId: Int,
    val scheduledAt: Long,
    val venue: String = "",
    val status: String = "SCHEDULED"  // SCHEDULED, LIVE, COMPLETED, WALKOVER
)
