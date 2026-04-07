package com.demosportsapl.app.data.local.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "results")
data class ResultEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val fixtureId: Int,
    val homeScore: Int,
    val awayScore: Int,
    val isWalkover: Boolean = false,
    val medalType: String? = null  // GOLD, SILVER, BRONZE
)
