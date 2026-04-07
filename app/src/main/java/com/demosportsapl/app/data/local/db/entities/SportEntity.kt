package com.demosportsapl.app.data.local.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sports")
data class SportEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val description: String = ""
)
